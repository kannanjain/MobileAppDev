package com.zybooks.journalapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import java.io.File
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.zIndex
import androidx.compose.material.icons.filled.Camera // For filled style
import androidx.compose.material.icons.filled.Share
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

@Composable
fun EntryScreen(viewModel: EntryViewModel, navController: NavController) {
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
    val cameraUri =
        FileProvider.getUriForFile(context, "${context.packageName}.provider", cameraFile)

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                viewModel.captureImage(cameraUri)
            }
        }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(cameraUri)
        } else {
            Toast.makeText(context, "Camera permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Make entire content scrollable vertically
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back to Calendar Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigate("home") }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to Calendar",
                        tint = Color.Gray
                    )
                }
            }
            // Image & Mood Card
            Card(
                modifier = Modifier
                    .size(180.dp, 200.dp)
                    .offset(x = 30.dp, y = 20.dp)
                    .rotate(-10f)
                    .zIndex(1f)
                    .horizontalScroll(rememberScrollState()), // Make the Card scroll horizontally if needed
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
                            .clickable {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                    == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(cameraUri)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
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

            Spacer(modifier = Modifier.height(16.dp))

            // Journal Entry Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(20.dp),
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

            Spacer(modifier = Modifier.height(16.dp))

            // Share Button
            Button(
                onClick = {
                    val shareText = "Journal Entry:\n\n$entryText\n\nMood: $mood\nLocation: $location"
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share your journal entry"))
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF8C98A8)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Share Entry", fontSize = 14.sp)
            }
        }
    }
}
