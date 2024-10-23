package com.example.sacnnerui.data.model

data class Location(
    val description: String,
    val id: Int,
    var is_primary: Boolean,
    val location_code: String,
    val location_id: Int,
    val part_no: String,
    val product_id: Int
){
    // Secondary constructor with only id
    constructor(code: String) : this(
        description =code,
        id = 0,
        is_primary = false,
        location_code = code,
        location_id = 0,
        part_no = "",
        product_id = 0
    )
    constructor(code: String, is_primary: Boolean) : this(
        description = code,
        id = 0,
        is_primary = is_primary,
        location_code = code,
        location_id = 0,
        part_no = "",
        product_id = 0
    )

    data class LocationBody(
        var isPrimary: Boolean,
        var locationCode: String
    ) {
        constructor(code: String) : this(
            isPrimary = false,
            locationCode = code
        )
    }

}