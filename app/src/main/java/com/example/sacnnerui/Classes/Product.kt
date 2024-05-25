package com.example.sacnnerui.Classes

class Product(
    val title: String,
    val sku: String,
    val price: String,
    var uoms : MutableList<UOM>,
    var quantities: MutableList<Quantity>,
    var primaryLocation: Location?,
    var locations: MutableList<Location>,



) {

    fun addLocation(location: Location) {
        locations.add(location)
    }
    fun removeLocation(location: Location) {
        locations.remove(location)
        if(primaryLocation == location)
        {
            primaryLocation = null
        }
    }

    fun setPrimary(location: Location){
        primaryLocation = location
    }
    data class Quantity(
        val unit: String,
        val value: String
    )

    public data class UOM(
        val code: String,
        val description: String,
        val conversionFactor: String,
        val weight: String,
        val upc: String,
        val fractionalQuantities: Boolean,
        val ifSell: Boolean,
        val ifBuy: Boolean
    )

}

