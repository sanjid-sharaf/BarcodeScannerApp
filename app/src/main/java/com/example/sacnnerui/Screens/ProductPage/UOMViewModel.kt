package com.example.sacnnerui.Screens.ProductPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sacnnerui.data.model.Product
import com.example.sacnnerui.data.model.UnitsOfMeasure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UOMViewModel(private val product: Product) : ViewModel() {
    init {
        println("UOM view Model created")
    }
    override fun onCleared() {
        println("UOM ViewModel cleared")
        super.onCleared()
    }

    private val _uoms = MutableStateFlow(product.unitsOfMeasure.toMutableList())
    val uoms: StateFlow<List<UnitsOfMeasure>> = _uoms

    fun addUoms(uom: UnitsOfMeasure)
    {
        product.addUoms(uom)
        _uoms.value = product.unitsOfMeasure.toMutableList()
    }

    fun removeUoms(uom: UnitsOfMeasure)
    {
        product.removeUoms(uom)
        _uoms.value = product.unitsOfMeasure.toMutableList()
    }

    fun updateUom(oldUom: UnitsOfMeasure, newUom: UnitsOfMeasure )
    {
        product.updateUom(oldUom,newUom)
        _uoms.value = product.unitsOfMeasure.toMutableList()
    }
}


class UOMViewModelFactory(private val product: Product) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UOMViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UOMViewModel(product) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}