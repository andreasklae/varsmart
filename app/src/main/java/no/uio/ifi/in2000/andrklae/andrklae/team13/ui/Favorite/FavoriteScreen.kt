package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.ImageIcon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.settingsButton
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.Team13Theme

//A data class for dummy data
data class Favorite(
    val weatherIcon: Int,
    val location: String,
    val temp: String,
    val description: String
)

@Composable
fun FavoriteScreen() {
    val favorites = mutableListOf<Favorite>(
        Favorite(R.drawable.sunclouds, "Oslo","10°", "Over skyet"),
        Favorite(R.drawable.sunclouds, "Nice","27°", "Sol"),
        Favorite(R.drawable.sunclouds, "Cancun","30°", "Mye sol"),
        Favorite(R.drawable.heavyrain, "Rio di janairo","-7°", "Regn")
    )

    Team13Theme {
        Surface(color = MaterialTheme.colorScheme.background) {

            Scaffold(

            ) { innerPadding ->
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(innerPadding)

                ) {
                    item {
                        FavoriteTopAppBar()
                    }
                    favorites.forEach {
                        item {
                            FavoriteBox(
                                location = it.location,
                                weatherIcon = it.weatherIcon,
                                midDayTemp = it.temp,
                                description = it.description,
                                onClick={ /* handle button click */ }
                            )
                        }
                    }
                    item{
                        AddFavorite(onClick = { /* handle button click */ })
                    }

                }
            }

        }
    }


}

@Preview
@Composable
fun FavoritePreview() {
    Team13Theme {
        FavoriteScreen()
    }

}

@Composable
fun FavoriteTopAppBar(){
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        settingsButton()
    }
}




@Composable
fun FavoriteBox(location: String, weatherIcon: Int, midDayTemp: String, description: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .width(380.dp)
            .height(91.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White.copy(alpha = 0.5f))
            .clickable { onClick() }
    ){
        FavoriteForecast(
            location = location,
            weatherIcon = weatherIcon,
            midDayTemp = midDayTemp,
            description = description
        )
    }

}


@Composable
fun FavoriteForecast(location: String, weatherIcon: Int, midDayTemp: String, description: String){
    Row(
        modifier = Modifier
            .padding(8.dp)
    ){
        ImageIcon(y = -3, x = -2, symbolId = weatherIcon, 79, 81)

        Spacer(modifier = Modifier.padding(20.dp))
        Column(
            modifier = Modifier
                .width(180.dp)
        ) {
            Text(
                text = location,
                fontSize = 24.sp,

                )
            Text(
                text = description,
                fontSize = 15.sp
            )


        }

        Column(
            modifier = Modifier
                .width(120.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = midDayTemp,
                fontSize = 24.sp,

                )
        }
    }
}

@Composable
fun AddFavorite(onClick: () -> Unit){
    FloatingActionButton(onClick = { onClick()},
        modifier = Modifier
            .clip(RoundedCornerShape(100))
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }

}