package com.example.sacnnerui


sealed class Screen(val rout: String) {
    object Home : Screen("home")
    object Products : Screen("products")
    object Location : Screen("locations")
    object page: Screen("page")
}
