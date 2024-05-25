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

    val uom1 = Product.UOM(
        code = "EA",
        description = "Each Quantity",
        conversionFactor = "1",
        weight = "1 kg",
        upc = "347365839234",
        fractionalQuantities = false,
        ifSell = true,
        ifBuy = true
    )

    val uom2 = Product.UOM(
        code = "MP",
        description = "Master Pack",
        conversionFactor = "12 per 1",
        weight = "20",
        upc = "347365833412",
        fractionalQuantities = true,
        ifSell = true,
        ifBuy = true
    )

    val quantities = mutableListOf(
        Product.Quantity(unit = "On Hand", value = "100"),
        Product.Quantity(unit = "Available", value = "80"),
        Product.Quantity(unit = "On Order", value = "20"),
        Product.Quantity(unit = "Committed", value = "10")
    )

    val uomList = mutableListOf(uom1, uom2)




    val sampleProduct = Product(
        title = "Milwaukee M28 Cordless 6-1/2\" Circular Saw Kit\n",
        sku = "MIL082020",
        price = "$99.99",
        uoms = uomList,
        quantities = quantities,
        primaryLocation = Location("WH101"),
        locations = mutableListOf(Location("WH101"), Location("WH202"))
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

