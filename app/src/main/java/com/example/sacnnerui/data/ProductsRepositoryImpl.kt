package com.example.sacnnerui.data

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.sacnnerui.data.model.Location
import com.example.sacnnerui.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException


/*
* Implementation of Product Repository Interface
*  */

class ProductsRepositoryImpl(
    private val api: API
) : ProductsRepository {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getProduct(id: Int): Flow<Result<Product>> {
        return flow {
            println("getProductFunction")
            val productFromApi = try {
                api.getProduct(id)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading  - io"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading products - https"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading products"))
                return@flow
            }

            emit(Result.Success(productFromApi))

        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getProductsQuery(query: String): Flow<Result<List<Product>>> {
        return flow {
            println("getProductsListFunction")
            val productsFromApi = try {
                api.getProductsQuery(query)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading  - io"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading products - https"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading products"))
                return@flow
            }
            emit(Result.Success(productsFromApi.records))

        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun addLocation(
        id: Int,
        locationBody: Location.LocationBody
    ): Flow<Result<Product>> {
        return flow {
            val locationPostResult = try {
                api.addLocation(id, locationBody)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading  - io"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading products - https"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading products"))
                return@flow
            }
            emit(Result.Success(locationPostResult))
        }
    }

}





