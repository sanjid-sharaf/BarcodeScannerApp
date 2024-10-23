package com.example.sacnnerui.Screens.LocationsPage

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import coil.compose.rememberAsyncImagePainter
import com.example.sacnnerui.R
import com.example.sacnnerui.ScanBroadcastReceiver
import com.example.sacnnerui.Screens.ProductsScreen.SearchBox


@SuppressLint("MutableCollectionMutableState")
@Composable
fun ProductLocationPage(productId: Int, onBack: () -> Unit) {

    var decodedData by remember { mutableStateOf("") }
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val imageUrl = "https://www.milwaukeetool.ca/--/web-images/sc/20eb754dd019493e924c126941c7d01a?hash=3b17fd61c56f8beb29390c9e36b76509"
    val locationsList = remember { mutableStateListOf("WH1011", "AI101", "B1027", "A2323", "B23123", "2312", "00") }

    DisposableEffect(Unit) {
        println("Product Location Page called")
        val broadcastReceiver = ScanBroadcastReceiver { data ->
            // Update the state with the decoded data
            if (data != null) {
                decodedData = data
                println(decodedData)
            }
        }
        // Create an intent filter with categories and action
        val intentFilter = IntentFilter().apply {
            addCategory(Intent.CATEGORY_LAUNCHER) // Adding the category
            addAction(context.getString(R.string.activity_intent_filter_action)) // Adding the action
        }
        // Register the receiver dynamically
        context.registerReceiver(broadcastReceiver, intentFilter)



        onDispose {
            println("Product location Page disposed")
            context.unregisterReceiver(broadcastReceiver)
        }
    }

    LaunchedEffect(decodedData){
        if (decodedData!="" && decodedData.all { it.isLetterOrDigit() } && !locationsList.contains(decodedData))
        {
            locationsList.add(decodedData)
        }
        decodedData = ""
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery!=""){
            locationsList.add(searchQuery)
        }
        searchQuery = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        // Top Red Tab
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFFF6F60)), // Red background
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        // Search Box with Icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.QrCode, // Using Material Design QR Code icon
                contentDescription = "QR Code",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))

            SearchBox(
                text = "Search Or Scan Location",
                onSearch = { query ->
                           searchQuery = query
                },
                modifier = Modifier
                    .border(width = 0.5.dp, color = Color.Black)
                    .size(width = 250.dp, height = 50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { /* Handle search action */ }) {
                Icon(
                    imageVector = Icons.Filled.Search, // Replace with your search icon
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            }
        }

        // Blue Bar for Product Details
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF7B99DC)) // Blue background
                .padding(horizontal = 0.dp, vertical = 10.dp),
        ) {

            // Row for Image and Product Details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // White container for the image
                Box(
                    modifier = Modifier
                        .size(90.dp) // Size of the container
                        .background(Color.White) // White background
                ) {
                    // Image on the left
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxSize() // Fill the container
                    )
                }

                // Product Details on the right
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp), // Fill remaining space
                    verticalArrangement = Arrangement.Center

                ) {
                    Text(
                        text = "MIL285320", // Product ID
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Milwaukee M18 FUEL Cordless 1/4\" Hex Impact Driver - Tool Only", // Product description
                        fontSize = 12.sp,
                        lineHeight = 14.sp, // Set this value to control the space between lines
                        color = Color.Black
                    )
                    Text(
                        text = "Onhand: 12", // Onhand quantity
                        fontSize = 10.sp,
                        color = Color.Black
                    )
                }
            }
        }

        // Subheading "Locations"
        Text(
            text = "Locations",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        // Container for Locations Table
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Box border
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(locationsList) { index,location ->
                    val backgroundColor =
                        if (index % 2 == 0) Color.White else Color.LightGray

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 1.dp) // Tighter padding
                            .background(backgroundColor)
                            .size(200.dp, height = 30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star, // Star icon for favorites
                            contentDescription = "Favorite",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp) // Slightly larger icon size
                        )
                        Spacer(modifier = Modifier.width(4.dp)) // Reduced space
                        Text(
                            text = location,
                            fontSize = 14.sp, // Smaller font size for more data visibility
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            locationsList.removeAt(index)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close, // Close icon for delete
                                contentDescription = "Delete",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp) // Slightly larger icon size
                            )
                        }
                    }
                    Divider(color = Color.Gray, thickness = 1.dp) // Add a divider between rows
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductLocationPagePreview() {
    MaterialTheme {
        ProductLocationPage(productId = 93434, onBack = {})
    }
}
