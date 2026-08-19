package com.amos_tech_code.kmp_memocore.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amos_tech_code.kmp_memocore.data.cache.DataStoreManager
import com.amos_tech_code.kmp_memocore.data.db.NoteDatabase
import com.amos_tech_code.kmp_memocore.data.remote.ApiService
import com.amos_tech_code.kmp_memocore.data.remote.HttpClientFactory
import com.amos_tech_code.kmp_memocore.data.remote.SyncRepository
import com.amos_tech_code.kmp_memocore.data.remote.SyncState
import com.amos_tech_code.kmp_memocore.model.Note
import com.amos_tech_code.kmp_memocore.model.toEntity
import com.amos_tech_code.kmp_memocore.model.toNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val noteDatabase: NoteDatabase,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val dao = noteDatabase.noteDao()

    val notes = dao.getAllNotes()
        .map { entities -> entities.map { it.toNote() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val userEmail = MutableStateFlow("")

    init {
        viewModelScope.launch {
            userEmail.value = dataStoreManager.getEmail() ?: ""
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note.toEntity())
            // Perform sync
            performSync()
        }
    }

    fun performSync() {
        viewModelScope.launch {
            val apiService = ApiService(HttpClientFactory.getHttpClient(), dataStoreManager)
            val userID = dataStoreManager.getUserId() ?: return@launch
            val syncRepository = SyncRepository(
                userID = userID,
                apiService = apiService,
                noteDao = dao,
                syncDataDao = noteDatabase.syncDataDao()
            )

            syncRepository.performSync()

            syncRepository.syncState.collect {
                when (it) {
                    is SyncState.Idle -> {
                    }
                    is SyncState.Syncing -> {
                    }
                    is SyncState.Success -> {

                    }
                    is SyncState.Error -> {

                    }
                }
            }
        }
    }

}