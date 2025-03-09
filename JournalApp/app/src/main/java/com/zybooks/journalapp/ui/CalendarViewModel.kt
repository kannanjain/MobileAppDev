package com.zybooks.journalapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate


class CalendarViewModel : ViewModel() {
    private val _selectedDate = MutableLiveData(LocalDate.now())
    val selectedDate: LiveData<LocalDate> = _selectedDate

    fun updateDate(newDate: LocalDate) {
        _selectedDate.value = newDate
    }
    fun changeMonth(monthOffset: Int) {
        val currentDate = _selectedDate.value ?: LocalDate.now()
        // Adjust the month by the offset (positive or negative)
        val newDate = currentDate.plusMonths(monthOffset.toLong())
        updateDate(newDate)  // Use the updateDate method to update the selectedDate
    }

}