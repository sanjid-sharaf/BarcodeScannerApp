package com.example.sacnnerui.data

import com.example.sacnnerui.data.model.Location
import com.example.sacnnerui.data.model.Product
import kotlinx.coroutines.flow.Flow

/*
* Interface For API Methods
* Suspend Functions
* */
interface ProductsRepository {
    suspend fun getProduct(id : Int): Flow<Result<Product>>
    suspend fun getProductsQuery(query: String): Flow<Result<List<Product>>>
    suspend fun addLocation(id: Int, locationBody: Location.LocationBody): Flow<Result<Product>>
}
