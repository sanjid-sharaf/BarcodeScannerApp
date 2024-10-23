package com.example.sacnnerui.Screens

/*
* Class That holds the String Values for Screen Objects for Navigation
* */
sealed class Screen(val rout: String) {
    object Home : Screen("home")
    object Products : Screen("products")
    object Location : Screen("locations")
    object page : Screen("page")

    object productPage : Screen("products/{productID}"){
        const val productIdArg = "productID"
    }
    object LocationPage : Screen("locations/{locationCode}/{locationTab}") {
        const val locationCodeArg = "locationCode" // Use a constant for the argument name
        const val locationTabArg = "locationTab"
    }
    object ProductLocationPage : Screen("locations/product/{productID}") {
        const val productIdArg = "productID" // Use a constant for the argument name
    }

}