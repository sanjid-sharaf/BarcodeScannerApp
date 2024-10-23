package com.example.sacnnerui


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.sacnnerui.Screens.MyNavHost
import com.example.sacnnerui.ui.theme.SacnnerUITheme
import kotlinx.coroutines.flow.MutableStateFlow


class MainActivity : ComponentActivity() {
    private val _currentRoute = MutableStateFlow<String?>(null)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            SacnnerUITheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val navController = rememberNavController()
                    MyNavHost(navController = navController)
                    navController.addOnDestinationChangedListener { _, destination, _ ->
                        _currentRoute.value = destination.route // Update current route
                    }

                }

            }
        }

    }
    /**
    // Intent Extras Containing Label Name And Data >> Part Numbers(Barcodes), UPCs and Location Codes
    // ToDO : Distinguish decodedData from UPC, Location Code, etc.
    */
}



class ScanBroadcastReceiver(
    private val onDecodedData: (String?) -> Unit // Callback to return decoded data
) : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras

        // Extract the decoded data from the intent
        val decodedSource = extras?.getString(context.getString(R.string.com_sacnner_ui_source))
        val decodedData = extras?.getString(context.getString(R.string.com_sacnner_ui_data_string))
        val decodedLabelType = extras?.getString(context.getString(R.string.com_sacnner_ui_label_type))

        println("Source: $decodedSource")
        println("Data: $decodedData")
        println("Label Type: $decodedLabelType")

        // Return the decoded data through the callback
        onDecodedData(decodedData)
    }
}
