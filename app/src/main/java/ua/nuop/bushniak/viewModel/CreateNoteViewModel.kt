package ua.nuop.bushniak.viewModel

import android.util.Log
import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamedrejeb.richeditor.model.RichTextState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nuop.bushniak.entity.Note
import ua.nuop.bushniak.repo.NoteRepo
import javax.inject.Inject

@HiltViewModel(assistedFactory = CreateNoteViewModel.Factory::class)
class CreateNoteViewModel @AssistedInject constructor(
    private val noteRepo: NoteRepo,
    @Assisted private val noteId: Long?
) : ViewModel() {

    private val _isPrivate = MutableStateFlow(false)
    val isPrivate = _isPrivate.asStateFlow()

    @AssistedFactory
    interface Factory {
        fun create(recordId: Long?): CreateNoteViewModel
    }


    val richTextState = RichTextState()

    init {
        if (noteId != null) {
            viewModelScope.launch {
                val note = noteRepo.getNoteById(noteId)
                Log.d("createVM", note.toString())
                if (note != null) {
                    richTextState.setHtml(note.text)
                    _isPrivate.update {
                        note.isPrivate
                    }
                }
            }

        }
    }

    fun updateIsPrivate(value: Boolean) {
        _isPrivate.value = value
    }

    fun addNote(text: String) {
        if (noteId != null) {
            viewModelScope.launch {
                noteRepo.editNote(note = Note(id = noteId, text = text, isPrivate = isPrivate.value))
            }
        } else {

            viewModelScope.launch {
                if (text.parseAsHtml().toString() != "\n")
                    noteRepo.insertNote(note = Note(text = text, isPrivate = isPrivate.value))
            }
        }
    }
}