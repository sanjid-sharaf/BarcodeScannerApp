package com.example.sacnnerui.data

import com.example.sacnnerui.data.model.APIResponse
import com.example.sacnnerui.data.model.Location
import com.example.sacnnerui.data.model.Product
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface API {
    companion object {
        const val BASE_URL = "http://localhost:5001/"
    }

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int,
//        @Query("q") query: String
    ): Product

    @GET("products/")
    suspend fun getProductsQuery(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10
    ): APIResponse


    @PUT("products/")
    suspend fun updateProducts(
        @Path("id") id : Int
    )
    @Headers("Content-Type: application/json")
    @POST("products/{id}/locations")
    suspend fun addLocation(
        @Path("id") id: Int,
        @Body locationBody: Location.LocationBody
    ): Product


}
