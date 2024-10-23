package com.example.sacnnerui.Screens.LocationsPage

import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.AddLocation
import androidx.compose.material.icons.rounded.AddLocationAlt
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.LocationOff
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sacnnerui.R
import com.example.sacnnerui.ScanBroadcastReceiver
import com.example.sacnnerui.Screens.ProductsScreen.SearchBox
import com.example.sacnnerui.Screens.Screen

data class BottomNavigation(
    val title: String,
    val icon: ImageVector
)
@Composable
fun ViewAll(locationCode: String, navController: NavController) {
    // Sample product list with SKU and name
    val productList = mutableListOf(
        "MIL256220|Milwaukee M12 FUEL Stubby 3/8\" Impact Wrench - Tool Only",
        "MIL256221|Milwaukee M12 FUEL Stubby 3/8\" Impact Wrench Kit",
        "MIL256222|Milwaukee M12 FUEL Stubby 3/8\" Impact Wrench Kit - 2 Battery",
        "MIL256320|Milwaukee M12 FUEL™ Stubby 1/2\" Impact Wrench - Tool Only",
        "MIL256322|Milwaukee M12 FUEL™ Stubby 1/2\" Impact Wrench Kit",
        "MIL2563P20|Milwaukee M12 FUEL™ Stubby 1/2\" Impact Wrench w/ Pin Detent - Tool Only",
        "MIL256921|Milwaukee M12 FUEL 3/8\" Extended Reach High Speed Ratchet Kit",
        "MIL283421HD|Milwaukee M18 FUEL™ 7-1/4” Circular Saw Kit",
        "MIL48112330|Milwaukee Heated Gear Power Source w/ App Control"
    )

    DisposableEffect(Unit) {
        println("viewAll called")
        onDispose {
            println("viewAll disposed")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Row with Back Button and Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "All Products",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Location Code Display
        Text(
            text = locationCode,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Description Text
        Text(
            text = "Products in this location.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Product List Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .heightIn(max = 350.dp)// Box border
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(productList) { index, product ->
                    // Split SKU and Product Name
                    val (sku, productName) = product.split("|")

                    val backgroundColor = if (index % 2 == 0) Color.White else Color.LightGray

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 1.dp) // Tighter padding
                            .background(backgroundColor),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(4.dp)) // Reduced space
                        Text(
                            text = sku, // Display SKU and product name
                            fontSize = 12.sp, // Smaller font size for more data visibility
                            modifier = Modifier.weight(0.5f),
                        )

                        Text(
                            text = productName, // Display SKU and product name
                            fontSize = 12.sp, // Smaller font size for more data visibility
                            modifier = Modifier.weight(1f),
                        )

                        IconButton(onClick = {
                            productList.removeAt(index)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close, // Close icon for delete
                                contentDescription = "Delete",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp) // Slightly larger icon size
                            )
                        }
                    }
                    Divider(color = Color.Gray, thickness = 1.dp) // Add a divider between rows
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Pass selectedIndex and a lambda to update it to the BottomNavigationBar
        BottomNavigationBar(selectedIndex = 0, navController = navController, locationCode = locationCode)
    }
}

