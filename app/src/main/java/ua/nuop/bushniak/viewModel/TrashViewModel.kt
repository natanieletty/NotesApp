package ua.nuop.bushniak.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import ua.nuop.bushniak.entity.Note
import ua.nuop.bushniak.repo.NoteRepo
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor(private val noteRepo: NoteRepo) : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    init {
        viewModelScope.launch {
            noteRepo.getDeletedNotes().collect {
//                Log.d("trashVM", it.toString())
                _notes.value = it
            }
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            noteRepo.deleteNote(id)
        }
    }

    fun restoreNote(id: Long){
        viewModelScope.launch {
            noteRepo.restoreNote(id)
        }
    }
}