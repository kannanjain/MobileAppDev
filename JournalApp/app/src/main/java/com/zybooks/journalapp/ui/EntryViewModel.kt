package com.zybooks.journalapp.ui
import android.location.Location
import java.util.*

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EntryViewModel : ViewModel() {
    private val _entryText = MutableLiveData("Today I was feeling ...")
    val entryText: LiveData<String> = _entryText

    private val _mood = MutableLiveData("Happy 🙂")
    val mood: LiveData<String> = _mood

    private val _location = MutableLiveData("123 Ave 📍")
    val location: LiveData<String> = _location

    fun updateEntry(text: String) {
        _entryText.value = text
    }

    fun updateMood(newMood: String) {
        _mood.value = newMood
    }

    fun updateLocation(newLocation: String) {
        _location.value = newLocation
    }
}

