package com.amos_tech_code.kmp_memocore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.amos_tech_code.kmp_memocore.data.cache.createDataStore
import com.amos_tech_code.kmp_memocore.data.cache.dataStoreFileName

fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)