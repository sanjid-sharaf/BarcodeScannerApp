package com.example.sacnnerui.Screens

import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.example.sacnnerui.R
import com.example.sacnnerui.ScanBroadcastReceiver


@Composable
fun HomeScreen( navController: NavHostController) {

    var decodedData by remember { mutableStateOf("") }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        println("Home Screen Composed")
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
            addAction(navController.context.getString(R.string.activity_intent_filter_action)) // Adding the action
        }
        // Register the receiver dynamically
        context.registerReceiver(broadcastReceiver, intentFilter)

        onDispose { println("home Screen disposed")
            context.unregisterReceiver(broadcastReceiver) }
    }

    LaunchedEffect(decodedData){
        if (decodedData!="" && decodedData.isDigitsOnly())
        {
            navController.navigate("products/$decodedData")
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable._1380_interline_logo_rgb),
            contentDescription = "Interline Log",
            modifier = Modifier
                .size(200.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate(Screen.Products.rout)
        }) {
            Text(
                text = "Products",
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate(Screen.Location.rout)
        }) {
            Text(
                text = "Locations",
            )
        }
        Button(onClick = {
            navController.navigate(Screen.page.rout)
        }) {
            Text(

                text = "sample",
            )
        }
    }
}