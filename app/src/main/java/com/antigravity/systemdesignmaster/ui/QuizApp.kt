package com.antigravity.systemdesignmaster.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.antigravity.systemdesignmaster.repository.QuizRepository
import com.antigravity.systemdesignmaster.ui.screens.CategorySelectionScreen
import com.antigravity.systemdesignmaster.ui.screens.QuizScreen

enum class Screen {
    CATEGORY_SELECTION,
    QUIZ
}

@Composable
fun QuizApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val repository = QuizRepository(context)
    val viewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(repository)
    )
    val state by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = Screen.CATEGORY_SELECTION.name) {
        composable(Screen.CATEGORY_SELECTION.name) {
            CategorySelectionScreen(
                viewModel = viewModel,
                onSubjectSelected = { subject ->
                    viewModel.setSubject(subject)
                    navController.navigate(Screen.QUIZ.name)
                }
            )
        }
        composable(Screen.QUIZ.name) {
            QuizScreen(
                state = state,
                onOptionSelected = { index ->
                    viewModel.submitAnswer(index)
                },
                onNextQuestion = {
                    viewModel.loadNextQuestion()
                },
                onBack = {
                    viewModel.refreshSubjects() // Refresh counts when coming back
                    navController.popBackStack()
                }
            )
        }
    }
}
