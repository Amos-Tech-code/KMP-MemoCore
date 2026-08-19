package com.amos_tech_code.kmp_memocore.model

import com.amos_tech_code.kmp_memocore.data.db.NoteEntity
import kotlin.time.Clock
import kotlin.uuid.Uuid

data class Note(
    val id: String = Uuid.random().toString(),
    val title: String,
    val description: String,
    val isDeleted: Boolean = false,
    val isDirty: Boolean = false,
    val updatedAt: String = Clock.System.now().toString(),
    val userId: String
)

fun Note.toEntity() = NoteEntity(
    id = id,
    title = title,
    description = description,
    isDeleted = isDeleted,
    isDirty = isDirty,
    updatedAt = updatedAt,
    userId = userId
)

fun NoteEntity.toNote() = Note(
    id = id,
    title = title,
    description = description,
    isDeleted = isDeleted,
    isDirty = isDirty,
    updatedAt = updatedAt,
    userId = userId
)