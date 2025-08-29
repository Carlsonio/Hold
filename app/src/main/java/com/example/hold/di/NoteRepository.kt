package com.example.hold.di

import com.example.hold.data.Note
import com.example.hold.data.NoteDao
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun getNoteById(id: Int): Note? // Добавить в интерфейс

}

class NoteRepositoryImpl @Inject constructor(private val dao: NoteDao) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> = dao.getNotes()
    override suspend fun insertNote(note: Note) = dao.insertNote(note)
    override suspend fun deleteNote(note: Note) = dao.deleteNote(note)
    override suspend fun getNoteById(id: Int): Note? = dao.getNoteById(id)
}
