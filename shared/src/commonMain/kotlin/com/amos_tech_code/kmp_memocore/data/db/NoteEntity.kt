package com.amos_tech_code.kmp_memocore.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.uuid.Uuid

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val isDeleted: Boolean,
    val isDirty: Boolean, // Track if note needs to be synced
    val updatedAt: String,
    val userId: String
)