/****************************************Mass Locate Screen************************************************/
@Composable
fun MassLocateScreen(locationCode: String, navController: NavController) {
    var decodedData by remember { mutableStateOf("") }
    val context = LocalContext.current
    val productList = remember { mutableStateListOf<String>() }

    DisposableEffect(Unit) {
        println("Mass Locate called")

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

        onDispose {
            println("Mass Locate disposed")
            context.unregisterReceiver(broadcastReceiver)
        }
    }

    LaunchedEffect(decodedData) {
        if(decodedData.isNotEmpty() && decodedData.all { it.isLetterOrDigit() } && !productList.contains(decodedData))
        {
            productList.add(decodedData)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Row with Back Button and Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Add Location",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Display Location Code
        Text(
            text = locationCode,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Card with search text and box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF1F0F0) // Set your desired color here
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Row to contain the Icon and both Texts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly // Align items to the start
                ) {
                    Spacer(modifier = Modifier.width(20.dp)) // Space between icon and text

                    Icon(
                        imageVector = Icons.Rounded.AddLocationAlt,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp)) // Space between icon and text

                    // Column to hold the two text items
                    Column(
                        modifier = Modifier.weight(1f) // Take remaining space
                    ) {
                        Text(
                            text = "Search Or Scan Products",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "To add product to this location",
                            fontSize = 12.sp,
                            color = Color.Gray, // Use a lighter color for smaller text
                            modifier = Modifier.padding(top = 4.dp) // Add some space above
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBox(
                        onSearch = { query ->
                                   if(!productList.contains(query)) { productList.add(query) }
                        },
                        text = "Search"
                    )
                }
            }
        }

        Text(
            text = "Added Products : ",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(8.dp) // Add some padding
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Box border
        ) {


            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(productList) { index, product ->
                    // Split SKU and Product Name

                    val backgroundColor = if (index % 2 == 0) Color.White else Color.LightGray

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 1.dp) // Tighter padding
                            .background(backgroundColor)
                            .height(30.dp), // Fixed height
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(4.dp)) // Reduced space
                        Text(
                            text = product, // Display SKU and product name
                            fontSize = 12.sp, // Smaller font size for more data visibility
                            modifier = Modifier.weight(1f),
                            maxLines = 1, // Limit to one line
                            overflow = TextOverflow.Ellipsis // Show ellipsis if the text overflows
                        )
                        IconButton(onClick = {
                            productList.removeAt(index)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close, // Close icon for delete
                                contentDescription = "Delete",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp) // Slightly larger icon size
                            )
                        }
                    }
                    Divider(color = Color.Gray, thickness = 1.dp) // Add a divider between rows
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Pass selectedIndex and a lambda to update it to the BottomNavigationBar
        BottomNavigationBar(selectedIndex = 1, navController = navController, locationCode = locationCode)
    }
}

/**************************************MASS ASSIGN PRIMARY****************************************/
// Mockup for Mass Assign Primary Screen
@Composable
fun MassAssignPrimaryScreen(locationCode: String, navController: NavController) {
    var decodedData by remember { mutableStateOf("") }
    val context = LocalContext.current
    val productList = remember { mutableStateListOf<String>() }

    DisposableEffect(Unit) {
        println("Mass Assign called")

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

        onDispose {
            println("Mass Assign disposed")
            context.unregisterReceiver(broadcastReceiver)
        }
    }

    LaunchedEffect(decodedData) {
        if(decodedData.isNotEmpty() && decodedData.all { it.isLetterOrDigit() } && !productList.contains(decodedData))
        {
            productList.add(decodedData)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Row with Back Button and Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Assign Primary Location",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Display Location Code
        Text(
            text = locationCode,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Card with search text and box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF1F0F0) // Set your desired color here
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Row to contain the Icon and both Texts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly // Align items to the start
                ) {
                    Spacer(modifier = Modifier.width(20.dp)) // Space between icon and text

                    Icon(
                        imageVector = Icons.Rounded.AddLocationAlt,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp)) // Space between icon and text

                    // Column to hold the two text items
                    Column(
                        modifier = Modifier.weight(1f) // Take remaining space
                    ) {
                        Text(
                            text = "Search Or Scan Products",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "To assign primary location to the product",
                            fontSize = 12.sp,
                            color = Color.Gray, // Use a lighter color for smaller text
                            modifier = Modifier.padding(top = 4.dp) // Add some space above
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBox(
                        onSearch = { query ->
                            if(!productList.contains(query)) { productList.add(query) }
                        },
                        text = "Search"
                    )
                }
            }
        }

        Text(
            text = "Added Products : ",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(8.dp) // Add some padding
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Box border
        ) {


            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(productList) { index, product ->
                    // Split SKU and Product Name

                    val backgroundColor = if (index % 2 == 0) Color.White else Color.LightGray

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 1.dp) // Tighter padding
                            .background(backgroundColor),

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(4.dp)) // Reduced space
                        Text(
                            text = product, // Display SKU and product name
                            fontSize = 12.sp, // Smaller font size for more data visibility
                            modifier = Modifier.weight(1f),
                            maxLines = 1, // Limit to one line
                            overflow = TextOverflow.Ellipsis // Show ellipsis if the text overflows
                        )
                        IconButton(onClick = {
                            productList.removeAt(index)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close, // Close icon for delete
                                contentDescription = "Delete",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp) // Slightly larger icon size
                            )
                        }
                    }
                    Divider(color = Color.Gray, thickness = 1.dp) // Add a divider between rows
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Pass selectedIndex and a lambda to update it to the BottomNavigationBar
        BottomNavigationBar(selectedIndex = 2, navController = navController, locationCode = locationCode)
    }

}

/**********************************MASS DELOCATE**************************************************/

