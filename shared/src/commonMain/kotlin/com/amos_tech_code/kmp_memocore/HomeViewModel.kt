package com.amos_tech_code.kmp_memocore

import androidx.lifecycle.ViewModel
import com.amos_tech_code.kmp_memocore.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel: ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    fun addNote(note: Note) {
        _notes.update { it + note }
    }

}