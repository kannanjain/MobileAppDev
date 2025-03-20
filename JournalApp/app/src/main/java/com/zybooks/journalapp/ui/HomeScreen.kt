package com.zybooks.journalapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(viewModel: CalendarViewModel, navController: NavController) {
    val today by viewModel.selectedDate.observeAsState(LocalDate.now())

    // Wrapping the content with a Scrollable modifier
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Make it scrollable
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Display Today's Date
        Text(
            text = today.format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy")).uppercase(),
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Greeting
        Text(
            text = "Hello, Rebecca!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Start Journal Button - Navigate to EntryScreen
        Button(
            onClick = { navController.navigate("entry") },
            colors = ButtonDefaults.buttonColors(Color(0xFFB0BEC5)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(text = "Start Today’s Journal", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Month Selector
        MonthSelector(viewModel)

        Spacer(modifier = Modifier.height(10.dp))

        // Calendar Grid
        CalendarGrid(today.monthValue, today.year, today)
    }
}

@Composable
fun CalendarGrid(month: Int, year: Int, today: LocalDate) {
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    val firstDayOfMonth = LocalDate.of(year, month, 1).dayOfWeek.value % 7  // Adjust to start from Monday
    val days = (1..daysInMonth).toList()

    Column {
        // Weekday Headers
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
                Text(text = it, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier
                    .width(40.dp) // Distributes space evenly
                    .wrapContentWidth(Alignment.CenterHorizontally))
            }
        }

        Spacer(modifier = Modifier.height(7.dp))

        var dayIndex = 1
        for (week in 0 until 6) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (dayOfWeek in 1..7) {
                    if ((week == 0 && dayOfWeek < firstDayOfMonth) || dayIndex > daysInMonth) {
                        Text(text = " ", modifier = Modifier.size(40.dp)) // Empty space before first day
                    } else {
                        val day = dayIndex
                        val isToday = today.dayOfMonth == day && today.monthValue == month && today.year == year

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(if (isToday) Color.DarkGray else Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.toString(),
                                fontSize = 16.sp,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                color = if (isToday) Color.White else Color.Black
                            )
                        }
                        dayIndex++
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun MonthSelector(viewModel: CalendarViewModel) {
    val today by viewModel.selectedDate.observeAsState(LocalDate.now())

    // Remember the current month and year when the user clicks next or previous month
    val currentMonth = rememberSaveable { mutableStateOf(today.monthValue) }
    val currentYear = rememberSaveable { mutableStateOf(today.year) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = {
            // When clicking the previous month, decrement and update the state
            viewModel.changeMonth(-1)
            currentMonth.value = today.monthValue
            currentYear.value = today.year
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
        }

        Text(
            text = today.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + today.year,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        )

        IconButton(onClick = {
            // When clicking the next month, increment and update the state
            viewModel.changeMonth(1)
            currentMonth.value = today.monthValue
            currentYear.value = today.year
        }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
        }
    }
}
