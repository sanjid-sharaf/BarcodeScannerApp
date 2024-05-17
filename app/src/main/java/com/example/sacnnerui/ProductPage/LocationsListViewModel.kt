package com.example.sacnnerui.ProductPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sacnnerui.Classes.Location
import com.example.sacnnerui.Classes.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationsListViewModel(product: Product) : ViewModel() {
    private val _locations = MutableStateFlow(product.locations)
    val locations: StateFlow<List<Location>> = _locations

    private val _primaryLocation = MutableStateFlow<Location?>(product.primaryLocation)
    val primaryLocation: StateFlow<Location?> = _primaryLocation

    fun removeLocation(location: Location) {
        _locations.value = _locations.value.filterNot { it.code == location.code }
    }

    fun setAsPrimary(location: Location) {
        _primaryLocation.value = location
    }
}

class LocationsListViewModelFactory(private val product: Product) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationsListViewModel(product) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}