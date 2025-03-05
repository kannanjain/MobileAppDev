package com.zybooks.journalapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EntryScreen(viewModel: EntryViewModel) {
    val entryText by viewModel.entryText.observeAsState("Today I was feeling ...")
    val mood by viewModel.mood.observeAsState("Happy 🙂")
    val location by viewModel.location.observeAsState("123 Ave 📍")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        // Journal Entry Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(20.dp)
                .align(Alignment.Center), // Centers the journal box
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = location,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )

                Spacer(modifier = Modifier.height(100.dp))

                Text(text = entryText, fontSize = 16.sp, color = Color.Black)
            }
        }

        // Slanted Photo (Overlapping the journal entry)
        Box(
            modifier = Modifier
                .size(170.dp)
                .offset(x = 30.dp, y = 80.dp) // Move it lower for better overlap
                .rotate(-15f) // Keeps the slant
                .align(Alignment.TopStart) // Positions near the top
                .background(Color.LightGray, RoundedCornerShape(10.dp)),
                //.shadow(6.dp, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    // Placeholder for Image
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = mood, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

