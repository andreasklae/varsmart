package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components.DrawSymbol
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components.ImageIcon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components.SettingsButton
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.Team13Theme
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

//A data class for dummy data
data class Favorite(
    val weatherIcon: String?,
    val location: String,
    val temp: Double?,
    val description: String
)


@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = viewModel()
) {
    //var favorites = favoriteViewModel.favouritesUiState.favourites.values
    val favorites = remember { mutableStateOf(emptyList<DataHolder>()) }

    // Use LaunchedEffect to observe changes in FavoriteViewModel and update the favoritesList
    LaunchedEffect(favoriteViewModel.favouritesUiState) {
        favorites.value = favoriteViewModel.favouritesUiState.favourites.values.toList()
    }




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
                            SearchBarField(favoriteViewModel)

                        }
                    }
                    favorites.value.forEach(){
                        item {
                            FavoriteBoxSwipe(
                                location = it.location.name,
                                weatherIcon = it.currentWeather?.symbolName,
                                midDayTemp = it.currentWeather?.temperature
                                //description = it.
                            ) { /* handle button click */ }
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
        val vm = FavoriteViewModel()
        //SearchBarField(favoriteViewModel = vm)
    }

}

@Composable
fun FavoriteTopAppBar(){
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        SettingsButton()
    }
}




@Composable
fun FavoriteBox(location: String, weatherIcon: String?, midDayTemp: Double?, /*description: String,*/ onClick: () -> Unit) {

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
            midDayTemp = midDayTemp
            //description = description
        )
    }
}

@Composable
fun FavoriteBoxSwipe(
    location: String,
    weatherIcon: String?,
    midDayTemp: Double?,
    //description: String,
    onClick: () -> Unit
) {
    val offsetX = remember { mutableFloatStateOf(0f) }

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
            //description = description
        )
    }
}

@Composable
fun FavoriteForecast(
    location: String,
    weatherIcon: String?,
    midDayTemp: Double?,
    // description: String
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
    ){
        DrawSymbol(
            weatherIcon!!,
            size = 80.dp,
            modifier = Modifier
                .offset(x = -2.dp, y = -3.dp)
        )

        Spacer(modifier = Modifier.padding(20.dp))
        Column(
            modifier = Modifier
                .width(180.dp)
        ) {
            Text(
                text = location,
                fontSize = 24.sp,

                )
            /*Text(
                text = description,
                fontSize = 15.sp ) */


        }

        Column(
            modifier = Modifier
                .width(120.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = midDayTemp.toString(),
                fontSize = 24.sp )
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
fun SearchBarField(
    favoriteViewModel: FavoriteViewModel
) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }


    SearchBar(
        modifier = Modifier
            .width(380.dp)
            .height(350.dp)
            .clip(RoundedCornerShape(15.dp)),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            favoriteViewModel.loadSearch(text)
            active = true

            println("trykket sok")

        },
        active = active,
        //remember to change functionality
        onActiveChange = {
            active = it
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
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon"
                )
            }
        }
    ) {

        LazyColumn {

            favoriteViewModel.locationsUiState?.forEach { location ->
                item {
                    Box(modifier = Modifier.clickable {
                        active = false
                        favoriteViewModel.loadFavourites(location)
                    }) {
                        //updates the list
                        /*LaunchedEffect(location){
                            favoriteViewModel.loadFavourites(location)
                        }*/

                        Text(
                            text = location.name + location.lat,
                            modifier = Modifier.padding(
                                start = 8.dp,
                                top = 4.dp,
                                end = 8.dp,
                                bottom = 4.dp
                            )
                        )
                    }


                }
            }


        }
    }
}






