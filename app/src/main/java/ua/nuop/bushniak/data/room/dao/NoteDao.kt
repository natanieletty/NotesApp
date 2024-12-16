package ua.nuop.bushniak.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ua.nuop.bushniak.entity.Note

@Dao
interface NoteDao {
    @Insert
    suspend fun insertNote(note: Note): Long

    @Query("SELECT * FROM Note WHERE isPrivate=0 AND deletedAt IS NULL")
    fun getPublicNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE id=:id")
    suspend fun getNoteById(id: Long): Note?

    @Update
    suspend fun updateNote(vararg notes: Note)

    @Query("SELECT * FROM Note WHERE deletedAt NOT NULL")
    fun getDeletedNotes():Flow<List<Note>>

    @Query("DELETE FROM Note WHERE id = :noteId")
    suspend fun deleteByNoteId(noteId: Long)

    @Query("SELECT * FROM Note WHERE isPrivate = 1 AND deletedAt IS NULL")
    fun getPrivateNotes(): Flow<List<Note>>
}