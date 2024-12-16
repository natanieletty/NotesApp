package ua.nuop.bushniak.data.firestore

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import org.json.JSONObject
import ua.nuop.bushniak.entity.Note
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirestoreClient @Inject constructor(firebaseAuth: FirebaseAuth) {
    private val firestore = Firebase.firestore
    private val _client = MutableStateFlow(Firebase.auth.currentUser)
    private var client = _client.asStateFlow()


    init {
        firebaseAuth.addAuthStateListener {
            _client.value = it.currentUser
        }
    }

    suspend fun getAllNotes(): List<Note> {

        if (client.value != null) {
            val coll = firestore.collection(client.value!!.uid).get().await()
            val remoteNotes: List<Note?> = coll.documents.map {
                if (it.data != null) {
                    val json = JSONObject(it.data!!)
                    Log.d("firestore", json.toString())

                    return@map Json.decodeFromString<Note>(json.toString())
                } else {
                    return@map null
                }
            }
            return remoteNotes.filterNotNull()

        }
        return emptyList()
    }

    suspend fun addRecord(note: Note) {
        Log.d("firestore", client.value?.uid.toString())
        if (client.value != null) {
            try {
                firestore.collection(client.value!!.uid).document(note.id.toString()).set(note)
                    .addOnSuccessListener {
                        Log.d("firestore", "good")
                    }.addOnCanceledListener {
                        Log.d("firestore", "canceled")

                    }.addOnFailureListener { it ->
                        Log.d("firestore", it.message.toString())

                    }
                    .await()
            } catch (e: NullPointerException) {

            }

        }
    }


    suspend fun deleteNote(id: Long) {
        if (client.value != null) {
            firestore.collection(client.value!!.uid).document(id.toString()).delete().await()
        }
    }
}