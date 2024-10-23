package com.example.sacnnerui.data.model

data class UnitsOfMeasure(
    val allow_fractional_qty: Boolean,
    val buy_uom: Boolean,
    val uom: String,
    val created_at: String,
    val description: String?,
    val id: Int,
    val inventory_id: Int,
    val modified_at: String,
    val part_no: String,
    val price_factor: String,
    val qty_factor: String,
    val sell_uom: Boolean,
    val upcs: List<String>,
    val weight: String,
    val whse: String
) {
    constructor(
        upcs: List<String>,
        allow_fractional_qty: Boolean,
        code: String,
        description: String,
        price_factor: String,
        qty_factor: String,
        weight: String,
        buy_uom: Boolean,
        sell_uom: Boolean,
        whse: String
    ) : this(
        allow_fractional_qty = allow_fractional_qty,
        buy_uom = buy_uom,
        uom = code,
        created_at = "",
        description = description,
        id = 0,
        inventory_id = 0,
        modified_at = "",
        part_no = "",
        price_factor = price_factor,
        qty_factor = qty_factor,
        sell_uom = sell_uom,
        upcs = upcs,
        weight = weight,
        whse = whse
    )
}