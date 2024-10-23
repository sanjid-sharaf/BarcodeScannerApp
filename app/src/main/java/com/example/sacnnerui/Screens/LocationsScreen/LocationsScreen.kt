package com.example.sacnnerui.Screens.LocationsScreen

import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.example.sacnnerui.R
import com.example.sacnnerui.ScanBroadcastReceiver
import com.example.sacnnerui.Screens.ProductsScreen.SearchBox
import com.example.sacnnerui.Screens.Screen

@Composable
fun LocationsScreen(navController: NavHostController) {
    var locationCode by remember { mutableStateOf("") }
    var productId by remember { mutableStateOf( "")}
    val context = LocalContext.current


    /*
    Registering Broadcast receiver for Scans
    Scanned Data is assumed to be location code and navigates to locations page
     */
    DisposableEffect(Unit) {
        println("Location Screen Composed")
        val broadcastReceiver = ScanBroadcastReceiver { data ->
            // Update the state with the decoded data
            if (data != null) {
                locationCode = data
                println(locationCode)
            }
        }
        // Create an intent filter with categories and action
        val intentFilter = IntentFilter().apply {
            addCategory(Intent.CATEGORY_LAUNCHER) // Adding the category
            addAction(navController.context.getString(R.string.activity_intent_filter_action)) // Adding the action
        }
        // Register the receiver dynamically
        context.registerReceiver(broadcastReceiver, intentFilter)

        onDispose { println("Location Screen disposed")
            context.unregisterReceiver(broadcastReceiver) }
    }
    /**
     * Checks if [locationCode] is changing
     * Only navigate if code is alphanumeric*/

    LaunchedEffect(locationCode){
        if (locationCode!="" && locationCode.all { it.isLetterOrDigit() })
        {
            navController.navigate("locations/$locationCode/viewAll")
        }
    }

    LaunchedEffect(productId){
        if (productId!="" && productId.isDigitsOnly())
        {
            navController.navigate("locations/product/$productId")
        }
    }


  //==============================================================================================================

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .verticalScroll(scrollState)
            .imePadding()
    ) {
        /*
        * Row For Back Button and headers
        * */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = {
                navController.navigate(Screen.Home.rout)
            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Text(
                text = "Locations",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        /*
        * Column holding SearchBox and Interline Logo
        * */

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable._1380_interline_logo_rgb),
                contentDescription = "Interline Log",
                modifier = Modifier
                    .size(150.dp)
            )

            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .size(width = 250.dp, height = 150.dp)
            ) {
                // Apply the background inside the card content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFFFA6559)), // Set background inside the card content
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Search Or Scan Location",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchBox(
                        text = "->",
                        /*
                        * If query is not empty, Use Viewmodel to Make API CAll
                        * -> updates the products List State
                        * */
                        onSearch = { query ->
                            locationCode = query
                        },
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.White)
                            .size(width = 200.dp, height = 50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .size(width = 250.dp, height = 150.dp)
            ) {
                // Apply the background inside the card content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFF7B99DC)), // Set background inside the card content
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Search Or Scan Products",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchBox(
                        onSearch = { query ->
                                   productId = query
                        },
                        text = "->",
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.White)
                            .size(width = 200.dp, height = 50.dp)
                            .imePadding()

                    )
                }
            }
        }
    }
}



