package com.example.sacnnerui.Screens.ProductsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sacnnerui.data.ProductsRepository
import com.example.sacnnerui.data.Result
import com.example.sacnnerui.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ProductsViewModel(
    private val repository: ProductsRepository
) : ViewModel() {

    /**
    * Mutable State of Product Lists and a Single Product
    * productList to hold the state a list of products from a query. Uses [ProductsRepository.getProductsQuery]
    * product holds the State flow of a single product. Uses [ProductsRepository.getProduct]
    * */

    init {
        println("Products view Model created")
    }

    override fun onCleared() {
        println("products ViewModel cleared")
        super.onCleared()
    }

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val products  = _productList.asStateFlow()

    val _product  = MutableStateFlow<Product?>(null)
    val product = _product.asStateFlow()

    /**
     * @param id -> inventory id of product.
     * updates the state of prodcut
     * TODO Error Handling.
    * */
    fun fetchProduct(id: Int) {
        viewModelScope.launch {
            repository.getProduct(id).collectLatest {
                result ->
                when(result) {
                    is Result.Error ->{
                    }
                    is Result.Success -> {
                        result.data?.let {
                                product -> _product.update { product }
                        }
                    }
                }
            }
        }
    }
    /**
     * @param query -> Query from Search Box.
     * updates the state of products List.
     * TODO Error Handling.
     * */
    fun fetchProductList(query: String) {
        viewModelScope.launch {
            println("callroutine")
            repository.getProductsQuery(query).collectLatest { result ->
                when(result) {
                    is Result.Error ->{

                    }
                    is Result.Success -> {
                        result.data?.let {
                            products -> _productList.update { products }

                        }
                    }
                }
            }
        }
    }
}

// viewmodel Factory >>
class ProductsViewModelFactory(
    private val repository: ProductsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}