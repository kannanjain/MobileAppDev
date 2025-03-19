package com.zybooks.journalapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey
    val date: String, // Store LocalDate as String in format "yyyy-MM-dd"
    val entryText: String,
    val mood: String,
    val location: String,
    val imageUri: String? = null // Store image URI as string if needed later
)