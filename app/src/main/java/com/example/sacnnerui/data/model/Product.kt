package com.example.sacnnerui.data.model

import java.util.Locale

data class Product(
    val backorder_qty: String,
    val committed_qty: String,
    val created_at: String,
    val current_cost: String,
    val description: String,
    val extended_description: String,
    val id: Int,
    var locations: MutableList<Location>,
    val modified_at: String,
    val onhand_qty: String,
    val pack_size: Any,
    val part_no: String,
    val purchase_qty: String,
    val price: String,
    val status: Int,
    val unitsOfMeasure: MutableList<UnitsOfMeasure>,
    val uom_purchase: String,
    val uom_sales: String,
    val uom_stock: String,
    val upload: Boolean,
    val whse: String
)
{
    data class Quantity(
        val unit: String,
        val value: String
    )


    fun getQuantities(): List<Quantity> {
        return listOf(
            Quantity(unit = "Available", value = String.format(Locale.getDefault(), "%.2f", (onhand_qty.toDouble() - committed_qty.toDouble()))),
            Quantity(unit = "Committed", value = String.format(Locale.getDefault(), "%.2f", committed_qty.toDouble())),
            Quantity(unit = "Backorder", value = String.format(Locale.getDefault(), "%.2f", backorder_qty.toDouble())),
            Quantity(unit = "On Hand", value = String.format(Locale.getDefault(), "%.2f", onhand_qty.toDouble()))
        )
    }


    fun getPrimary(): Location? {
        for(l in locations)
        {
            if(l.is_primary)
            {
                return l
            }
        }
        return null
    }

    fun addLocation(location: Location) {
        locations.add(location)
    }
    fun removeLocation(location: Location) {
        locations.remove(location)
        if(location.is_primary)
        {
            location.is_primary = false
        }
    }

    fun setPrimary(location: Location){
        location.is_primary = true
    }

    fun addUoms(uom: UnitsOfMeasure) {
        unitsOfMeasure.add(uom)
    }

    fun removeUoms(uom: UnitsOfMeasure) {
        unitsOfMeasure.remove(uom)
    }

    fun updateUom(oldUOM: UnitsOfMeasure, newUOM: UnitsOfMeasure)
    {
        unitsOfMeasure.remove(oldUOM)
        unitsOfMeasure.add(newUOM)
    }
}