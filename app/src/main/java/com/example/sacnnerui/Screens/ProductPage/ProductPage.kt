package com.example.sacnnerui.Screens.ProductPage

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.apitesting.RetrofitInstance
import com.example.sacnnerui.R
import com.example.sacnnerui.ScanBroadcastReceiver
import com.example.sacnnerui.data.ProductsRepositoryImpl
import com.example.sacnnerui.data.Result
import com.example.sacnnerui.data.model.Location
import com.example.sacnnerui.data.model.Product
import com.example.sacnnerui.data.model.UnitsOfMeasure
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun GetProduct(productId : Int, navHostController: NavHostController) {
    println("productId is $productId")
    val repository = ProductsRepositoryImpl(RetrofitInstance.api)
    var product by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(productId) {
        repository.getProduct(productId).collectLatest { result ->
            when (result) {
                is Result.Error -> {  }
                is Result.Success -> {
                    result.data?.let { product = result.data
                    }
                }
            }
        }

    }
    product?.let {
        ProductDisplay(product = it, navController = navHostController)
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDisplay(product: Product, navController: NavHostController) {
    val factory = LocationsListViewModelFactory(product)
    val UOMViewModelFactory = UOMViewModelFactory(product)
    // Obtain the ViewModel instance using the factory
    val UOMViewModel: UOMViewModel = viewModel(factory = UOMViewModelFactory)
    val LocationsViewModel: LocationsListViewModel = viewModel(factory = factory)
    val locations by LocationsViewModel.locations.collectAsState()
    val primaryLocation by LocationsViewModel.primaryLocation.collectAsState()
    val addLocationDialog = remember { mutableStateOf(false) }
    var locationCode by remember { mutableStateOf("") }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        println("Products page Composed")
        val broadcastReceiver = ScanBroadcastReceiver { data ->
            // Update the state with the decoded data
            if (data != null) {
                locationCode = data
                println(locationCode)
            }
        }
        // Create an intent filter with categories and action
        val intentFilter = IntentFilter().apply {
            addCategory(Intent.CATEGORY_LAUNCHER) // Adding the category
            addAction(navController.context.getString(R.string.activity_intent_filter_action)) // Adding the action
        }
        // Register the receiver dynamically
        context.registerReceiver(broadcastReceiver, intentFilter)

        onDispose { println("product Page disposed")
            context.unregisterReceiver(broadcastReceiver) }
    }

    LaunchedEffect(locationCode){
        if (locationCode!="" && locationCode.all { it.isLetterOrDigit() })
        {
            LocationsViewModel.addLocation(Location(code = locationCode))
            println("loacton added : prodiuct locations  : ${product.locations}")
            locationCode = ""
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),

            Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                println("back button")
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Text(
                text = product.part_no.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(4.dp)
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = product.description.trim(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "$${String.format(Locale.US, "%.2f", product.price.toDouble())}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .padding(4.dp)
                    .background(Color.Black)
            )
            ProductQuantityTable(product = product)
        }
        Spacer(modifier = Modifier.height(8.dp))

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
                    onCancel = { addLocationDialog.value = false },
                    onConfirm = { addLocationDialog.value = false ; }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
        )
            {
                LocationList(
                    locations = locations,
                    primaryLocation = primaryLocation,
                    onRemove = {LocationsViewModel.removeLocation(it) },
                    onSetAsPrimary = {LocationsViewModel.setAsPrimary(it) }
                )
        }

    }
    // Adding UnitsOfMeasureSection here
    UnitsOfMeasureSection(UOMViewModel)
}


