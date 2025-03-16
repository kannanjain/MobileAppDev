package com.zybooks.journalapp.ui

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.camera.core.Camera
import androidx.compose.foundation.Image
//import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import java.io.File
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import com.zybooks.journalapp.R
import androidx.compose.material.icons.filled.Camera // For filled style
import androidx.compose.material.icons.outlined.Camera // For outlined style
// Use Icons.Filled.Camera or Icons.Outlined.Camera

/*@Composable
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

                Spacer(modifier = Modifier.height(9.dp))

                Text(text = mood, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}*/

@Composable
fun EntryScreen(viewModel: EntryViewModel) {
    val context = LocalContext.current
    val entryText by viewModel.entryText.observeAsState("Today I was feeling ...")
    val mood by viewModel.mood.observeAsState("Happy 🙂")
    val location by viewModel.location.observeAsState("Add Location 📍")
    val imageUri by viewModel.imageUri.observeAsState()

    // File for captured image
    val cameraFile = remember {
        File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "journal_image_${System.currentTimeMillis()}.jpg"
        )
    }
    val cameraUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", cameraFile)

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            viewModel.captureImage(cameraUri)
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            viewModel.fetchLocation()
        } else {
            Toast.makeText(context, "Location permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        // Image & Mood Card (Floating, Tilted)
        Card(
            modifier = Modifier
                .size(180.dp, 200.dp)
                .offset(x = 30.dp, y = 20.dp)
                .rotate(-10f)
                .zIndex(1f),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(Color(0xFFEFEFEF))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .clickable { cameraLauncher.launch(cameraUri) },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Captured Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Camera",
                            tint = Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.selectMood() },
                    colors = ButtonDefaults.buttonColors(Color(0xFFB0BEC5)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = mood, fontSize = 14.sp)
                }
            }
        }

        // Journal Entry Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp) // Adjusted height
                .padding(20.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(Color(0xFFD0D6DE))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Location Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                        colors = ButtonDefaults.buttonColors(Color(0xFF8C98A8)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = location, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Journal Entry Editable Box
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE8E8E8), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    TextField(
                        value = entryText,
                        onValueChange = { viewModel.updateEntry(it) },
                        modifier = Modifier.fillMaxSize(),
                        placeholder = { Text("Write your journal entry here...") },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        )
                    )

                    // Edit Icon at Bottom-Right
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .size(24.dp)
                            .clickable { /* Open edit mode */ },
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}


