import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sacnnerui.Screen
import com.example.sacnnerui.SearchBar.SearchViewModel
import com.example.sacnnerui.SearchBar.SearchWidgetState

@Composable
fun ProductsScreen(navController: NavHostController, viewModel: SearchViewModel = viewModel()) {
    val searchWidgetState = viewModel.searchWidgetState.value
    val searchTextState = viewModel.searchTextState.value
    val productDictionary = viewModel.productDictionary

    val filteredProducts = remember(searchTextState) {
        productDictionary.filter { (modelNumber, title) ->
            modelNumber.contains(searchTextState, ignoreCase = true) ||
                    title.contains(searchTextState, ignoreCase = true)
        }.values.toList()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        if (searchWidgetState == SearchWidgetState.OPENED) {
            SearchBar(
                text = searchTextState,
                onTextChange = { viewModel.updateSearchTextState(it) },
                onCloseClicked = { viewModel.updateSearchWidgetState(SearchWidgetState.CLOSED) },
                onSearchClicked = { query ->
                    viewModel.updateSearchTextState(query)
                }
            )

            // Display the filtered list of products only if search bar is active
            LazyColumn {
                items(filteredProducts.size) { index ->
                    Text(
                        text = filteredProducts[index],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    navController.navigate(Screen.Home.rout)
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
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable { viewModel.updateSearchWidgetState(SearchWidgetState.OPENED) }
                        .padding(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}




@Composable
fun SearchBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    color = Color.Black.copy(alpha = 0.5f),
                    text = "Search For Product"
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.labelSmall.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    if (text.isNotEmpty()) {
                        onTextChange("")
                    } else {
                        onCloseClicked()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close Icon",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            )
        )
    }
}



@Composable
@Preview
fun SearchAppBarPreview() {
    SearchBar(
        text = "",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {}
    )
}