package com.zybooks.journalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zybooks.journalapp.ui.CalendarViewModel
import com.zybooks.journalapp.ui.EntryScreen
import com.zybooks.journalapp.ui.EntryViewModel
import com.zybooks.journalapp.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val entryViewModel: EntryViewModel = viewModel()
            val calendarViewModel: CalendarViewModel = viewModel()

            // Setup navigation with shared ViewModels
            NavHost(navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(calendarViewModel, navController)
                }
                composable("entry") {
                    // When navigating to entry screen, take the date from CalendarViewModel
                    // and load the journal entry for that date
                    EntryScreen(entryViewModel, navController)

                    // Access the selected date from CalendarViewModel
                    val selectedDate = calendarViewModel.selectedDate.value
                    if (selectedDate != null) {
                        // Update EntryViewModel with the selected date
                        entryViewModel.loadEntryForDate(selectedDate)
                    }
                }
            }
        }
    }
}

