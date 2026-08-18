package com.amos_tech_code.kmp_memocore

import androidx.compose.ui.window.ComposeUIViewController

import com.amos_tech_code.kmp_memocore.data.db.getNoteDatabase

fun MainViewController() = ComposeUIViewController { 
    App(
        database = getNoteDatabase(getDatabaseBuilder())
    )
}