package com.example.sacnnerui.ProductPage

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sacnnerui.Classes.Location
import com.example.sacnnerui.Classes.Product


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDisplay(product: Product, navController: NavController) {
    val factory = LocationsListViewModelFactory(product)

    // Obtain the ViewModel instance using the factory
    val LocationsViewModel: LocationsListViewModel = viewModel(factory = factory)
    val locations by LocationsViewModel.locations.collectAsState()
    val primaryLocation by LocationsViewModel.primaryLocation.collectAsState()
    val addLocationDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        IconButton(onClick = {
            navController.navigateUp()
        }) {
            Icon(
                imageVector = Icons.Rounded.ArrowBackIosNew,
                contentDescription = "Back Icon",
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = product.sku,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = product.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp)
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = product.price,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.Black)
            )

            ProductQuantityTable(product = product)

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            Arrangement.Center
        )
        {
            ExtendedFloatingActionButton(onClick =
            {
                addLocationDialog.value = true
            },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
                icon  = { Icon(Icons.Filled.AddLocation, "Small floating action button.")},
                text = {Text(text = "Locate")}
            )
        }
        when {
            addLocationDialog.value -> {
                LocationsViewModel.AddLocationsDialog(
                    onCancel = {addLocationDialog.value = false},
                    onConfirm = {addLocationDialog.value = false}
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background))

            {
                Text(
                    text = "Primary Location: ${primaryLocation?.code}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
                LocationList(
                    locations = locations,
                    primaryLocation = primaryLocation,
                    onRemove = {LocationsViewModel.removeLocation(it) },
                    onSetAsPrimary = {LocationsViewModel.setAsPrimary(it) }
                )

        }

    }
    // Adding UnitsOfMeasureSection here
    UnitsOfMeasureSection(Product = product)


}





@Composable
fun ProductQuantityTable(product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSurfaceVariant)
    )
    {
        product.quantities.forEach { quantity ->
            Text(
                text = quantity.unit,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(8.dp)
            )
        }

    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.outline),
        Arrangement.SpaceEvenly

    )
    {
        product.quantities.forEach { quantity ->
            Text(
                text = quantity.value,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(8.dp)
            )
        }

    }
}

@Composable
fun LocationRow(
    location: Location,
    primaryLocation: Location?,
    onRemove: () -> Unit,
    onSetAsPrimary: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = location.code,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(8.dp))

        if (location != primaryLocation) {
            Button(onClick = onSetAsPrimary) {
                Text("Set as Primary")
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onRemove) {
            Text("Remove")
        }
    }
}

@Composable
fun LocationList(
    locations: List<Location>,
    primaryLocation: Location?,
    onRemove: (Location) -> Unit,
    onSetAsPrimary: (Location) -> Unit
) {
    LazyColumn {
        items(locations) { location ->
            LocationRow(
                location = location,
                primaryLocation = primaryLocation,
                onRemove = { onRemove(location) },
                onSetAsPrimary = { onSetAsPrimary(location) }
            )
        }
    }
}



@Composable
fun UnitsOfMeasureSection(Product : Product) {
    var isVisible by remember {
        mutableStateOf(false)
    }
    var iconState by remember {
        mutableStateOf(Icons.Rounded.KeyboardArrowUp)
    }

    var showAddUomDialog by remember { mutableStateOf(false) }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .animateContentSize()
        ) {

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .animateContentSize()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable {
                        isVisible = !isVisible
                        iconState = if (isVisible) {
                            Icons.Rounded.KeyboardArrowDown
                        } else {
                            Icons.Rounded.KeyboardArrowUp
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = iconState,
                        contentDescription = "Units of Measure",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )

                }

                Spacer(modifier = Modifier.width(20.dp))

                Text(
                    text = "Units of Measure",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(20.dp))

                IconButton(onClick ={showAddUomDialog = true })
                {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add Icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                if (showAddUomDialog) {
                    UOMInputDialog(
                        onSave = { newUOM ->
                            var uom = newUOM
                            Product.uoms.add(uom)
                            showAddUomDialog = false
                        },
                        onClose = { showAddUomDialog = false }
                    )
                }

            }

            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )

            if (isVisible) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                        .background(MaterialTheme.colorScheme.background)
                ) {

                    val boxWithConstraintsScope = this
                    val width = boxWithConstraintsScope.maxWidth / 4

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                modifier = Modifier.width(width),
                                text = "Unit",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                modifier = Modifier.width(width),
                                text = "Description",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                modifier = Modifier.width(width),
                                text = "Conversion Factor",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Text(
                                modifier = Modifier.width(width),
                                text = "View",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn {
                                items(Product.uoms) { measure ->
                                UnitOfMeasureItem(
                                    measure,
                                    width = width
                                )


                            }


                        }

                    }

                }

            }
        }

    }
}

