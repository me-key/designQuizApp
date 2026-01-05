package com.antigravity.systemdesignmaster.ui.screens

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.systemdesignmaster.data.Question
import com.antigravity.systemdesignmaster.ui.QuizUiState
import com.antigravity.systemdesignmaster.ui.theme.CorrectGreen
import com.antigravity.systemdesignmaster.ui.theme.CorrectGreenBg
import com.antigravity.systemdesignmaster.ui.theme.ElectricBlue
import com.antigravity.systemdesignmaster.ui.theme.ErrorRed
import com.antigravity.systemdesignmaster.ui.theme.ErrorRedBg
import com.antigravity.systemdesignmaster.ui.theme.SlateLight

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuizScreen(
    state: QuizUiState,
    onOptionSelected: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onBack: () -> Unit
) {
    val view = LocalView.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Streak: ${state.stats.third} 🔥",
                color = Color(0xFFFFA000),
                fontWeight = FontWeight.Bold
            )
        }

        // Progress Bar
        val progress = if (state.progress.second > 0) state.progress.first.toFloat() / state.progress.second.toFloat() else 0f
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Mastery Progress",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "${state.progress.first}/${state.progress.second}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = ElectricBlue,
                trackColor = SlateLight
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Question Content with Animation
        Box(modifier = Modifier.weight(1f)) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ElectricBlue)
                }
            } else if (state.quizComplete) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Text(
                            text = "All Questions Mastered!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = CorrectGreen,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "You have cleared the deck.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(onClick = onBack) {
                            Text("Back to Dashboard")
                        }
                    }
                }
            } else {
                state.currentQuestion?.let { question ->
                    AnimatedContent(
                        targetState = question,
                        transitionSpec = {
                            slideInHorizontally(initialOffsetX = { it }) + fadeIn() with
                            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                        },
                        label = "Question Animation"
                    ) { currentQ ->
                        QuestionContent(
                            question = currentQ,
                            selectedOptionIndex = state.selectedOptionIndex,
                            isAnswered = state.isAnswered,
                            isCorrect = state.isCorrect,
                            onOptionSelected = {
                                if (!state.isAnswered) {
                                    onOptionSelected(it)
                                    // Haptic feedback logic inside Composable due to state awareness
                                    if (currentQ.correctAnswerIndex != it) {
                                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                                    }
                                }
                            },
                            onNext = onNextQuestion
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionContent(
    question: Question,
    selectedOptionIndex: Int?,
    isAnswered: Boolean,
    isCorrect: Boolean,
    onOptionSelected: (Int) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = question.text,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(32.dp))

        val options = listOf(question.option0, question.option1, question.option2, question.option3)

        options.forEachIndexed { index, option ->
            val isSelected = selectedOptionIndex == index
            val isTargetCorrect = question.correctAnswerIndex == index
            
            // Determine colors
            // If NOT answered: Default
            // If Answered:
            //   - Ideally correct one is GREEN
            //   - Selected wrong one is RED
            //   - Others dimmed
            
            var borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            var backgroundColor = MaterialTheme.colorScheme.surface
            var textColor = MaterialTheme.colorScheme.onSurface

            if (isAnswered) {
                if (isTargetCorrect) {
                     borderColor = CorrectGreen
                     backgroundColor = CorrectGreenBg.copy(alpha = 0.1f)
                     textColor = CorrectGreen
                } else if (isSelected && !isCorrect) {
                     borderColor = ErrorRed
                     backgroundColor = ErrorRedBg.copy(alpha = 0.1f)
                     textColor = ErrorRed
                } else {
                     textColor = textColor.copy(alpha = 0.5f)
                }
            } else if (isSelected) {
                borderColor = ElectricBlue
                textColor = ElectricBlue
            }

            OptionCard(
                text = option,
                borderColor = borderColor,
                backgroundColor = backgroundColor,
                textColor = textColor,
                onClick = { onOptionSelected(index) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Feedback Card
        AnimatedVisibility(visible = isAnswered) {
            Column(modifier = Modifier.padding(top = 24.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isCorrect) CorrectGreenBg.copy(alpha = 0.2f) else ErrorRedBg.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isCorrect) CorrectGreen else ErrorRed,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = if (isCorrect) "Correct! 🎉" else "Incorrect ❌",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isCorrect) CorrectGreen else ErrorRed
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Next Question", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun OptionCard(
    text: String,
    borderColor: Color,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
