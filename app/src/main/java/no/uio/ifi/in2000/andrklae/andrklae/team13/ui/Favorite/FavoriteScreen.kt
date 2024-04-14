package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.DrawSymbol
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.glassEffect
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

//A data class for dummy data
data class Favorite(
    val weatherIcon: Int,
    val location: String,
    val temp: String,
    val description: String
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    favVM: FavoriteViewModel,
    weatherVM: WeatherViewModel,
    activity: MainActivity,
    pagerState: PagerState
)
{
    val favorites = DataHolder.Favourites.sortedBy { if (it.location.name.equals("Min posisjon")) 0 else 1 }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )

            )

    ) {
        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
        favorites.forEach {
            item {
                FavoriteBox(favVM, weatherVM, it, pagerState)
            }
        }

        item {
            // adding new location button
            ElevatedButton(
                onClick = {
                    favVM.toggleBottomSheet()
                },
                shape = CircleShape,
                modifier = Modifier
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(Color.Transparent)

            ) {
                Icon(Icons.Filled.Add,"legg til posisjon")
            }
            Spacer(modifier = Modifier.height(12.dp))

            val showBottomSheet by favVM.showBottomSheet.collectAsState()

            if (showBottomSheet){
                BottomSheet(favVM, activity)
            }
            val showDialog by favVM.showSearch.collectAsState()
            if (showDialog){
                SearchDialog(favVM)
            }

        }
        item {
            Spacer(modifier = Modifier.fillMaxHeight())
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(favVM: FavoriteViewModel, activity: MainActivity) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { favVM.toggleBottomSheet() },
        sheetState = sheetState,
    ) {
        Column(
            Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            // header
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Legg til sted",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))

            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            // current location
            if (!DataHolder.Favourites.any{it.location.name == "Min posisjon"}){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .clickable {
                            activity.getCurrentLocation()
                            favVM.toggleBottomSheet()
                        }
                        .padding(15.dp)


                ) {
                    Icon(Icons.Filled.Place,"legg til posisjon")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "Nåværende posisjon",
                        fontSize = 20.sp
                    )

                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
                    .clickable {
                        favVM.toggleBottomSheet()
                        favVM.toggleSearchDialog()
                    }
                    .padding(15.dp)


            ) {
                Icon(Icons.Filled.Search,"legg til posisjon")
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Søk",
                    fontSize = 20.sp
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDialog(favVM: FavoriteViewModel) {
    val searchStatus by favVM.searchStatus.collectAsState()
    Dialog(onDismissRequest = {
        favVM.toggleSearchDialog()
        favVM.emptySearchresults()
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.8f)
                    .clip(RoundedCornerShape(15.dp))
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .padding(top = 20.dp, bottom = 0.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                )
                {
                    SearchBox(favVM)
                    Spacer(modifier = Modifier.height(20.dp))
                    when (searchStatus){
                        // loading searches
                        favVM.statusStates[0] -> {
                            CircularProgressIndicator(color = Color.Black)
                        }
                        // found location(s)
                        favVM.statusStates[1] -> {
                            val scrollState = rememberScrollState()
                            Column (modifier = Modifier.verticalScroll(scrollState)) {
                                Surface {

                                }
                                SearchResults(favVM)
                            }

                        }
                        // found no location
                        favVM.statusStates[2] -> {
                            Text(text = "Fant ingen resultater")
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .wrapContentWidth()
                    .clickable { favVM.toggleBottomSheet() }
                    .padding(15.dp)

            ) {
                Icon(Icons.Filled.Close,"fjern fra favoritter")
            }
            Spacer(modifier = Modifier.weight(1f))

        }

    }
}

@Composable
fun SearchResults(favVM: FavoriteViewModel) {
    val searchText by favVM.searchText.collectAsState()
    val searchResults by favVM.searchResults.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        searchResults.forEach { location ->
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(15.dp)

            ){
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "${location.name}",
                        fontSize = 20.sp,
                    )
                    Text(
                        text = location.postSted + ", " + location.fylke,
                        fontSize = 10.sp,
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                // if location allready exists
                if (DataHolder.Favourites.any{it.location == location}){
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFFFE0E0))
                            .wrapContentWidth()
                            .clickable {
                                DataHolder.Favourites.remove(
                                    DataHolder.Favourites.find {
                                        it.location == location
                                    }
                                )
                            }
                            .padding(10.dp)
                    ) {
                        Icon(Icons.Filled.Close,"fjern fra favoritter")
                    }
                } else{
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE0FFE0))
                            .wrapContentWidth()
                            .clickable {
                                DataHolder(location)
                            }
                            .padding(10.dp)
                    ) {
                        Icon(Icons.Filled.Add,"legg til sted i favoritter")
                    }
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(favVM: FavoriteViewModel) {
    val searchText by favVM.searchText.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchText,
        onValueChange = { favVM.changeSearchText(it) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear Text",
                    modifier = Modifier.clickable {
                        favVM.changeSearchText("")
                        keyboardController?.hide()  // Optionally hide the keyboard when clearing text
                    }
                )
            }
        },
        placeholder = { Text("Søk...") },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            favVM.toggleSearching(false)
            keyboardController?.hide()  // Hide the keyboard when the user confirms the search
        })
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteBox(favVM: FavoriteViewModel, weatherVM: WeatherViewModel, data: DataHolder, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .height(120.dp)
            .glassEffect()
            .clickable {
                coroutineScope.launch {
                    weatherVM.setLocation(DataHolder.Favourites.indexOf(data))
                    pagerState.animateScrollToPage(1)
                }
            }
            .padding(horizontal = 5.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )

            )

    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ){
            val status = data.weatherStatus
            when (status.value){
                data.statusStates[0] -> {
                    Text(
                        text = data.location.name,
                        fontSize = 24.sp
                    )
                    CircularProgressIndicator(color = Color.Black)
                }

                data.statusStates[1] -> {
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

/*@Composable
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

}*/






