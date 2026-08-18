package com.amos_tech_code.kmp_memocore

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amos_tech_code.kmp_memocore.data.db.NoteDatabase

fun getDatabaseBuilder(
    context: Context,
): RoomDatabase.Builder<NoteDatabase> {

    val appContext = context.applicationContext
    val dbPath = appContext.getDatabasePath("notes_database.db")

    return Room.databaseBuilder(context, NoteDatabase::class.java, dbPath.path)

}