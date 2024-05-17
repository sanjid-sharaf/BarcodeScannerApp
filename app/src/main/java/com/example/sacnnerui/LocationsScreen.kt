package com.example.sacnnerui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


/**
 * @author Android Devs Academy (Ahmed Guedmioui)
 */

@Composable
fun LocationsScreen( navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),

    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement =  Arrangement.Start
        ){
            IconButton(onClick = {
                navController.navigate(Screen.Home.rout)
            })
            {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew, // Replace with your desired icon
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Locations",
                fontSize = 20.sp
            )

        }




    }
}