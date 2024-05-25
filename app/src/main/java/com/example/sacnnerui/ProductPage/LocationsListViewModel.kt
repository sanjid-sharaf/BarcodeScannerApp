package com.example.sacnnerui.ProductPage


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sacnnerui.Classes.Location
import com.example.sacnnerui.Classes.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationsListViewModel(private val product: Product) : ViewModel() {
    private val _locations = MutableStateFlow(product.locations.toList())
    val locations: StateFlow<List<Location>> = _locations

    private val _primaryLocation = MutableStateFlow<Location?>(product.primaryLocation)
    val primaryLocation: StateFlow<Location?> = _primaryLocation

    fun removeLocation(location: Location) {
        product.removeLocation(location)
        _locations.value = product.locations.toList()
        _primaryLocation.value = product.primaryLocation
    }
    fun addLocation(location: Location)
    {
        product.addLocation(location)
        _locations.value = product.locations.toList()
    }


    fun setAsPrimary(location: Location) {
        product.setPrimary(location)
        _primaryLocation.value = location

    }

    @Composable
    fun AddLocationsDialog(
        onCancel: () -> Unit,
        onConfirm: () -> Unit)
    {
        var locationInput by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { }) {
            Surface(
                shape =  RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Box (
                    contentAlignment = Alignment.Center
                ){
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        Row {
                            Text(
                                text = "Add Location",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = locationInput,
                            onValueChange = { locationInput = it },
                            label = { Text("Enter Location Code") }
                        )

                        Row (
                            horizontalArrangement =  Arrangement.End
                        ){
                            TextButton(
                                onClick = { onCancel() },
                                modifier = Modifier.padding(8.dp),
                            ) {
                                Text("Dismiss")
                            }

                            TextButton(
                                onClick = { onConfirm();  addLocation(Location(locationInput))},
                                modifier = Modifier.padding(8.dp),
                            ) {
                                Text("Confirm")
                            }
                        }
                    }
                }
            }
        }

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
