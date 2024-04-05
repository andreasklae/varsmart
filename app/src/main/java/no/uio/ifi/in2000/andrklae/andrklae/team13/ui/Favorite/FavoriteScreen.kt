package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.ImageIcon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.settingsButton
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.Team13Theme
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

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
    var showSearchBar by remember { mutableStateOf(false) }

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
                    item {
                        if (showSearchBar) {
                            SearchBarField()

                        }
                    }
                    favorites.forEach {
                        item {
                            FavoriteBoxSwipe(
                                location = it.location,
                                weatherIcon = it.weatherIcon,
                                midDayTemp = it.temp,
                                description = it.description,
                                onClick = { /* handle button click */ }
                            )
                        }
                    }

                    item {
                        AddFavorite(onClick = { showSearchBar = true }
                        )


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
fun FavoriteBoxSwipe(
    location: String,
    weatherIcon: Int,
    midDayTemp: String,
    description: String,
    onClick: () -> Unit
) {
    val offsetX = remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .width(380.dp)
            .height(91.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White.copy(alpha = 0.5f))
            .clickable { onClick() }
            .draggable(
                state = rememberDraggableState { delta ->
                    offsetX.value += delta
                },
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    if (offsetX.value.absoluteValue > 200) {
                        // If the user swipes more than 200 dp, remove the item
                        // You can define your own threshold here
                        // For simplicity, let's call the onClick callback
                        onClick()
                    } else {
                        // Otherwise, reset the offset
                        offsetX.value = 0f
                    }
                }
            )
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
    ) {
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarField(){
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier
            .width(380.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(15.dp)),
        query = text,
        onQueryChange = {
            text= it
        },
        onSearch = {
            active = false
        },
        active = active,
        //remember to change functionality
        onActiveChange = {
            active= it
        },
        placeholder = {
            Text(text = "Skriv stedsnavn")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if(text.isNotEmpty()){
                            text = ""
                        } else{
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon"
                )
            }
        }
    )

    {

    }
}


