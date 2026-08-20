package com.amos_tech_code.kmp_memocore.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amos_tech_code.kmp_memocore.data.cache.DataStoreManager
import com.amos_tech_code.kmp_memocore.data.db.NoteDatabase
import com.amos_tech_code.kmp_memocore.feature.listItemScreen.ListNotesScreen
import com.amos_tech_code.kmp_memocore.model.Note
import kmpmemocore.shared.generated.resources.Res
import kmpmemocore.shared.generated.resources.ic_rafiki
import kmpmemocore.shared.generated.resources.ic_settings
import kmpmemocore.shared.generated.resources.ic_sync
import kmpmemocore.shared.generated.resources.ic_user
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    database: NoteDatabase,
    dataStoreManager: DataStoreManager,
    navController: NavController
) {

    val viewModel = viewModel {
        HomeViewModel(noteDatabase = database, dataStoreManager = dataStoreManager)
    }
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val email = remember { mutableStateOf("") }
    val userId = remember { mutableStateOf("") }

    LaunchedEffect(true) {
        email.value = dataStoreManager.getEmail() ?: ""
        userId.value = dataStoreManager.getUserId() ?: ""
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                shape = CircleShape
            ) {
                Text(text = "+", fontSize = 18.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_user),
                        null,
                        modifier = Modifier
                            .padding(end = 16.dp).size(48.dp).padding(4.dp)
                            .clip(CircleShape)
                            .clickable {
                                coroutineScope.launch {
                                    if (dataStoreManager.getToken() != null) {
                                        navController.navigate("profile")
                                    } else {
                                        navController.navigate("signup")
                                    }
                                }
                            }
                    )
                    Image(
                        painterResource(Res.drawable.ic_settings),
                        null,
                        modifier = Modifier.padding(end = 16.dp).size(48.dp).padding(4.dp)
                            .clickable {
                                navController.navigate("settings")

                            },
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )

                    if(email.value.isNotEmpty()) {
                        Image(
                            painterResource(Res.drawable.ic_sync),
                            null,
                            modifier = Modifier
                                .padding(end = 16.dp).size(48.dp).padding(4.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        viewModel.performSync()
                                    }
                                }
                        )
                    }
                }

            }

            if (notes.isEmpty()) {
                EmptyView()
            } else {
                ListNotesScreen(
                    list = notes,
                    onDelete = {
                        viewModel.deleteNote(it)
                    }
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState
            ) {
                // Bottom sheet content
                AddItemDialog(
                    userId = userId.value,
                    onCancel = { showBottomSheet = false },
                    onSave = {
                        viewModel.addNote(it)
                        showBottomSheet = false
                    }
                )
            }
        }

    }
}


@Composable
fun AddItemDialog(
    userId: String,
    onCancel: () -> Unit,
    onSave: (Note) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        val colour = TextFieldDefaults.colors(
            focusedContainerColor = Transparent,
            unfocusedContainerColor = Transparent,
        )

        TextField(
            value = title,
            onValueChange = { title = it },
            colors = colour,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Title", fontSize = 22.sp)
            },
            textStyle = TextStyle(fontSize = 22.sp)
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            colors = colour,
            placeholder = {
                Text(text = "Say something")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5
        )

        Row(modifier = Modifier.align(Alignment.End)) {

            Text(
                text = "Cancel",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onCancel() }
            )
            Text(
                text = "Save",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onSave(
                            Note(
                            title = title,
                            description = description,
                            userId = userId,
                            isDirty = true
                        ))
                    }
            )
        }
    }

}

@Composable
fun EmptyView() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Image(
                painter = painterResource(Res.drawable.ic_rafiki),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "Create your first note !",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 16.sp
            )
        }
    }
}