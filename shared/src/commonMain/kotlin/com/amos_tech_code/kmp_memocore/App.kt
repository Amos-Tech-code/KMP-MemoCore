package com.amos_tech_code.kmp_memocore

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amos_tech_code.kmp_memocore.data.db.NoteDatabase
import com.amos_tech_code.kmp_memocore.listItemScreen.ListNotesScreen
import com.amos_tech_code.kmp_memocore.model.Note
import com.amos_tech_code.kmp_memocore.ui.theme.QuickNotesAppTheme
import kmpmemocore.shared.generated.resources.Res
import kmpmemocore.shared.generated.resources.ic_rafiki
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    database: NoteDatabase
) {
    QuickNotesAppTheme {

        val viewModel = viewModel { HomeViewModel(database) }
        val notes by viewModel.notes.collectAsStateWithLifecycle()
        val bottomSheetState = rememberModalBottomSheetState()
        var showBottomSheet by remember { mutableStateOf(false) }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showBottomSheet = true },
                    shape = CircleShape
                ) {
                    Text(text = "+", fontSize = 18.sp)
                }
            }
        ) {
            Column(
                modifier = Modifier.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )

                if (notes.isEmpty()) {
                    EmptyView()
                } else {
                    ListNotesScreen(notes)
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = bottomSheetState
                ) {
                    // Bottom sheet content
                    AddItemDialog(
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
}

@Composable
fun AddItemDialog(
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
                        onSave(Note(
                            title = title,
                            description = description
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