package com.antigravity.systemdesignmaster.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.antigravity.systemdesignmaster.data.Subject
import com.antigravity.systemdesignmaster.ui.QuizViewModel
import com.antigravity.systemdesignmaster.ui.GenerationState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun CategorySelectionScreen(
    viewModel: QuizViewModel,
    onSubjectSelected: (String) -> Unit
) {
    val subjects by viewModel.subjects.collectAsState()
    val generationState by viewModel.generationState.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var newTopicText by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        viewModel.refreshSubjects()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("System Design Master") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
               Icon(Icons.Default.Add, contentDescription = "Add Subject")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    "Select or Add a Topic",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (subjects.isEmpty() && generationState !is GenerationState.Loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                         Text("No subjects found. Add one!")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(items = subjects) { subject ->
                            CategoryCard(
                                subject = subject, 
                                onClick = { onSubjectSelected(subject.name) },
                                onReset = { viewModel.resetSubject(subject.name) },
                                onDelete = { viewModel.deleteSubject(subject.name) }
                            )
                        }
                    }
                }
            }
            
            // Loading Overlay
            if (generationState is GenerationState.Loading) {
                 val topic = (generationState as GenerationState.Loading).topic
                 AlertDialog(
                     onDismissRequest = {},
                     title = { Text("Generating Content") },
                     text = { 
                         Column(horizontalAlignment = Alignment.CenterHorizontally) {
                             CircularProgressIndicator()
                             Spacer(modifier = Modifier.height(16.dp))
                             Text("Creating questions for $topic...")
                             Text("This may take up to 30 seconds.")
                         }
                     },
                     confirmButton = {}
                 )
            }
            
            if (generationState is GenerationState.Error) {
                val error = (generationState as GenerationState.Error).message
                 AlertDialog(
                     onDismissRequest = { /* handeled by delay in VM currently */ },
                     title = { Text("Error") },
                     text = { Text(error) },
                     confirmButton = {}
                 )
            }
        }
        
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add New Subject") },
                text = {
                    OutlinedTextField(
                        value = newTopicText,
                        onValueChange = { newTopicText = it },
                        label = { Text("Topic (e.g. Kafka)") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newTopicText.isNotBlank()) {
                                viewModel.addSubject(newTopicText)
                                newTopicText = ""
                                showAddDialog = false
                            }
                        }
                    ) {
                        Text("Generate")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun CategoryCard(
    subject: Subject,
    onClick: () -> Unit,
    onReset: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { showMenu = true }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = subject.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Progress Bar
                val progress = if (subject.totalQuestions > 0) subject.masteredQuestions.toFloat() / subject.totalQuestions else 0f
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mastered: ${subject.masteredQuestions}/${subject.totalQuestions}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            
            // Menu
             DropdownMenu(
                 expanded = showMenu,
                 onDismissRequest = { showMenu = false }
             ) {
                 DropdownMenuItem(
                     text = { Text("Reset Progress") },
                     onClick = {
                         onReset()
                         showMenu = false
                     },
                     leadingIcon = {
                          Icon(Icons.Default.Refresh, contentDescription = null)
                     }
                 )
                 DropdownMenuItem(
                     text = { Text("Delete Subject", color = Color.Red) },
                     onClick = {
                         onDelete()
                         showMenu = false
                     },
                     leadingIcon = {
                          Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                     }
                 )
             }
        }
    }
}
