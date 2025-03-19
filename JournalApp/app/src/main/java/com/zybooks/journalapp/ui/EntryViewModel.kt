package com.zybooks.journalapp.ui

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.google.android.gms.location.LocationServices
import com.zybooks.journalapp.data.JournalDatabase
import com.zybooks.journalapp.data.JournalEntry
import com.zybooks.journalapp.data.JournalRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class EntryViewModel(application: Application) : AndroidViewModel(application) {

    // Database setup
    private val repository: JournalRepository
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Current date being edited
    private val _currentDate = MutableLiveData(LocalDate.now())
    val currentDate: LiveData<LocalDate> = _currentDate

    // Entry data
    private val _entryText = MutableLiveData<String>()
    val entryText: LiveData<String> = _entryText

    private val _mood = MutableLiveData<String>()
    val mood: LiveData<String> = _mood

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> = _location

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    // List of dates with saved entries
    private val _savedDates = MutableLiveData<List<LocalDate>>(emptyList())
    val savedDates: LiveData<List<LocalDate>> = _savedDates

    // Current entry from the database (if it exists)
    private var currentEntry: LiveData<JournalEntry?> = MutableLiveData(null)

    // Location services
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    init {
        val journalDao = JournalDatabase.getDatabase(application).journalDao()
        repository = JournalRepository(journalDao)

        // Observe dates from repository and convert them to LocalDate objects
        repository.allDates.observeForever { stringDates ->
            _savedDates.value = stringDates.map { repository.stringToLocalDate(it) }
        }

        // Load today's entry if it exists
        loadEntryForDate(LocalDate.now())
    }

    fun loadEntryForDate(date: LocalDate) {
        _currentDate.value = date
        currentEntry = repository.getEntryByDate(date)

        // Observe the entry and update UI values when it changes
        currentEntry.observeForever { entry ->
            if (entry != null) {
                _entryText.value = entry.entryText
                _mood.value = entry.mood
                _location.value = entry.location
                _imageUri.value = entry.imageUri?.let { Uri.parse(it) }
            } else {
                // No entry for this date, set defaults
                _entryText.value = "Today I was feeling ..."
                _mood.value = "Happy 🙂"
                _location.value = "Tap to add location 📍"
                _imageUri.value = null
            }
        }
    }

    fun saveCurrentEntry() {
        viewModelScope.launch {
            repository.insertEntry(
                date = _currentDate.value ?: LocalDate.now(),
                entryText = _entryText.value ?: "Today I was feeling ...",
                mood = _mood.value ?: "Happy 🙂",
                location = _location.value ?: "Tap to add location 📍",
                imageUri = _imageUri.value?.toString()
            )
        }
    }

    fun updateEntry(text: String) {
        _entryText.value = text
        // Auto-save when text changes
        saveCurrentEntry()
    }

    fun updateMood(newMood: String) {
        _mood.value = newMood
        saveCurrentEntry()
    }

    fun updateLocation(newLocation: String) {
        _location.value = newLocation
        saveCurrentEntry()
    }

    fun fetchLocation() {
        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            _location.value = "Location permission not granted 📍"
            return
        }

        // If permission is granted, fetch the location
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // Use the latitude and longitude from the location object
                val latitude = location.latitude
                val longitude = location.longitude

                // Use Geocoder to fetch the location address
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                if (addresses != null && addresses.isNotEmpty()) {
                    _location.value = addresses[0].getAddressLine(0) + " 📍"
                    saveCurrentEntry()
                } else {
                    _location.value = "Location not found 📍"
                }
            } else {
                _location.value = "Unable to fetch location 📍"
            }
        }.addOnFailureListener { exception ->
            _location.value = "Location fetch failed 📍"
        }
    }

    fun captureImage(uri: Uri) {
        _imageUri.value = uri
        saveCurrentEntry()
    }

    fun selectMood() {
        val moodOptions = listOf("Happy 🙂", "Sad 😢", "Excited 🤩", "Tired 😴", "Calm 😌")
        _mood.value = moodOptions.random()
        saveCurrentEntry()
    }

    override fun onCleared() {
        super.onCleared()
        // Remove all observers when ViewModel is cleared
        currentEntry.removeObserver { }
        // Also remove the allDates observer
        repository.allDates.removeObserver { }
    }
}