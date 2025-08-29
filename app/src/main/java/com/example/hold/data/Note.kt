package com.example.hold.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_secure")
data class Note(
    @PrimaryKey(autoGenerate = true)
                val id: Int = 0,
                val title: String,
                val content: String,
                val color: Long,
                val timestamp: Long,
                val isPinned: Boolean = false
)
