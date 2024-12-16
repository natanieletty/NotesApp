package ua.nuop.bushniak.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.nuop.bushniak.data.room.dao.NoteDao
import ua.nuop.bushniak.entity.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}