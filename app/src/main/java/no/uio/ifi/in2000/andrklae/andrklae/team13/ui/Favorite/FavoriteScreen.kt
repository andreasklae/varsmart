package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.DrawSymbol
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.glassEffect
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.ImageIcon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.SettingsButton
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.Team13Theme
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

//A data class for dummy data
data class Favorite(
    val weatherIcon: Int,
    val location: String,
    val temp: String,
    val description: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteScreen(
    favVM: FavoriteViewModel,
    weatherVM: WeatherViewModel,
    activity: MainActivity,
    pagerState: PagerState
)
{
    val favorites = DataHolder.Favourites
    favorites.sortBy { if (it.location.name == "Min posisjon") 0 else 1 }
    var showSearchBar by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()

    ) {
        Spacer(modifier = Modifier.height(90.dp))

        if (showSearchBar) {
            //SearchBarField()
        }
        favorites.forEach {
            println(favorites.size)
            FavoriteBox(favVM, weatherVM, it, pagerState)
        }

        Button(
            onClick = { activity.getCurrentLocation() },
            shape = CircleShape,
            modifier = Modifier.glassEffect().clip(CircleShape),
            colors = ButtonDefaults.buttonColors(Color.Transparent)


        ) {
            Icon(Icons.Filled.Add,"legg til posisjon")
        }

        Spacer(modifier = Modifier.fillMaxHeight())

    }
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteBox(favVM: FavoriteViewModel, weatherVM: WeatherViewModel, data: DataHolder, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .glassEffect()
            .clickable {
                coroutineScope.launch {
                    weatherVM.setLocation(DataHolder.Favourites.indexOf(data))
                    pagerState.animateScrollToPage(1)
                }
            }
            .padding(horizontal = 5.dp)

    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ){
            val status by data.status
            when (status){
                favVM.statusStates[0] -> {
                    Text(
                        text = data.location.name,
                        fontSize = 24.sp
                    )
                    CircularProgressIndicator(color = Color.Black)
                }

                favVM.statusStates[1] -> {
                    DrawSymbol(symbol = data.currentWeather!!.symbolName, size = 100.dp )
                    Column{
                        Text(
                            text = data.location.name,
                            fontSize = 24.sp
                        )
                        Text(
                            text = data.currentWeather!!.symbolName.toString(),
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))


                    Text(
                        text = data.currentWeather!!.temperature.toString() + "°C",
                        fontSize = 24.sp,
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                }
            }

        }
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
            .glassEffect()
            .width(380.dp)
            .height(91.dp)
            //.clip(RoundedCornerShape(15.dp))
            //.background(Color.White.copy(alpha = 0.5f))
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
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
    ){
        ImageIcon(y = -3, x = -2, symbolId = weatherIcon, 79, 81)

        Spacer(modifier = Modifier.weight(0.1f))
        Column(
            modifier = Modifier

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
        Spacer(modifier = Modifier.weight(1f))


        Text(
            text = midDayTemp,
            fontSize = 24.sp,
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarField(
    favoriteViewModel: FavoriteViewModel
){
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }


    SearchBar(
        modifier = Modifier
            .width(380.dp)
            .height(350.dp)
            .clip(RoundedCornerShape(15.dp)),
        query = text,
        onQueryChange = {
            text= it
        },
        onSearch = {},
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
    ){
        /*LazyColumn {

            favoriteViewModel.locationsUiState?.forEach { location ->
                item {
                    Box(modifier = Modifier.clickable {
                        active=false
                        favoriteViewModel.loadFavourites(location)}){
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
        }*/
    }

}