@Composable
fun ProductQuantityTable(product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSurfaceVariant)
    )
    {

        product.getQuantities().forEach { quantity ->
            Text(
                text = quantity.unit,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(4.dp)
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
        product.getQuantities().forEach { quantity ->
            Text(
                text = quantity.value,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(4.dp)
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
        verticalAlignment = Alignment.CenterVertically  // Align items vertically center
    ) {
        val locationCode = location.location_code
        val description = location.description

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = if (location == primaryLocation) FontWeight.ExtraBold else FontWeight.Medium)) {
                    append(locationCode)
                }
                append(" $description")
            },
            modifier = Modifier.weight(1f),
            fontSize = 15.sp,
            color = if (location == primaryLocation) Color.hsv(349F, 0.7F, 0.92F) else Color.Black
        )

        Spacer(modifier = Modifier.width(4.dp))

        Checkbox(
            checked = location == primaryLocation,
            onCheckedChange = { checked ->
                if (checked) {
                    onSetAsPrimary()
                }
            }
        )
        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Remove"
            )
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Set the height you want for the scrollable area
    ){
        LazyColumn (

        ){
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

}



@Composable
fun UnitsOfMeasureSection(UOMViewModel: UOMViewModel) {
    var isVisible by remember {
        mutableStateOf(false)
    }
    var iconState by remember {
        mutableStateOf(Icons.Rounded.KeyboardArrowUp)
    }

    var showAddUomDialog by remember { mutableStateOf(false) }

    val UOMS by UOMViewModel.uoms.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
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
                            UOMViewModel.addUoms(uom)
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
                                items(UOMS) { measure ->
                                UnitOfMeasureItem(
                                    measure,
                                    width = width,
                                    UOMViewModel
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
fun UOMInputDialog(onSave: (UnitsOfMeasure) -> Unit, onClose: () -> Unit) {
    var code by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var quantityFactor by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var upc by remember { mutableStateOf("") }
    var upcList by remember { mutableStateOf(listOf<String>()) } // List to store UPCs
    var allowFractionalQty by remember { mutableStateOf(false) }
    var buy by remember { mutableStateOf(true) }
    var sell by remember { mutableStateOf(true) }
    var priceFactor by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onClose) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            ){
                Text(text = "Enter UOM Details", style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "closeButton",
                        tint = MaterialTheme.colorScheme.onBackground )
                }
            }
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {


                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Code") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                TextField(
                    value = quantityFactor,
                    onValueChange = { quantityFactor = it ; priceFactor = it},
                    label = { Text("Quantity Factor") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                TextField(
                    value = priceFactor,
                    onValueChange = { priceFactor = it },
                    label = { Text("Price Factor") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                TextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (lbs)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                TextField(
                    value = upc,
                    onValueChange = { upc = it },
                    label = { Text("Add UPC") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            upcList = upcList + upc // Add UPC to the list
                            upc = "" // Clear the UPC field
                        }
                    )

                )

                Spacer(modifier = Modifier.height(8.dp))

                // Display list of UPCs
                Text(text = "UPCs")
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))

                ) {
                    items(upcList) { upc ->
                        Text(
                            text = upc,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)

                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = allowFractionalQty,
                        onCheckedChange = { allowFractionalQty = it }
                    )
                    Text("Allow Fractional Quantities")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = buy,
                        onCheckedChange = { buy  = it }
                    )
                    Text("Set as Buy UOM")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = sell,
                        onCheckedChange = { sell = it }
                    )
                    Text("Set as Sell UOM")
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onClose) {
                        Text(text = "Cancel")
                    }
                    TextButton(onClick = {
                        val newUOM = UnitsOfMeasure(
                            upcs = upcList,
                            allow_fractional_qty = allowFractionalQty,
                            code = code,
                            description = description,
                            price_factor = priceFactor.toString(),
                            qty_factor = quantityFactor,
                            weight = weight,
                            buy_uom = buy,
                            sell_uom = sell,
                            whse = "00"
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
fun UnitOfMeasureItem(measure: UnitsOfMeasure, width: Dp, UOMViewModel: UOMViewModel) {

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
            text = measure.uom,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            modifier = Modifier
                .width(width)
                .padding(start = 10.dp),
            text = measure.description ?: "", // Use Elvis operator to provide an empty string if null
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            modifier = Modifier
                .width(width)
                .padding(start = 10.dp),
            text = ("%.2f".format(measure.qty_factor.toDouble())),
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
            UOMDialog(uom = measure, onClose = { showViewDialog = false}, UOMViewModel )
        }
    }
}


@Composable
fun UOMDialog(uom: UnitsOfMeasure, onClose: () -> Unit, UOMViewModel: UOMViewModel) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember{ mutableStateOf(false)}
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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(text = "UOM Details", style = MaterialTheme.typography.headlineSmall)

                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = "closeButton",
                            tint = MaterialTheme.colorScheme.onBackground )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Code: ${uom.uom}")
                Text(text = "Description: ${uom.description}")
                Text(text = "QuantityFactor: %.2f".format(uom.qty_factor.toDouble()))
                Text(text = "Weight (lbs) : %.2f" .format(uom.weight.toDouble()))
                Text(text = "UPC: ${uom.upcs.joinToString(separator = ", ")}")
                Text(text = "Fractional Quantities: ${if (uom.allow_fractional_qty) "Yes" else "No"}")
                Text(text = "Buy: ${if (uom.buy_uom) "Yes" else "No"}")
                Text(text = "Sell: ${if (uom.sell_uom) "Yes" else "No"}")

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(onClick = { showDeleteDialog = !showDeleteDialog}) {
                        Text(text = "Delete")
                    }
                    Button(onClick = {showEditDialog = !showEditDialog }) {
                        Text(text = "Edit")
                    }

                }

                if (showDeleteDialog){
                    ConfirmDeleteDialog(
                        onCancel = {  showDeleteDialog = false},
                        onConfirm = {UOMViewModel.removeUoms(uom)}
                    )
                }

                if (showEditDialog) {
                    UOMEditDialog(uom = uom, onSave = {newUOM ->
                        UOMViewModel.updateUom(uom,newUOM)
                        showEditDialog = false
                    }  , onClose = { showEditDialog = false})
                }
            }
        }
    }
}



