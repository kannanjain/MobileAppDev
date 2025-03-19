package com.zybooks.journalapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntry)

    @Query("SELECT * FROM journal_entries WHERE date = :date")
    fun getEntryByDate(date: String): LiveData<JournalEntry?>

    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAllEntries(): LiveData<List<JournalEntry>>

    @Query("SELECT date FROM journal_entries")
    fun getAllDates(): LiveData<List<String>>

    @Update
    suspend fun updateEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteEntry(entry: JournalEntry)
}