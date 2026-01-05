package com.antigravity.systemdesignmaster.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

data class SubjectCount(
    val subject: String,
    val count: Int
)

@Dao
interface QuizDao {
    @Query("SELECT * FROM questions")
    fun getAllQuestions(): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE status = 'NEW' OR status = 'LEARNING'")
    suspend fun getActiveQuestions(): List<Question>

    @Query("SELECT * FROM questions WHERE status = 'MASTERED'")
    suspend fun getMasteredQuestions(): List<Question>

    // Subject specific queries
    @Query("SELECT * FROM questions WHERE (status = 'NEW' OR status = 'LEARNING') AND subject = :subject")
    suspend fun getActiveQuestionsBySubject(subject: String): List<Question>

    @Query("SELECT * FROM questions WHERE status = 'MASTERED' AND subject = :subject")
    suspend fun getMasteredQuestionsBySubject(subject: String): List<Question>

    @Query("SELECT COUNT(*) FROM questions WHERE subject = :subject")
    suspend fun getQuestionCountBySubject(subject: String): Int
    
    @Query("SELECT subject, COUNT(*) as count FROM questions GROUP BY subject")
    suspend fun getSubjectCounts(): List<SubjectCount>

    @Query("SELECT subject, COUNT(*) as count FROM questions WHERE status = 'MASTERED' GROUP BY subject")
    suspend fun getMasteredSubjectCounts(): List<SubjectCount>

    @Query("UPDATE questions SET status = 'NEW', lastAnsweredTimestamp = 0 WHERE subject = :subject")
    suspend fun resetSubjectProgress(subject: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Update
    suspend fun updateQuestion(question: Question)
    
    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int
}
