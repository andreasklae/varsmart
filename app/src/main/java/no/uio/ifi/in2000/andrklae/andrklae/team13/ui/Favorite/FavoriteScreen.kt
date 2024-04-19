package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Status
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.ActionButton
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.DrawSymbol
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.fontSize
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Search.Search
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Search.SearchViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    favVM: FavoriteViewModel,
    searchVm: SearchViewModel,
    setHomeLocation: (DataHolder) -> Unit,
    navigateToHome: (Int) -> Unit,
    activity: MainActivity,
    pagerState: PagerState
) {
    // loads the list every time the ui changes (ie a new favourite is added)
    favVM.loadData()
    // sorts favorite list to put current location at the top of the list
    val favorites = DataHolder
        .Favourites
        .sortedBy {
            if (it.location.name.equals("Min posisjon")) 0 else 1
        }

    val showDialog by searchVm.showSearchDialog.collectAsState()
    // column of all favourites
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )

            )


    ) {
        // spacer

        Spacer(modifier = Modifier.height(30.dp))
        // Row for refreshing favourites and searching for new places
        FunctionRow(
            favVM,
            searchVm,
            showDialog,
            toggleDialog = {searchVm.toggleSearchDialog()},
            setLocation = {data -> setHomeLocation(data)},
            toggleBottomSheet = {favVM.toggleBottomSheet()},
            navigateToHome = {page: Int -> navigateToHome(page)}
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Favoritter:",
                fontSize = 30.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // boxes for each favourite
        favorites.forEach {
            FavoriteBox({ data -> setHomeLocation(data) }, it, pagerState)
            Spacer(modifier = Modifier.height(20.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))

        val showBottomSheet by favVM.showBottomSheet.collectAsState()

        if (showBottomSheet) {
            BottomSheet(favVM, activity, searchVm)
        }


        Spacer(modifier = Modifier.fillMaxHeight())

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FunctionRow(
    favVM: FavoriteViewModel,
    searchVm: SearchViewModel,
    showDialog: Boolean,
    toggleDialog: () -> Unit,
    toggleBottomSheet: () -> Unit,
    setLocation: (DataHolder) -> Unit,
    navigateToHome: (Int) -> Unit
) {
    if (showDialog){
        Search(
            searchVm = searchVm,
            toggleDialog = { toggleDialog() },
            functionToPerform = {data -> setLocation(data) },
            navigateToHome = {page: Int -> navigateToHome(page)}
        )
    }

    Row(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        ActionButton(
            icon = Icons.Filled.Refresh,
            onClick = {favVM.updateWeather()}
        )
        Spacer(modifier = Modifier.width(10.dp))

        ActionButton(
            icon = Icons.Filled.Search,
            onClick = {toggleBottomSheet()}
        )


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    favVM: FavoriteViewModel,
    activity: MainActivity,
    searchVm: SearchViewModel
) {
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

            // Give option to add current location if the user hasn't done it already
            if (!DataHolder.Favourites.any { it.location.name == "Min posisjon" }) {
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
                    Icon(Icons.Filled.Place, "legg til posisjon")
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
                        searchVm.toggleSearchDialog()
                    }
                    .padding(15.dp)


            ) {
                Icon(Icons.Filled.Search, "legg til posisjon")
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Søk",
                    fontSize = 20.sp
                )

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteBox(
    setHomeLocation: (DataHolder) -> Unit,
    data: DataHolder, pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .padding(horizontal = 20.dp)
            .height(120.dp)
            .glassEffect()
            .clickable {
                coroutineScope.launch {
                    setHomeLocation(data)
                    pagerState.animateScrollToPage(0)
                }
            }
            .padding(10.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )

            )

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            val status = data.weatherStatus
            when (status.value) {
                Status.LOADING -> {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = data.location.name,
                            fontSize = 30.sp
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.width(90.dp)
                    ) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                }

                Status.SUCCESS -> {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Spacer(modifier = Modifier.weight(2f))
                        Text(
                            text = data.location.name,
                            fontSize = 30.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Oppdatert ${data.lastUpdate.hour}:${data.lastUpdate.minute}",
                            fontSize = 15.sp,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.width(90.dp)
                    ) {
                        Column {
                            Spacer(modifier = Modifier.weight(1f))
                            DrawSymbol(symbol = data.currentWeather!!.symbolName, size = 90.dp)
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        Column {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = data.currentWeather!!.temperature.toString() + "°C",
                                fontSize = 12.sp,
                            )
                        }

                    }

                }

                Status.FAILED ->{
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Spacer(modifier = Modifier.weight(2f))
                        Text(
                            text = data.location.name,
                            fontSize = 30.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Feilet.\n" +
                                    "Sjekk internett",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.width(90.dp)
                    ) {
                        Column {
                            Spacer(modifier = Modifier.weight(1f))
                            val updateScope = rememberCoroutineScope()
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.InsertDriveFile,
                                    contentDescription = "Last på nytt",
                                    modifier = Modifier
                                        .size(70.dp)
                                )
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = "Last på nytt",
                                    modifier = Modifier
                                        .size(25.dp)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                }
            }

        }
    }
}






