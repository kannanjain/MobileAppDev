package com.zybooks.journalapp

import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel


import com.zybooks.journalapp.ui.EntryViewModel

import com.zybooks.journalapp.ui.EntryScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zybooks.journalapp.ui.CalendarViewModel
import com.zybooks.journalapp.ui.HomeScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val entryViewModel: EntryViewModel = viewModel()
            val calendarViewModel: CalendarViewModel = viewModel()

            NavHost(navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(calendarViewModel, navController)
                }
                composable("entry") {
                    EntryScreen(entryViewModel, navController) // Pass the navController
                }
            }
        }
    }
}

