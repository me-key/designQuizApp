package com.antigravity.systemdesignmaster.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class QuestionStatus {
    NEW,
    LEARNING,
    MASTERED
}

@Entity(
    tableName = "questions",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Subject::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index("subjectId")]
)
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val option0: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val correctAnswerIndex: Int,
    val explanation: String,
    val subjectId: Long, // Foreign Key to Subject
    val status: QuestionStatus = QuestionStatus.NEW,
    val lastAnsweredTimestamp: Long = 0 
)
