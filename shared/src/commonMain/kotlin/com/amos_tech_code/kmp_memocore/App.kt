package com.amos_tech_code.kmp_memocore

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amos_tech_code.kmp_memocore.data.cache.DataStoreManager
import com.amos_tech_code.kmp_memocore.data.db.NoteDatabase
import com.amos_tech_code.kmp_memocore.feature.auth.SignInScreen
import com.amos_tech_code.kmp_memocore.feature.auth.SignUpScreen
import com.amos_tech_code.kmp_memocore.feature.home.HomeScreen
import com.amos_tech_code.kmp_memocore.feature.profile.UserProfileScreen
import com.amos_tech_code.kmp_memocore.ui.theme.QuickNotesAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    database: NoteDatabase,
    dataStoreManager: DataStoreManager
) {
    QuickNotesAppTheme {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "home",
        ) {

            composable("home") {
                HomeScreen(database, dataStoreManager, navController)
            }

            composable("signup") {
                SignUpScreen(navController, dataStoreManager)
            }

            composable("signin") {
                SignInScreen(navController, dataStoreManager)
            }

            composable("profile") {
                UserProfileScreen(dataStoreManager)
            }

        }


    }
}