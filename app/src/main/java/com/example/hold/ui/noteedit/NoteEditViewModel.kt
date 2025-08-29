package com.example.hold.ui.noteedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hold.data.Note
import com.example.hold.di.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoteEditState(
    val title: String = "",
    val content: String = "",
    val color: Long = 0xFFFFFFFF,
    val isNoteLoaded: Boolean = false,
    val isNewNote: Boolean = true
)

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(NoteEditState())
    val state = _state.asStateFlow()

    private val noteId: Int = checkNotNull(savedStateHandle["noteId"])

    init {
        if (noteId != -1) {
            viewModelScope.launch {
                repository.getNoteById(noteId)?.let { note ->
                    _state.value = NoteEditState(
                        title = note.title,
                        content = note.content,
                        color = note.color,
                        isNoteLoaded = true,
                        isNewNote = false
                    )
                }
            }
        } else {
            _state.value = NoteEditState(isNoteLoaded = true, isNewNote = true)
        }
    }

    fun onTitleChange(newTitle: String) {
        _state.value = _state.value.copy(title = newTitle)
    }

    fun onContentChange(newContent: String) {
        _state.value = _state.value.copy(content = newContent)
    }

    fun saveNote(onNoteSaved: () -> Unit) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState.title.isNotBlank() || currentState.content.isNotBlank()) {
                val note = Note(
                    id = if (noteId == -1) 0 else noteId,
                    title = currentState.title,
                    content = currentState.content,
                    color = currentState.color,
                    timestamp = System.currentTimeMillis()
                )
                repository.insertNote(note)
            }
            onNoteSaved()
        }
    }
}