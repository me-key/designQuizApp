package com.antigravity.systemdesignmaster.repository

import android.content.Context
import android.content.SharedPreferences
import com.antigravity.systemdesignmaster.data.AppDatabase
import com.antigravity.systemdesignmaster.data.Question
import com.antigravity.systemdesignmaster.data.QuestionStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

import com.antigravity.systemdesignmaster.data.Subject
import com.antigravity.systemdesignmaster.data.QuestionGeneratorService
import kotlinx.coroutines.flow.first

data class JsonQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val subject: String = "General"
)

class QuizRepository(private val context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val dao = db.quizDao()
    private val prefs: SharedPreferences = context.getSharedPreferences("quiz_stats", Context.MODE_PRIVATE)
    // IMPORTANT: Inject or pass API key. For now, reading from BuildConfig
    private val questionGenerator = QuestionGeneratorService(com.antigravity.systemdesignmaster.BuildConfig.GEMINI_API_KEY)

    suspend fun initializeQuestionsIfEmpty() {
        withContext(Dispatchers.IO) {
            if (dao.getQuestionCount() == 0) {
                val questions = loadQuestionsFromAssets()
                // Migration Logic:
                // 1. Identify unique subjects
                val uniqueSubjects = questions.map { it.legacySubject }.distinct()
                
                // 2. Insert Subjects and get their IDs
                val subjectMap = mutableMapOf<String, Long>()
                for (subName in uniqueSubjects) {
                    val id = dao.insertSubject(Subject(name = subName))
                    subjectMap[subName] = id
                }

                // 3. Transform questions to use subjectId and insert
                val roomQuestions = questions.map { q ->
                    Question(
                        text = q.question,
                        option0 = q.options.getOrElse(0) { "" },
                        option1 = q.options.getOrElse(1) { "" },
                        option2 = q.options.getOrElse(2) { "" },
                        option3 = q.options.getOrElse(3) { "" },
                        correctAnswerIndex = q.correctAnswerIndex,
                        explanation = q.explanation,
                        subjectId = subjectMap[q.legacySubject] ?: 0L,
                        status = QuestionStatus.NEW
                    )
                }
                if (roomQuestions.isNotEmpty()) {
                    dao.insertQuestions(roomQuestions)
                }
                
                // Ensure "General" exists if not already
                if (!subjectMap.containsKey("General")) {
                    dao.insertSubject(Subject(name = "General"))
                }
            }
        }
    }

    private data class TempJsonQuestion(
        val question: String,
        val options: List<String>,
        val correctAnswerIndex: Int,
        val explanation: String,
        val subject: String = "General"
    ) {
        val legacySubject: String get() = subject
    }

    private fun loadQuestionsFromAssets(): List<TempJsonQuestion> {
        return try {
            val jsonString = context.assets.open("system_design_questions.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val listType = object : TypeToken<List<TempJsonQuestion>>() {}.type
            gson.fromJson(jsonString, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun addNewSubject(topic: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Check if subject already exists
                if (dao.getSubjectByName(topic) != null) return@withContext true
                
                // 2. Generate content
                 val generatedQuestions = questionGenerator.generateQuestions(topic)
                 if (generatedQuestions.isEmpty()) return@withContext false
                 
                 // 3. Insert Subject
                 val subjectId = dao.insertSubject(Subject(name = topic))
                 
                 // 4. Insert Questions
                 val questions = generatedQuestions.map { q ->
                      Question(
                        text = q.question,
                        option0 = q.options.getOrElse(0) { "" },
                        option1 = q.options.getOrElse(1) { "" },
                        option2 = q.options.getOrElse(2) { "" },
                        option3 = q.options.getOrElse(3) { "" },
                        correctAnswerIndex = q.correct_index,
                        explanation = q.explanation,
                        subjectId = subjectId,
                        status = QuestionStatus.NEW
                      )
                 }
                 dao.insertQuestions(questions)
                 true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun deleteSubject(subjectName: String) {
        withContext(Dispatchers.IO) {
            val subject = dao.getSubjectByName(subjectName)
            if (subject != null) {
                dao.deleteSubject(subject.id)
            }
        }
    }


    suspend fun getNextQuestion(subjectName: String): Question? {
        return withContext(Dispatchers.IO) {
            val subject = dao.getSubjectByName(subjectName) ?: return@withContext null
            val subjectId = subject.id
            
            val allActive = dao.getActiveQuestionsBySubject(subjectId)
            val totalAnswered = prefs.getInt("total_questions_answered_counter", 0)

            // 1. Check Learning Queue (must be > 3 questions ago)
            val readyLearning = allActive.filter { 
                it.status == QuestionStatus.LEARNING && (totalAnswered - it.lastAnsweredTimestamp) > 3 
            }

            if (readyLearning.isNotEmpty()) {
                return@withContext readyLearning.random()
            }

            // 2. Check New Questions
            val newQuestions = allActive.filter { it.status == QuestionStatus.NEW }
            if (newQuestions.isNotEmpty()) {
                return@withContext newQuestions.random()
            }
            
             if (allActive.isNotEmpty()) {
                  // Only cooling down learning questions left.
                  return@withContext allActive.minByOrNull { it.lastAnsweredTimestamp }
             }

            // 3. If Active is empty, return Mastered (Done state or Review)
             val mastered = dao.getMasteredQuestionsBySubject(subjectId)
             if (mastered.isNotEmpty()) {
                 return@withContext null
             }
             
             null
        }
    }

    suspend fun processAnswer(question: Question, isCorrect: Boolean) {
        val currentCounter = prefs.getInt("total_questions_answered_counter", 0) + 1
        
        // Update Stats (Global stats)
        val totalAttempts = prefs.getInt("total_attempted", 0) + 1
        var totalCorrect = prefs.getInt("total_correct", 0)
        var streak = prefs.getInt("streak", 0)

        val newStatus = if (isCorrect) {
            totalCorrect++
            streak++
            QuestionStatus.MASTERED
        } else {
            streak = 0
            QuestionStatus.LEARNING
        }

        prefs.edit()
            .putInt("total_questions_answered_counter", currentCounter)
            .putInt("total_attempted", totalAttempts)
            .putInt("total_correct", totalCorrect)
            .putInt("streak", streak)
            .apply()

        // Update Question in DB
        val updatedQuestion = question.copy(
            status = newStatus,
            lastAnsweredTimestamp = currentCounter.toLong()
        )
        dao.updateQuestion(updatedQuestion)
    }

    fun getStats(): Triple<Int, Int, Int> {
        val totalCorrect = prefs.getInt("total_correct", 0)
        val totalAttempted = prefs.getInt("total_attempted", 0)
        val streak = prefs.getInt("streak", 0)
        return Triple(totalCorrect, totalAttempted, streak)
    }

    suspend fun getProgress(subjectName: String): Pair<Int, Int> {
        return withContext(Dispatchers.IO) {
            val subject = dao.getSubjectByName(subjectName) ?: return@withContext Pair(0,0)
            val mastered = dao.getMasteredQuestionsBySubject(subject.id).size
            val total = dao.getQuestionCountBySubject(subject.id)
            Pair(mastered, total)
        }
    }
    
    suspend fun getSubjects(): List<Subject> {
        return withContext(Dispatchers.IO) {
            val masteredCounts = dao.getMasteredSubjectCounts().associate { it.subject to it.count }
            val counts = dao.getSubjectCounts() 
             
             counts.map { sc ->
                 Subject(
                     name = sc.subject, 
                     totalQuestions = sc.count,
                     masteredQuestions = masteredCounts[sc.subject] ?: 0
                 )
             }
        }
    }
    
    suspend fun resetSubject(subjectName: String) {
        withContext(Dispatchers.IO) {
            val subject = dao.getSubjectByName(subjectName)
            if (subject != null) {
                dao.resetSubjectProgress(subject.id)
            }
        }
    }
}