@Composable
fun UOMInputDialog(onSave: (Product.UOM) -> Unit, onClose: () -> Unit) {
    var code by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var conversionFactor by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var upc by remember { mutableStateOf("") }
    var fractionalQuantities by remember { mutableStateOf(false) }
    var ifSell by remember { mutableStateOf(false) }
    var ifBuy by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onClose) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(text = "Enter UOM Details", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Code") }
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                TextField(
                    value = conversionFactor,
                    onValueChange = { conversionFactor = it },
                    label = { Text("Conversion Factor") }
                )
                TextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight") }
                )
                TextField(
                    value = upc,
                    onValueChange = { upc = it },
                    label = { Text("UPC") }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = fractionalQuantities,
                        onCheckedChange = { fractionalQuantities = it }
                    )
                    Text("Fractional Quantities")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = ifSell,
                        onCheckedChange = { ifSell = it }
                    )
                    Text("Sell")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = ifBuy,
                        onCheckedChange = { ifBuy = it }
                    )
                    Text("Buy")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onClose) {
                        Text(text = "Cancel")
                    }
                    TextButton(onClick = {
                        val newUOM = Product.UOM(
                            code = code,
                            description = description,
                            conversionFactor = conversionFactor,
                            weight = weight,
                            upc = upc,
                            fractionalQuantities = fractionalQuantities,
                            ifSell = ifSell,
                            ifBuy = ifBuy
                        )
                        onSave(newUOM)
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}



@Composable
fun UOMDialog(uom: Product.UOM, onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(text = "UOM Details", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Code: ${uom.code}")
                Text(text = "Description: ${uom.description}")
                Text(text = "Conversion Factor: ${uom.conversionFactor}")
                Text(text = "Weight: ${uom.weight}")
                Text(text = "UPC: ${uom.upc}")
                Text(text = "Fractional Quantities: ${if (uom.fractionalQuantities) "Yes" else "No"}")
                Text(text = "Sell: ${if (uom.ifSell) "Yes" else "No"}")
                Text(text = "Buy: ${if (uom.ifBuy) "Yes" else "No"}")

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onClose) {
                        Text(text = "Close")
                    }
                    TextButton(onClick = { /* Edit functionality */ }) {
                        Text(text = "Edit")
                    }
                    Button(onClick = { /* Confirm functionality */ }) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}


@Composable
fun UnitOfMeasureItem(measure: Product.UOM, width: Dp) {

    var showViewDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            modifier = Modifier
                .width(width)
                .padding(start = 10.dp),
            text = measure.code,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            modifier = Modifier
                .width(width)
                .padding(start = 10.dp),
            text = measure.description,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            modifier = Modifier
                .width(width)
                .padding(start = 10.dp),
            text = measure.conversionFactor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )

        IconButton(onClick = { showViewDialog = !showViewDialog }) {
            Icon(
                imageVector = Icons.Rounded.Visibility,
                contentDescription = "Edit Icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        if (showViewDialog) {
            UOMDialog(uom = measure, onClose = { showViewDialog = false })
        }
    }
}




@Preview
@Composable
fun PreviewProductDisplay() {


    val uom1 = Product.UOM(
        code = "EA",
        description = "Each Quantity",
        conversionFactor = "1",
        weight = "1 kg",
        upc = "347365839234",
        fractionalQuantities = false,
        ifSell = true,
        ifBuy = true
    )

    val uom2 = Product.UOM(
        code = "MP",
        description = "Master Pack",
        conversionFactor = "12 per 1",
        weight = "20",
        upc = "347365833412",
        fractionalQuantities = true,
        ifSell = true,
        ifBuy = true
    )

    val quantities = mutableListOf(
        Product.Quantity(unit = "On Hand", value = "100"),
        Product.Quantity(unit = "Available", value = "80"),
        Product.Quantity(unit = "On Order", value = "20"),
        Product.Quantity(unit = "Committed", value = "10")
    )

    val uomList = mutableListOf(uom1, uom2)

    val sampleProduct = Product(
        title = "Milwaukee M28 Cordless 6-1/2\" Circular Saw Kit\n",
        sku = "MIL082020",
        price = "$99.99",
        uoms = uomList,
        quantities = quantities,
        primaryLocation = Location("WH101"),
        locations = mutableListOf(Location("WH101"), Location("WH202"))
    )
    val navController = rememberNavController()
    ProductDisplay(product = sampleProduct, navController = navController)

}