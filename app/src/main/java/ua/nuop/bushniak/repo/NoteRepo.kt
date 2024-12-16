package ua.nuop.bushniak.repo

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.nuop.bushniak.data.firestore.FirestoreClient
import ua.nuop.bushniak.data.room.AppDB
import ua.nuop.bushniak.entity.Note
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NoteRepo @Inject constructor(db: AppDB, private val firestoreClient: FirestoreClient) {
    private val noteDao = db.noteDao()

    suspend fun insertNote(note: Note) {
        val insertedNoteId = noteDao.insertNote(note)
        if (!note.isPrivate) {
            firestoreClient.addRecord(note.copy(id = insertedNoteId))
        }
    }


    suspend fun getNoteById(id: Long): Note? {
        return noteDao.getNoteById(id)
    }

    suspend fun getPublicNotes(): Flow<List<Note>> {
        val notes = noteDao.getPublicNotes()
//        firestoreClient.getAllNotes()
        withContext(Dispatchers.IO) {
            launch {
                try {
                    synchronizeData()
                } catch (_: Exception) {

                }


            }

        }
        Log.d("noteRepo", "returningNotes")

        return notes
    }

    suspend fun synchronizeData() {
        val localNotes = noteDao.getPublicNotes().first()

        val remoteNotes = firestoreClient.getAllNotes()

        val filteredNotes =
            ((localNotes - remoteNotes.toSet()) union (remoteNotes - localNotes.toSet())).toMutableList()


        val duplicateNoteMap = emptyMap<Long?, Int>().toMutableMap()



        filteredNotes.forEach {
            duplicateNoteMap[it.id] = (duplicateNoteMap[it.id] ?: 0) + 1
        }



        duplicateNoteMap.filter { it.value > 1 }.forEach { (id, _) ->

            val duplicateConflictNote = filteredNotes.filter {
                it.id == id
            }

            val insertedNote = duplicateConflictNote.maxByOrNull { it.updatedAt }!!
            Log.d("noteRepo", duplicateConflictNote.toString())
            Log.d("noteRepo", insertedNote.toString())

            filteredNotes.removeIf {
                it.id == insertedNote.id && it.updatedAt != insertedNote.updatedAt
            }

        }

        Log.d("noteRepo", filteredNotes.toString())


        filteredNotes.forEach { conflictNote ->
            if (conflictNote in remoteNotes) {
                if (noteDao.getNoteById(conflictNote.id ?: 0) != null) {
                    noteDao.updateNote(conflictNote)
                } else {
                    noteDao.insertNote(conflictNote)
                }
            } else if (conflictNote in localNotes) {
                if (!conflictNote.isPrivate && conflictNote.deletedAt != null) {
                    firestoreClient.addRecord(conflictNote)
                }
            }
        }

    }

    suspend fun softDeleteNote(id: Long) {
        val note = getNoteById(id)
        if (note != null) {
            noteDao.updateNote(note.copy(deletedAt = System.currentTimeMillis() / 1000))
        }
        firestoreClient.deleteNote(id)
    }

    fun getDeletedNotes(): Flow<List<Note>> {
        val notes = noteDao.getDeletedNotes()
        return notes
    }

    suspend fun toggleVisibility(id: Long) {
        val note = getNoteById(id)
        if (note != null) {
            val updatedNote = note.copy(
                isPrivate = !note.isPrivate,
                updatedAt = System.currentTimeMillis() / 1000
            )
            noteDao.updateNote(updatedNote)
            if (!updatedNote.isPrivate) {
                firestoreClient.addRecord(updatedNote)
            } else {
                firestoreClient.deleteNote(updatedNote.id!!)
            }
        }
    }

    suspend fun deleteNote(id: Long) {
        noteDao.deleteByNoteId(id)
        firestoreClient.deleteNote(id)
    }

    suspend fun restoreNote(id: Long) {
        var note = getNoteById(id)
        if (note != null) {
            note = note.copy(deletedAt = null, updatedAt = System.currentTimeMillis() / 1000)
            noteDao.updateNote(note)
            firestoreClient.addRecord(note)
        }
    }

    suspend fun editNote(note: Note) {
        noteDao.updateNote(note.copy(updatedAt = System.currentTimeMillis() / 1000))
        if (!note.isPrivate) {
            firestoreClient.addRecord(note.copy(updatedAt = System.currentTimeMillis() / 1000))

        }
    }

    fun getPrivateNotes(): Flow<List<Note>> {
        val notes = noteDao.getPrivateNotes()
        return notes
    }


}