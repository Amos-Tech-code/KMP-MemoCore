package com.amos_tech_code.kmp_memocore

import androidx.compose.ui.window.ComposeUIViewController
import com.amos_tech_code.kmp_memocore.data.cache.DataStoreManager
import com.amos_tech_code.kmp_memocore.data.db.getNoteDatabase
import com.amos_tech_code.kmp_memocore.data.remote.createDataStore

fun MainViewController() = ComposeUIViewController { 
    App(
        database = getNoteDatabase(getDatabaseBuilder()),
        dataStoreManager = DataStoreManager(createDataStore())
    )
}