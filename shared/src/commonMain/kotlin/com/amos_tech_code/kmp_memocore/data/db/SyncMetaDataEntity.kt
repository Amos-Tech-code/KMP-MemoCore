package com.amos_tech_code.kmp_memocore.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_metadata")
data class SyncMetaDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lastSyncTimestamp: String? = null,
    val isSyncing: Boolean = false
)