@Composable
fun UOMEditDialog(uom: UnitsOfMeasure,onSave: (UnitsOfMeasure) -> Unit, onClose: () -> Unit) {
    var code by remember { mutableStateOf(uom.uom) }
    var description by remember { mutableStateOf(uom.description) }
    var quantityFactor by remember { mutableStateOf(uom.qty_factor) }
    var priceFactor by remember { mutableStateOf(uom.qty_factor) }
    var weight by remember { mutableStateOf(uom.weight) }
    var upc by remember { mutableStateOf("") }
    var allowFractionalQty by remember { mutableStateOf(uom.allow_fractional_qty) }
    var buy by remember { mutableStateOf(uom.buy_uom) }
    var sell by remember { mutableStateOf(uom.sell_uom)}
    var upcList by remember { mutableStateOf(uom.upcs) }

    Dialog(onDismissRequest = onClose) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            ){
            Text(text = "Edit UOM Details", style = MaterialTheme.typography.headlineSmall)
            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "closeButton",
                    tint = MaterialTheme.colorScheme.onBackground )
            }
        }
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {


                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Code") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                TextField(
                    value = description ?: "", // Shows an empty field
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                TextField(
                    value = quantityFactor.toString(),
                    onValueChange = { quantityFactorString: String ->
                        quantityFactor = quantityFactorString },
                    label = { Text("Quantity Factor") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                TextField(
                    value = priceFactor.toString(),
                    onValueChange = { pricef: String -> priceFactor = pricef },
                    label = { Text("Quantity Factor") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                TextField(
                    value = weight.toString(),
                    onValueChange = { newWeight: String ->
                        weight = newWeight// Parse String to Double
                    },
                    label = { Text("Weight (lbs)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                TextField(
                    value = upc,
                    onValueChange = { upc = it },
                    label = { Text("Add UPC") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            upcList = upcList + upc // Add UPC to the list
                            upc = "" // Clear the UPC field
                        }
                    )

                )

                Spacer(modifier = Modifier.height(8.dp))

                // Display list of UPCs
                Text(text = "UPCs")
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))

                ) {

                    items(upcList) { upc ->
                        Text(
                            text = upc,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = allowFractionalQty,
                        onCheckedChange = { allowFractionalQty = it }
                    )
                    Text("Allow Fractional Quantities")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = buy,
                        onCheckedChange = { buy = it }
                    )
                    Text("Buy")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = sell,
                        onCheckedChange = { sell = it }
                    )
                    Text("Sell")
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
                        val newUOM = UnitsOfMeasure(
                            upcs = upcList,
                            allow_fractional_qty = allowFractionalQty,
                            code = code,
                            description = description?: "",
                            price_factor = priceFactor,
                            qty_factor = quantityFactor,
                            weight = weight,
                            buy_uom = buy,
                            sell_uom = sell,
                            whse = "00"
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
fun ConfirmDeleteDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit)
{

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
                            text = "Delete UOM",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

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
                            onClick = { onConfirm()},
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

//@Preview
//@Composable
//fun PreviewProductDisplay() {
//
//
//    val uom1 = UOM(
//        UPC = "885911496681",
//        allowFractionalQty = true,
//        code = "EA",
//        description = "EA",
//        directFactor = true,
//        id = 1,
//        location = "WH101",
//        quantityFactor = 1.0,
//        self = "http://example.com/uom/1",
//        weight = 0.5
//    )
//
//    val uom2 = UOM(
//        UPC = "987654321098",
//        allowFractionalQty = false,
//        code = "MP",
//        description = "Master Pack",
//        directFactor = false,
//        id = 2,
//        location = "WH101",
//        quantityFactor = 10.0,
//        self = "http://example.com/uom/2",
//        weight = 0.98
//    )
//
//
//    val uomList = mutableListOf(uom1, uom2)
//
//    val product = Product(
//        availableQty =2,
//        averageCost = 50.0,
//        backorderQty = 0,
//        buyUOM = "EA",
//        committedQty = 0,
//        currentCost = 55.0,
//        description = "Dewalt SDS+ 1/2\" Carbide Hollow Core Bit\n",
//        groupNo = 1,
//        id = 12345,
//        links = Links(
//            components = "http://example.com/api/components/2",
//            serials = "http://example.com/api/serials/2",
//            uoms = "http://example.com/api/uoms/2",
//            upcs = "http://example.com/api/upcs/2"
//        ),
//        nullUPCs = listOf(),
//        onHandQty = 2,
//        onOrderQty = 0,
//        packSize = 1,
//        partNumber = "DEWDWA54012",
//        primaryVendor = "Dewalt",
//        productCode = "DEWDWA54012",
//        sellUOM = "Ea",
//        sellingPrice = "89.99",
//        status = "Active",
//        stockUOM = "Each",
//        unitsOfMeasure = uomList,
//        uploadToWeb = true,
//        warehouse = "Warehouse1",
//        weight = 2.5,
//        primaryLocation = Location("WH101"),
//        locations = mutableListOf(Location("WH101"), Location("WH202"))
//    )
//
//    val navController = rememberNavController()
//    ProductDisplay(product = product, navController = navController)
//
//}