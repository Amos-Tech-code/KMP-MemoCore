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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amos_tech_code.kmp_memocore.data.db.NoteDatabase
import com.amos_tech_code.kmp_memocore.feature.auth.SignInScreen
import com.amos_tech_code.kmp_memocore.feature.auth.SignUpScreen
import com.amos_tech_code.kmp_memocore.feature.home.HomeScreen
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

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "signup"
        ) {

            composable("home") {
                HomeScreen(database)
            }

            composable("signup") {
                SignUpScreen(navController)
            }

            composable("signin") {
                SignInScreen(navController)
            }

        }

    }
}