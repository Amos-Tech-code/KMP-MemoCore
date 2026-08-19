package com.amos_tech_code.kmp_memocore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SyncDataDao {

    @Query("SELECT * FROM sync_metadata WHERE id = 1")
    suspend fun getSyncMetadata(): SyncMetaDataEntity?

    @Query("UPDATE sync_metadata SET lastSyncTimestamp = :timestamp WHERE id = 1")
    suspend fun updateLastSyncTimestamp(timestamp: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncMetadata(metadata: SyncMetaDataEntity)

    @Query("UPDATE sync_metadata SET isSyncing = :isSyncing WHERE id = 1")
    suspend fun updateSyncingStatus(isSyncing: Boolean)

}