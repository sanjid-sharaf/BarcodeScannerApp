package com.example.sacnnerui.Screens.ProductPage


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresExtension
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
import androidx.lifecycle.viewModelScope
import com.example.apitesting.RetrofitInstance
import com.example.sacnnerui.data.ProductsRepositoryImpl
import com.example.sacnnerui.data.Result
import com.example.sacnnerui.data.model.Location
import com.example.sacnnerui.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LocationsListViewModel(private val product: Product) : ViewModel() {

    init {
        println("Locations view Model created")
    }

    override fun onCleared() {
        println("Location ViewModel cleared")
        super.onCleared()
    }

    private val _locations = MutableStateFlow(product.locations.toList())
    val locations: StateFlow<List<Location>> = _locations

    private val _primaryLocation = MutableStateFlow<Location?>(product.getPrimary())
    val primaryLocation: StateFlow<Location?> = _primaryLocation

    fun removeLocation(location: Location) {
        product.removeLocation(location)
        _locations.value = product.locations.toList()
        _primaryLocation.value = product.getPrimary()
    }

    fun addLocation(location: Location)
    {
        product.addLocation(location)
        _locations.value = product.locations.toList()
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun setAsPrimary(location: Location) {

        val locationBody = Location.LocationBody(true, location.location_code)
        postLocation(product.id, locationBody)

    }

    fun updateLocations(locationList: MutableList<Location>)
    {
        product.locations = locationList
        _primaryLocation.value = product.getPrimary()
        _locations.value = product.locations.toList()
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun postLocation(id: Int, locationBody: Location.LocationBody)
    {
       viewModelScope.launch {
            ProductsRepositoryImpl(RetrofitInstance.api).addLocation(id,locationBody).collectLatest {
                    result ->
                when(result) {
                    is Result.Error ->{

                    }
                    is Result.Success -> {
                        result.data?.let {
                            product -> updateLocations(product.locations)
                        }
                        println("Post Succesull")
                        }
                    }
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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
                            onValueChange = {  locationInput = it.uppercase() },
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
                                onClick = { onConfirm();  postLocation(product.id, Location.LocationBody(locationInput))},
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
