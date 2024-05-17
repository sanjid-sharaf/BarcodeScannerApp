package com.example.sacnnerui


import ProductsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.sacnnerui.ui.theme.SacnnerUITheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sacnnerui.Classes.Location
import com.example.sacnnerui.ProductPage.LocationsListViewModel
import com.example.sacnnerui.Classes.Product
import com.example.sacnnerui.ProductPage.ProductDisplay
import com.example.sacnnerui.SearchBar.SearchViewModel

class MainActivity : ComponentActivity() {
    val sampleProduct = Product(
        title = "Milwaukee M28 Cordless 6-1/2\" Circular Saw Kit\n",
        sku = "MIL082020",
        price = "$99.99",
        location = "WH101A",
        EA = "1",
        MP = "12",
        onHand = "10",
        available = "4",
        committed = " 5",
        onOrder = "0",
        primaryLocation = Location("WH101"),
        locations = listOf(Location("WH101"), Location("WH202"))
    )
    private val searchViewModel: SearchViewModel by viewModels()
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
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.rout
                    ){
                        composable(route = Screen.Home.rout){
                            HomeScreen(navController = navController)
                        }
                        composable(route = Screen.Products.rout){
                            ProductsScreen(navController = navController)
                        }
                        composable(route = Screen.Location.rout){
                            LocationsScreen(navController = navController)
                        }
                        composable(route = Screen.page.rout){
                            ProductDisplay(product = sampleProduct, navController = navController)
                        }
                    }
                }



            }
        }
    }
}

