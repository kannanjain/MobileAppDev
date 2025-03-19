package com.zybooks.journalapp.data

import androidx.lifecycle.LiveData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class JournalRepository(private val journalDao: JournalDao) {

    // Date formatter for consistent string representation
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Get all entries
    val allEntries: LiveData<List<JournalEntry>> = journalDao.getAllEntries()

    // Get all dates that have entries
    val allDates: LiveData<List<String>> = journalDao.getAllDates()

    // Get entry for specific date
    fun getEntryByDate(date: LocalDate): LiveData<JournalEntry?> {
        return journalDao.getEntryByDate(date.format(formatter))
    }

    // Insert or update an entry
    suspend fun insertEntry(
        date: LocalDate,
        entryText: String,
        mood: String,
        location: String,
        imageUri: String? = null
    ) {
        val entry = JournalEntry(
            date = date.format(formatter),
            entryText = entryText,
            mood = mood,
            location = location,
            imageUri = imageUri
        )
        journalDao.insertEntry(entry)
    }

    // Convert string date back to LocalDate
    fun stringToLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatter)
    }

    // Delete an entry
    suspend fun deleteEntry(entry: JournalEntry) {
        journalDao.deleteEntry(entry)
    }
}