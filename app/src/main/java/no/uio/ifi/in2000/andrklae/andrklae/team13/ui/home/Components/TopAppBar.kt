package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.andrklae.andrklae.team13.R

@Composable
fun TopAppBar(){
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FavouriteButton()
        MapButton()
        Spacer(modifier = Modifier.weight(1f))
        SettingsButton()
    }
}

@Composable
fun FavouriteButton(){
    IconButton(
        onClick = { /* handle button click */ },
        modifier = Modifier
            .size(50.dp)
            .padding(5.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.favoritt),
            contentDescription = "Image Description",

            modifier = Modifier
                .size(40.dp)

        )
    }
}

@Composable
fun SettingsButton(){
    IconButton(
        onClick = { /* handle button click */ },
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = Color.Black
        )
    }
}

@Composable
fun MapButton(){
    IconButton(
        onClick = { /* handle button click */ },
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Settings",
            tint = Color.Black
        )
    }
}