package com.amos_tech_code.kmp_memocore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: String): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NoteEntity>)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: String)

    // Soft delete
    @Query("UPDATE notes SET isDeleted = 1, isDirty = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDeleteNote(id: String, updatedAt: String)

    // Get dirty notes (notes that need syncing)
    @Query("SELECT * FROM notes WHERE isDirty = 1")
    suspend fun getDirtyNotes(): List<NoteEntity>

    // Mark notes as synced
    @Query("UPDATE notes SET isDirty = 0 WHERE id IN (:noteIds)")
    suspend fun markAsSynced(noteIds: List<String>)

    // Clear all notes (for testing)
    @Query("DELETE FROM notes")
    suspend fun clearAll()


}