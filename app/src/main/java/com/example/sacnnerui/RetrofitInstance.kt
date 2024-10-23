package com.example.apitesting


import com.example.sacnnerui.data.API
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/*
* Retrofit Object
* Specifies Timeout, Base URL
*
* */
object RetrofitInstance {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(30,TimeUnit.SECONDS)
        .readTimeout(30,TimeUnit.SECONDS)
        .build()


    val api: API = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(API.BASE_URL)
        .client(client)
        .build()
        .create(API::class.java)

}
