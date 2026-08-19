package com.amos_tech_code.kmp_memocore.data.remote

import com.amos_tech_code.kmp_memocore.data.db.NoteDao
import com.amos_tech_code.kmp_memocore.data.db.NoteEntity
import com.amos_tech_code.kmp_memocore.data.db.SyncDataDao
import com.amos_tech_code.kmp_memocore.model.Note
import com.amos_tech_code.kmp_memocore.model.NoteChange
import com.amos_tech_code.kmp_memocore.model.SyncRequest
import com.amos_tech_code.kmp_memocore.model.SyncResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class SyncRepository (
    val userID: String,
    val apiService: ApiService,
    val noteDao: NoteDao,
    val syncDataDao: SyncDataDao
) {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: MutableStateFlow<SyncState> = _syncState

    suspend fun performSync() = withContext(Dispatchers.IO) {

        try {// Gather sync metadata
            val metadata = syncDataDao.getSyncMetadata()
            if (metadata?.isSyncing == true) {
                return@withContext
            }

            _syncState.value = SyncState.Syncing
            syncDataDao.updateSyncingStatus(true)

            // Fetch dirty notes from local database
            val dirtyNotes = noteDao.getDirtyNotes()

            // Create sync request
            val syncRequest = SyncRequest(
                since = metadata?.lastSyncTimestamp,
                changes = dirtyNotes.map {
                    NoteChange(
                        id = it.id,
                        title = it.title,
                        body = it.description,
                        isDeleted = it.isDeleted,
                        updatedAt = it.updatedAt.toString()
                    )
                }
            )

            val response = apiService.sync(syncRequest)

            // Process sync response
            response?.getOrNull()?.let {
                processSyncResponse(it)
            }

            syncDataDao.updateLastSyncTimestamp(response.getOrNull()?.nextSince ?: "")
            syncDataDao.updateSyncingStatus(false)

            _syncState.value = SyncState.Success(response.getOrNull()!!)

        } catch (ex: Exception) {
            _syncState.value = SyncState.Error(ex.message ?: "Something went wrong")
        }

    }

    suspend fun processSyncResponse(response: SyncResponse) = withContext(Dispatchers.IO) {

        //applied
        if (response.applied.isNotEmpty()) {
            noteDao.markAsSynced(response.applied)
        }

        // conflicts
        if (response.conflicts.isNotEmpty()) {
            val conflictNotes = response.conflicts.map { noteChange ->
                NoteEntity(
                    id = noteChange.id,
                    title = noteChange.title,
                    description = noteChange.body,
                    isDeleted = noteChange.isDeleted,
                    updatedAt = noteChange.updatedAt,
                    isDirty = false,
                    userId = userID
                )
            }
            noteDao.insertNotes(conflictNotes)
        }

        // changes
        if(response.changes.isNotEmpty()){
            val serverNotes = response.changes.map { noteChange ->
                NoteEntity(
                    id = noteChange.id,
                    title = noteChange.title,
                    description = noteChange.body,
                    isDeleted = noteChange.isDeleted,
                    updatedAt = noteChange.updatedAt,
                    isDirty = false,
                    userId = userID
                )
            }
            noteDao.insertNotes(serverNotes)
        }
    }

}

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    data class Success(val message: SyncResponse) : SyncState()
    data class Error(val message: String) : SyncState()
}