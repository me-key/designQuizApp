package com.antigravity.systemdesignmaster.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.antigravity.systemdesignmaster.data.Question
import com.antigravity.systemdesignmaster.data.Subject
import com.antigravity.systemdesignmaster.repository.QuizRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(
    val isLoading: Boolean = true,
    val currentQuestion: Question? = null,
    val selectedOptionIndex: Int? = null,
    val isAnswered: Boolean = false,
    val isCorrect: Boolean = false,
    val stats: Triple<Int, Int, Int> = Triple(0, 0, 0), // Correct, Attempted, Streak
    val progress: Pair<Int, Int> = Pair(0, 0), // Mastered, Total
    val quizComplete: Boolean = false
)

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {


    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()
    
    // Generation State
    private val _generationState = MutableStateFlow<GenerationState>(GenerationState.Idle)
    val generationState: StateFlow<GenerationState> = _generationState.asStateFlow()

    private var currentSubject: String = "General"

    init {
        initializeQuiz()
    }

    private fun initializeQuiz() {
        viewModelScope.launch {
            repository.initializeQuestionsIfEmpty()
            refreshSubjects()
        }
    }
    
    fun refreshSubjects() {
        viewModelScope.launch {
            _subjects.value = repository.getSubjects()
        }
    }
    
    fun resetSubject(subject: String) {
        viewModelScope.launch {
            repository.resetSubject(subject)
            refreshSubjects()
        }
    }
    
    fun deleteSubject(subject: String) {
        viewModelScope.launch {
            repository.deleteSubject(subject)
            refreshSubjects()
        }
    }
    
    fun addSubject(topic: String) {
        viewModelScope.launch {
            _generationState.value = GenerationState.Loading(topic)
            val success = repository.addNewSubject(topic)
            if (success) {
                _generationState.value = GenerationState.Success
                refreshSubjects()
            } else {
                _generationState.value = GenerationState.Error("Failed to generate questions. Try a different topic.")
            }
            // Reset state after a delay or UI consumption
            delay(3000)
            _generationState.value = GenerationState.Idle
        }
    }
    
    fun setSubject(subject: String) {
        currentSubject = subject
        loadNextQuestion()
    }

    fun loadNextQuestion() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val nextQuestion = repository.getNextQuestion(currentSubject)
            val stats = repository.getStats()
            val progress = repository.getProgress(currentSubject)
            
            _uiState.update {
                it.copy(
                    isLoading = false,
                    currentQuestion = nextQuestion,
                    selectedOptionIndex = null,
                    isAnswered = false,
                    isCorrect = false,
                    stats = stats,
                    progress = progress,
                    quizComplete = nextQuestion == null
                )
            }
        }
    }

    fun submitAnswer(optionIndex: Int) {
        val currentState = _uiState.value
        val question = currentState.currentQuestion ?: return

        if (currentState.isAnswered) return // Prevent double submission

        val isCorrect = question.correctAnswerIndex == optionIndex
        
            viewModelScope.launch {
            repository.processAnswer(question, isCorrect)
            val newStats = repository.getStats()
            val newProgress = repository.getProgress(currentSubject)
            
            _uiState.update {
                it.copy(
                    selectedOptionIndex = optionIndex,
                    isAnswered = true,
                    isCorrect = isCorrect,
                    stats = newStats,
                    progress = newProgress
                )
            }
        }
    }
}

sealed class GenerationState {
    object Idle : GenerationState()
    data class Loading(val topic: String) : GenerationState()
    object Success : GenerationState()
    data class Error(val message: String) : GenerationState()
}

class QuizViewModelFactory(private val repository: QuizRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
