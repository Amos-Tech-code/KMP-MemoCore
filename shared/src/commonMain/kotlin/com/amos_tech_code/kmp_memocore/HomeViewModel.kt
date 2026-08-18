package com.amos_tech_code.kmp_memocore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amos_tech_code.kmp_memocore.data.db.NoteDatabase
import com.amos_tech_code.kmp_memocore.model.Note
import com.amos_tech_code.kmp_memocore.model.toEntity
import com.amos_tech_code.kmp_memocore.model.toNote
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    noteDatabase: NoteDatabase
) : ViewModel() {

    private val dao = noteDatabase.noteDao()

    val notes = dao.getAllNotes()
        .map { entities -> entities.map { it.toNote() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addNote(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note.toEntity())
        }
    }

}