package com.example.sacnnerui.Screens.ProductsScreen

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.apitesting.RetrofitInstance
import com.example.sacnnerui.R
import com.example.sacnnerui.ScanBroadcastReceiver
import com.example.sacnnerui.Screens.Screen
import com.example.sacnnerui.data.ProductsRepositoryImpl
import com.example.sacnnerui.data.model.Product


/**
 * todo make product navigation
 *
 * */
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ProductsScreen(navController: NavHostController) {

    // Create ViewModel Using viewmodel Factory
    val productsViewModel: ProductsViewModel = viewModel(
        factory = ProductsViewModelFactory(ProductsRepositoryImpl(RetrofitInstance.api)),
        key = "ProductsViewModelKey"
    )
    // @mutableState Modelno for SearchBox
    var modelNumber by remember { mutableStateOf("") }
    // @Product
    val productsState = productsViewModel.products.collectAsState()
    var productId by remember {
        mutableStateOf("")
    }

    var decodedData by remember { mutableStateOf("") }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        println("products Screen Composed")
        val broadcastReceiver = ScanBroadcastReceiver { data ->
            // Update the state with the decoded data
            if (data != null) {
                decodedData = data
                println(decodedData)
            }
        }
        // Create an intent filter with categories and action
        val intentFilter = IntentFilter().apply {
            addCategory(Intent.CATEGORY_LAUNCHER) // Adding the category
            addAction(navController.context.getString(R.string.activity_intent_filter_action)) // Adding the action
        }
        // Register the receiver dynamically
        context.registerReceiver(broadcastReceiver, intentFilter)

        onDispose { println("products Screen disposed")
            context.unregisterReceiver(broadcastReceiver) }
    }

    LaunchedEffect(decodedData){
        if (decodedData!="" && decodedData.isDigitsOnly())
        {
            navController.navigate("products/$decodedData")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        /*
        * Row For Back Button and headers
        * */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = {
                    navController.popBackStack()

            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Text(
                text = "Products",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        /*
        * Column holding SearchBox and Interline Logo
        * */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable._1380_interline_logo_rgb),
                contentDescription = "Interline Log",
                modifier = Modifier
                    .size(175.dp)
            )

            SearchBox(
                text = "Search For Products",
                /*
                * If query is not empty, Use Viewmodel to Make API CAll
                * -> updates the products List State
                * */
                onSearch = { query ->
                    modelNumber = query
                    if (query.isNotEmpty()) {
                        productsViewModel.fetchProductList(query)
                    }
                }

            )
            /**
             * Composes the Products List if not empty
             * if query returns a single product, product page is composed directly
             * */
            if (productsState.value.isNotEmpty()) {

                println("Products updated: ${productsState.value}")
                if (productsState.value.size != 1) {
                    ProductList(products = productsState.value) { Id ->
                        productId = Id.toString()
                    }
                } else {
                    productId = productsState.value.get(0).id.toString()
                }
            }
        }
    }
    LaunchedEffect(productId) {
        if (productId.isNotEmpty()) {
            println(productId)
            navController.navigate("products/$productId"){
                popUpTo(Screen.Products.rout) { inclusive = false }
            }
        }
    }
}

@Composable
fun SearchBox(
    onSearch: (String) -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    // Function to handle search action
    val performSearch = {
        if (searchText.isNotEmpty()) {
            onSearch(searchText)
            keyboardController?.hide()
        }
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            // Remove label by commenting it out
            // label = { Text(text) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            // Set smaller text size with textStyle
            textStyle = TextStyle(fontSize = 12.sp),  // Adjust the font size as needed
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    performSearch()
                }
            ),
            trailingIcon = {
                // Button to perform search action
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = performSearch) {
                        Icon(Icons.Default.ArrowCircleRight, contentDescription = "Search")
                    }
                }
            },
            placeholder = { Text(text, fontSize = 12.sp) } // Placeholder text with smaller font size
        )
    }
}



/**
 * Composable function that displays a list of products.
 *
 * This function takes a list of products and a callback function that will be invoked
 * when a product is clicked. It uses a `LazyColumn` to efficiently display the
 * list of products in a scrollable format.
 *
 * @param products The list of [Product] items to display in the list.
 * @param onProductClick A callback function that is invoked when a product is clicked.
 *                       The function takes the [Int] product ID as a parameter.
 */
@Composable
fun ProductList(
    products: List<Product>,
    onProductClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductItem(product = product, onClick = onProductClick)
        }
    }
}

/**
 * Composable function that represents a single product item.
 *
 * This function displays the product's part number and description in a clickable
 * box. When the item is clicked, the provided `onClick` callback is invoked with
 * the product's ID.
 *
 * @param product The [Product] item to display, containing the part number and description.
 * @param onClick A callback function that is invoked when the product item is clicked.
 *                It takes the [Int] product ID as a parameter.
 */
@Composable
fun ProductItem(
    product: Product,
    onClick: (Int) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick(product.id) }
            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(text =  product.part_no, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = product.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}