// Mockup for Mass Delocate Screen
@Composable
fun MassDelocateScreen(locationCode: String, navController: NavController) {
    var decodedData by remember { mutableStateOf("") }
    val context = LocalContext.current
    val productList = remember { mutableStateListOf<String>() }

    DisposableEffect(Unit) {
        println("Mass Delocate called")

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

        onDispose {
            println("Mass Delocate disposed")
            context.unregisterReceiver(broadcastReceiver)
        }
    }

    LaunchedEffect(decodedData) {
        if(decodedData.isNotEmpty() && decodedData.all { it.isLetterOrDigit() } && !productList.contains(decodedData))
        {
            productList.add(decodedData)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Row with Back Button and Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Delocate",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Display Location Code
        Text(
            text = locationCode,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Card with search text and box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF1F0F0) // Set your desired color here
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Row to contain the Icon and both Texts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly // Align items to the start
                ) {
                    Spacer(modifier = Modifier.width(20.dp)) // Space between icon and text

                    Icon(
                        imageVector = Icons.Rounded.AddLocationAlt,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp)) // Space between icon and text

                    // Column to hold the two text items
                    Column(
                        modifier = Modifier.weight(1f) // Take remaining space
                    ) {
                        Text(
                            text = "Search Or Scan Products",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "To Remove product from this location",
                            fontSize = 12.sp,
                            color = Color.Gray, // Use a lighter color for smaller text
                            modifier = Modifier.padding(top = 4.dp) // Add some space above
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBox(
                        onSearch = { query ->
                            if(!productList.contains(query)) { productList.add(query) }
                        },
                        text = "Search"
                    )
                }
            }
        }

        Text(
            text = "Removed Products : ",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(8.dp) // Add some padding
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Box border
        ) {


            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(productList) { index, product ->
                    // Split SKU and Product Name

                    val backgroundColor = if (index % 2 == 0) Color.White else Color.LightGray

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 1.dp) // Tighter padding
                            .background(backgroundColor)
                            .height(30.dp), // Fixed height
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(4.dp)) // Reduced space
                        Text(
                            text = product, // Display SKU and product name
                            fontSize = 12.sp, // Smaller font size for more data visibility
                            modifier = Modifier.weight(1f),
                            maxLines = 1, // Limit to one line
                            overflow = TextOverflow.Ellipsis // Show ellipsis if the text overflows
                        )
                        IconButton(onClick = {
                            productList.removeAt(index)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close, // Close icon for delete
                                contentDescription = "Delete",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp) // Slightly larger icon size
                            )
                        }
                    }
                    Divider(color = Color.Gray, thickness = 1.dp) // Add a divider between rows
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Pass selectedIndex and a lambda to update it to the BottomNavigationBar
        BottomNavigationBar(selectedIndex = 3, navController = navController, locationCode = locationCode)
    }
}


@Composable
fun BottomNavigationBar(selectedIndex: Int, navController: NavController, locationCode: String) {
    val items = listOf(
        BottomNavigation("View All", Icons.Rounded.Search),
        BottomNavigation("Add Location", Icons.Rounded.AddLocation),
        BottomNavigation("Assign Primary", Icons.Rounded.AddLocationAlt),
        BottomNavigation("Delocate", Icons.Rounded.LocationOff),
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .border(1.dp, Color.Black) // Outer border for the entire NavigationBar
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (selectedIndex == index) Color(0xFFdddddd) else Color(0xFFf1f1f1)
                        )
                ) {
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            // Construct the route based on the selected index
                            val tabName = when (index) {
                                0 -> "viewAll"
                                1 -> "massLocate"
                                2 -> "massAssignPrimary"
                                3 -> "massDelocate"
                                else -> "viewAll" // Default case
                            }
                            // Navigate to the desired screen using the navController
                            navController.navigate("locations/$locationCode/$tabName") {
                                popUpTo(route = Screen.Location.rout)
                            }
                        },
                        icon = {
                            Icon(item.icon, contentDescription = item.title)
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        },
                        alwaysShowLabel = true,
                        modifier = Modifier
                            .weight(1f)
                    )

                    // Draw vertical divider except for the last item
                    if (index < items.size - 1) {
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(Color.Black)
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "LocationPagePreview", device = Devices.NEXUS_5)
@Composable
fun LocationPagePreview() {
    MaterialTheme {
        ViewAll(locationCode = "WH101", navController = rememberNavController())
    }
}