package com.amos_tech_code.kmp_memocore

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amos_tech_code.kmp_memocore.data.db.NoteDatabase
import com.amos_tech_code.kmp_memocore.feature.auth.SignInScreen
import com.amos_tech_code.kmp_memocore.feature.auth.SignUpScreen
import com.amos_tech_code.kmp_memocore.feature.home.HomeScreen
import com.amos_tech_code.kmp_memocore.ui.theme.QuickNotesAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    database: NoteDatabase
) {
    QuickNotesAppTheme {

        val navController = rememberNavController()

        Scaffold {
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(it)
            ) {

                composable("home") {
                    HomeScreen(database, navController)
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
}