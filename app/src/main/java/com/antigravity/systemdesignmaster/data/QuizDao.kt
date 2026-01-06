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

    // New Subject Methods
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubject(subject: Subject): Long

    @Query("SELECT * FROM subjects WHERE name = :name LIMIT 1")
    suspend fun getSubjectByName(name: String): Subject?

    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): Flow<List<Subject>>
    
    @Query("DELETE FROM subjects WHERE id = :subjectId")
    suspend fun deleteSubject(subjectId: Long)

    // Updated Question Queries using subjectId
    @Query("SELECT * FROM questions WHERE (status = 'NEW' OR status = 'LEARNING') AND subjectId = :subjectId")
    suspend fun getActiveQuestionsBySubject(subjectId: Long): List<Question>

    @Query("SELECT * FROM questions WHERE status = 'MASTERED' AND subjectId = :subjectId")
    suspend fun getMasteredQuestionsBySubject(subjectId: Long): List<Question>

    @Query("SELECT COUNT(*) FROM questions WHERE subjectId = :subjectId")
    suspend fun getQuestionCountBySubject(subjectId: Long): Int
    
    // Statistics (Joining with Subject table if needed, or just using ID)
    @Query("SELECT s.name as subject, COUNT(q.id) as count FROM subjects s LEFT JOIN questions q ON s.id = q.subjectId GROUP BY s.id")
    suspend fun getSubjectCounts(): List<SubjectCount>

    @Query("SELECT s.name as subject, COUNT(q.id) as count FROM subjects s JOIN questions q ON s.id = q.subjectId WHERE q.status = 'MASTERED' GROUP BY s.id")
    suspend fun getMasteredSubjectCounts(): List<SubjectCount>

    @Query("UPDATE questions SET status = 'NEW', lastAnsweredTimestamp = 0 WHERE subjectId = :subjectId")
    suspend fun resetSubjectProgress(subjectId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Update
    suspend fun updateQuestion(question: Question)
    
    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int
}
