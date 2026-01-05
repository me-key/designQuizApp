package com.antigravity.systemdesignmaster.data

import androidx.compose.ui.graphics.vector.ImageVector

data class Subject(
    val name: String,
    val totalQuestions: Int = 0,
    val masteredQuestions: Int = 0
)
