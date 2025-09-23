package com.example.krishimitra.presentation.notification_screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.rememberAsyncImagePainter
import com.example.krishimitra.R
import com.example.krishimitra.domain.notification_model.GlobalNotificationData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    moveBackToHomeScreen: () -> Unit,
    state: NotificationScreenState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Notifications")
                },
                navigationIcon = {
                    IconButton(onClick = moveBackToHomeScreen) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Move back to home screen"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.slight_dark_green),
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.light_green))
                .padding(innerPadding)
        ) {
            items(state.notificationList) { notification ->
                NotificationItem(
                    notificationData = notification,
                    context = LocalContext.current
                )
            }
        }
    }
}

@Composable
fun NotificationItem(
    notificationData: GlobalNotificationData,
    context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
            .clickable {
                notificationData.webLink?.let {
                    try {
                        val intent =
                            Intent(Intent.ACTION_VIEW, notificationData.webLink.toUri())
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = notificationData.title,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatTime(notificationData.timeStamp),
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = notificationData.description)

            notificationData.imageUrl?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(notificationData.imageUrl),
                    contentDescription = "Notification Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}

@Composable
fun formatTime(timestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val difference = currentTime - timestamp

    val seconds = difference / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        minutes < 60 -> "$minutes m ago"
        hours < 24 -> "$hours h ago"
        days < 7 -> "$days d ago"
        else -> {
            val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}


