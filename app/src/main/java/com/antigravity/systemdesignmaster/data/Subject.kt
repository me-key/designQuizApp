package com.antigravity.systemdesignmaster.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

import androidx.room.Ignore

@Entity(tableName = "subjects", indices = [Index(value = ["name"], unique = true)])
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val lastUpdated: Long = System.currentTimeMillis(),
    @Ignore val totalQuestions: Int = 0,
    @Ignore val masteredQuestions: Int = 0
) {
    // Room Constructor
    constructor(id: Long, name: String, lastUpdated: Long) : this(id, name, lastUpdated, 0, 0)
}
