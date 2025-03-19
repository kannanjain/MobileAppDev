package com.zybooks.journalapp.ui

import android.app.Application
import androidx.lifecycle.*
import com.zybooks.journalapp.data.JournalDatabase
import com.zybooks.journalapp.data.JournalRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    // Repository setup
    private val repository: JournalRepository

    // Currently selected date on the calendar
    private val _selectedDate = MutableLiveData(LocalDate.now())
    val selectedDate: LiveData<LocalDate> = _selectedDate

    // List of dates that have journal entries
    private val _datesWithEntries = MutableLiveData<List<LocalDate>>(emptyList())
    val datesWithEntries: LiveData<List<LocalDate>> = _datesWithEntries

    init {
        val journalDao = JournalDatabase.getDatabase(application).journalDao()
        repository = JournalRepository(journalDao)

        // Observe string dates and convert to LocalDate objects
        repository.allDates.observeForever { stringDates ->
            _datesWithEntries.value = stringDates.map { repository.stringToLocalDate(it) }
        }
    }

    fun updateDate(newDate: LocalDate) {
        _selectedDate.value = newDate
    }

    fun changeMonth(monthOffset: Int) {
        val currentDate = _selectedDate.value ?: LocalDate.now()
        // Adjust the month by the offset (positive or negative)
        val newDate = currentDate.plusMonths(monthOffset.toLong())
        updateDate(newDate)
    }

    // Check if a date has an entry
    fun hasEntry(date: LocalDate): Boolean {
        return datesWithEntries.value?.contains(date) ?: false
    }
}