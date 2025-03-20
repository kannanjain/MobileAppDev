package com.zybooks.journalapp.ui

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import java.util.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import java.time.LocalDate



class EntryViewModel(application: Application) : AndroidViewModel(application) {
    private val _entryText = MutableLiveData("Today I was feeling ...")
    val entryText: LiveData<String> = _entryText

    private val _mood = MutableLiveData("Happy 🙂")
    val mood: LiveData<String> = _mood

    private val _location = MutableLiveData("Tap to add location 📍")
    val location: LiveData<String> = _location

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    private val _savedDates = MutableLiveData<List<LocalDate>>()
    val savedDates: LiveData<List<LocalDate>> = _savedDates

    fun updateEntry(text: String) {
        _entryText.value = text
    }

    fun updateMood(newMood: String) {
        _mood.value = newMood
    }

    fun updateLocation(newLocation: String) {
        _location.value = newLocation
    }

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)


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
                } else {
                    _location.value = "Location not found 📍"
                }
            } else {
                _location.value = "Unable to fetch location 📍"
            }
        }.addOnFailureListener { exception ->
            // Handle possible failures (e.g., no GPS, no location available)
            _location.value = "Location fetch failed 📍"
        }
    }

    fun captureImage(uri: Uri) {
        _imageUri.value = uri
    }

    fun selectMood() {
        val moodOptions = listOf("Happy 🙂", "Sad 😢", "Excited 🤩", "Tired 😴", "Calm 😌")
        _mood.value = moodOptions.random()
    }
}

