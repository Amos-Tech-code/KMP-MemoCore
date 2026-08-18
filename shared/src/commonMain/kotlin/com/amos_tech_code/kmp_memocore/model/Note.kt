package com.amos_tech_code.kmp_memocore.model

import com.amos_tech_code.kmp_memocore.data.NoteEntity

data class Note(
    val title: String,
    val description: String,
)

fun Note.toEntity() = NoteEntity(
    title = title,
    description = description,
)

fun NoteEntity.toNote() = Note(
    title = title,
    description = description
)