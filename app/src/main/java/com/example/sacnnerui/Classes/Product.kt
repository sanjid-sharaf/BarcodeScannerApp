package com.example.sacnnerui.Classes

class Product(
    val title: String,
    val sku: String,
    val price: String,
    val location: String,
    val EA: String,
    val MP: String,
    val onHand: String,
    val available: String,
    val committed: String,
    val onOrder: String,
    val primaryLocation: Location,
    val locations: List<Location>

) {
    var measures: List<Quantity> = listOf(
        Quantity("EA Quantity", EA),
        Quantity("Master Pack Quantity", MP)
    )
    val inventoryValues: List<Quantity> = listOf(
        Quantity("On Hand", onHand),
        Quantity("Available", available),
        Quantity("Committed", committed),
        Quantity("On Order", onOrder)
    )
}

data class Quantity(
    val unit: String,
    val value: String
)