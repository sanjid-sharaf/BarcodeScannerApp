package com.example.sacnnerui.ProductPage

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
            ExtendedFloatingActionButton(onClick = {
            },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
                icon  = { Icon(Icons.Filled.AddLocation, "Small floating action button.")},
                text = {Text(text = "Locate")}
            )
            }

        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background))

            {

                primaryLocation?.let {
                    Text(
                        text = "Primary Location: ${it.code}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                LocationList(
                    locations = product.locations,
                    onRemove = { LocationsViewModel.removeLocation(it) },
                    onSetAsPrimary = { LocationsViewModel.setAsPrimary(it) }
                )

            }



        }




        // Adding UnitsOfMeasureSection here
        UnitsOfMeasureSection(Product = product)
}



@Preview
@Composable
fun PreviewProductDisplay() {
    val sampleProduct = Product(
        title = "Milwaukee M28 Cordless 6-1/2\" Circular Saw Kit\n",
        sku = "MIL082020",
        price = "$99.99",
        location = "WH101A",
        EA = "1",
        MP = "12",
        onHand = "10",
        available = "4",
        committed = " 5",
        onOrder = "0",
        primaryLocation = Location("WH101"),
        locations = listOf(Location("WH101"), Location("WH202"))
    )
    val navController = rememberNavController()
    ProductDisplay(product = sampleProduct, navController = navController)

}


@Composable
fun ProductQuantityTable(product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSurfaceVariant)
    )
    {
        product.inventoryValues.forEach { quantity ->
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
        product.inventoryValues.forEach { quantity ->
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
        Button(onClick = onSetAsPrimary) {
            Text("Set as Primary")
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
    onRemove: (Location) -> Unit,
    onSetAsPrimary: (Location) -> Unit
) {
    LazyColumn {
        items(locations) { location ->
            LocationRow(
                location = location,
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

    var showDialog by remember { mutableStateOf(false) }

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
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                        .background(MaterialTheme.colorScheme.background)
                ) {

                    val boxWithConstraintsScope = this
                    val width = boxWithConstraintsScope.maxWidth / 3

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
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                modifier = Modifier.width(width),
                                text = "Quantity",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                modifier = Modifier.width(width),
                                text = "Edit",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn {
                            items(Product.measures) { measure ->
                                UnitOfMeasureItem(
                                    unit = measure.unit,
                                    quantity = measure.value,
                                    width = width,
                                    onEditClick =
                                    {
                                        showDialog = showDialog.not()
                                    }

                                )
                            }

                            }
                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text("Change UOM") },
                                text = { Text("This action cannot be undone") },
                                confirmButton = {
                                    TextButton(onClick = {  showDialog = false }) {
                                        Text("Save".uppercase())
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDialog = false }) {
                                        Text("Cancel".uppercase())
                                    }
                                },
                            )
                        }
                    }

                }

            }
        }

    }
}




@Composable
fun UnitOfMeasureItem(unit: String, quantity: String, width: Dp, onEditClick: () -> Unit) {
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
            text = unit,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            modifier = Modifier
                .width(width)
                .padding(start = 10.dp),
            text = quantity,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = "Edit Icon",
                tint = MaterialTheme.colorScheme.onBackground
            )


        }
    }
}

