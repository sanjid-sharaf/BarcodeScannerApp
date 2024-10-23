package com.example.sacnnerui.Screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sacnnerui.Screens.LocationsPage.MassAssignPrimaryScreen
import com.example.sacnnerui.Screens.LocationsPage.MassDelocateScreen
import com.example.sacnnerui.Screens.LocationsPage.MassLocateScreen
import com.example.sacnnerui.Screens.LocationsPage.ProductLocationPage
import com.example.sacnnerui.Screens.LocationsPage.ViewAll
import com.example.sacnnerui.Screens.LocationsScreen.LocationsScreen
import com.example.sacnnerui.Screens.ProductPage.GetProduct
import com.example.sacnnerui.Screens.ProductPage.ProductDisplay
import com.example.sacnnerui.Screens.ProductsScreen.ProductsScreen
import com.example.sacnnerui.data.model.Location
import com.example.sacnnerui.data.model.Product
import com.example.sacnnerui.data.model.UnitsOfMeasure

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable

fun MyNavHost(navController: NavHostController) {
    val uom1 = UnitsOfMeasure(
        allow_fractional_qty = true,
        buy_uom = true,
        code = "EA",
        description = "Each",
        price_factor = "1.0",
        qty_factor = "1.0",
        sell_uom = true,
        upcs = listOf("885911496681"),
        weight = "0.5",
        whse = "WH101"
    )

    val uom2 = UnitsOfMeasure(
        allow_fractional_qty = false,
        buy_uom = false,
        code = "MP",
        description = "Master Pack",
        price_factor = "10.0",
        qty_factor = "10.0",
        sell_uom = false,
        upcs = listOf("987654321098"),
        weight = "0.98",
        whse = "WH101"
    )

    val uomList = mutableListOf(uom1, uom2)

    val sampleProduct = Product(
        backorder_qty = "0",
        committed_qty = "0",
        created_at = "",
        current_cost = "55.0",
        description = "Dewalt SDS+ 1/2\" Carbide Hollow Core Bit",
        extended_description = "",
        id = 12345,
        locations = mutableListOf(
            Location("WH101", true),  // primary location
            Location("WH202")
        ),
        modified_at = "",
        onhand_qty = "2",
        pack_size = 1,
        part_no = "DEWDWA54012",
        purchase_qty = "0",
        price = "89.99",
        status = 1,
        unitsOfMeasure = uomList,
        uom_purchase = "EA",
        uom_sales = "Ea",
        uom_stock = "Each",
        upload = true,
        whse = "Warehouse1"
    )

    /*
    * todo Make ProductPage navigation
    * */
    NavHost(
        navController = navController,
        startDestination = Screen.Home.rout
    ) {
        composable(route = Screen.Home.rout) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Products.rout) {
            ProductsScreen(navController = navController)
        }
        composable(route = Screen.Location.rout) {
            LocationsScreen(navController = navController)
        }
        composable(route = Screen.page.rout) {
            ProductDisplay(product = sampleProduct, navController = navController )
        }

        composable(route = Screen.productPage.rout) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString(Screen.productPage.productIdArg)
            if (productId != null){
                GetProduct(productId = productId.toInt(), navHostController =  navController)
            }
        }

        composable(route = Screen.LocationPage.rout) { backStackEntry ->
            val locationCode = backStackEntry.arguments?.getString(Screen.LocationPage.locationCodeArg)
            val locationTab = backStackEntry.arguments?.getString(Screen.LocationPage.locationTabArg) // Get the locationTab argument

            if (locationCode != null) {
                when (locationTab) {
                    "viewAll" -> ViewAll(locationCode = locationCode , navController = navController) // Compose the ViewAll tab
                    "massLocate" -> MassLocateScreen(locationCode = locationCode , navController = navController) // Example for another tab
                    "massAssignPrimary" -> MassAssignPrimaryScreen(locationCode = locationCode , navController = navController) // Another example tab
                    "massDelocate" -> MassDelocateScreen(locationCode = locationCode , navController = navController) // Another example tab
                    else -> ViewAll(locationCode = locationCode , navController = navController) // Default tab if no match is found
                }

            }
        }

        composable(route = Screen.ProductLocationPage.rout) { backStackEntry ->
            val productID = backStackEntry.arguments?.getString(Screen.ProductLocationPage.productIdArg)
            if (productID != null) {
                ProductLocationPage(productId = productID.toInt(),
                    onBack = {navController.popBackStack()})
            }
        }
    }
}