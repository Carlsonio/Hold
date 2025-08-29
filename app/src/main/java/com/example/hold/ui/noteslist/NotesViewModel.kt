package com.example.hold.ui.noteslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hold.data.Note
import com.example.hold.di.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NotesState(
    val notes: List<Note> = emptyList()
)

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {
    private val _state = MutableStateFlow(NotesState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getNotes().collect { notes ->
                _state.value = NotesState(notes = notes)
            }
        }
    }
}
