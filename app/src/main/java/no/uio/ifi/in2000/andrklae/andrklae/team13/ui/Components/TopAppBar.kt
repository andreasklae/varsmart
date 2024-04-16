package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.AllPolygons
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomAppBar(weatherVM: WeatherViewModel, pagerState: PagerState) {
    Column(modifier = Modifier.fillMaxHeight()){
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.Yellow.copy(alpha = 0.7f),
                            Color.Red.copy(alpha = 0.7f),
                            Color.Blue.copy(alpha = 0.7f)
                        )
                    )
                )
        ) {
            FavouriteButton(pagerState)
            Spacer(modifier = Modifier.weight(1f))
            MapButton(weatherVM)
            Spacer(modifier = Modifier.weight(1f))
            SettingsButton()
        }
    }



}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavouriteButton(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    IconButton(
        onClick = {
            coroutineScope.launch {
                if (pagerState.currentPage == 0) {
                    pagerState.animateScrollToPage(1)
                } else {
                    pagerState.animateScrollToPage(0)
                }
            }
        },
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
fun SettingsButton() {
    var showDialog by remember { mutableStateOf(false) }
    IconButton(
        onClick = { showDialog = true },
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = Color.Black
        )
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.9f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "empty dialog")
                }
            }
        }
    }
}

@Composable
fun MapButton(vm: WeatherViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val data = vm.data.observeAsState().value
    val thing = data?.alertList
    IconButton(
        onClick = { showDialog = true },
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Settings",
            tint = Color.Black
        )
    }
    if (showDialog && thing != null) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.9f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AllPolygons(thing)
                }
            }
        }
    }
}