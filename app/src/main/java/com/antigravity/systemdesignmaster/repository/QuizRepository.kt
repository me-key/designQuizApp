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

    suspend fun initializeQuestionsIfEmpty() {
        withContext(Dispatchers.IO) {
            if (dao.getQuestionCount() == 0) {
                val questions = loadQuestionsFromAssets()
                if (questions.isNotEmpty()) {
                    dao.insertQuestions(questions)
                }
            }
        }
    }

    private fun loadQuestionsFromAssets(): List<Question> {
        return try {
            val jsonString = context.assets.open("system_design_questions.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val listType = object : TypeToken<List<JsonQuestion>>() {}.type
            val jsonQuestions: List<JsonQuestion> = gson.fromJson(jsonString, listType)

            jsonQuestions.map {
                Question(
                    text = it.question,
                    option0 = it.options.getOrElse(0) { "" },
                    option1 = it.options.getOrElse(1) { "" },
                    option2 = it.options.getOrElse(2) { "" },
                    option3 = it.options.getOrElse(3) { "" },
                    correctAnswerIndex = it.correctAnswerIndex,
                    explanation = it.explanation,
                    subject = it.subject,
                    status = QuestionStatus.NEW
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getNextQuestion(subject: String): Question? {
        return withContext(Dispatchers.IO) {
            val allActive = dao.getActiveQuestionsBySubject(subject)
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
             val mastered = dao.getMasteredQuestionsBySubject(subject)
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

    suspend fun getProgress(subject: String): Pair<Int, Int> {
        return withContext(Dispatchers.IO) {
            val mastered = dao.getMasteredQuestionsBySubject(subject).size
            val total = dao.getQuestionCountBySubject(subject)
            Pair(mastered, total)
        }
    }
    
    suspend fun getSubjects(): List<Subject> {
        return withContext(Dispatchers.IO) {
            val totalCounts = dao.getSubjectCounts().associate { it.subject to it.count }
            val masteredCounts = dao.getMasteredSubjectCounts().associate { it.subject to it.count }
            
            totalCounts.map { (subject, total) ->
                Subject(
                    name = subject, 
                    totalQuestions = total,
                    masteredQuestions = masteredCounts[subject] ?: 0
                )
            }
        }
    }
    
    suspend fun resetSubject(subject: String) {
        withContext(Dispatchers.IO) {
            dao.resetSubjectProgress(subject)
        }
    }
}
