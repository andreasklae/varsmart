package no.uio.ifi.in2000.andrklae.andrklae.team13.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

val iconSize = 35.dp
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomAppBar(weatherVM: WeatherViewModel, pagerState: PagerState) {
    Column(verticalArrangement = Arrangement.Bottom){
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(Color.White)

        ) {
            Spacer(modifier = Modifier.weight(0.5f))
            HomeButton(pagerState)
            Spacer(modifier = Modifier.weight(1f))
            FavouriteButton(pagerState)
            Spacer(modifier = Modifier.weight(1f))
            WarningButton(pagerState)
            Spacer(modifier = Modifier.weight(1f))
            ProfileButton(pagerState)
            Spacer(modifier = Modifier.weight(0.5f))

        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeButton(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val icon = {
        if (pagerState.currentPage == 0) {
            Icons.Filled.Home
        }
        else Icons.Outlined.Home
    }
    IconButton(
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(0)
            }
        },
        modifier = Modifier
            .size(50.dp)
            .padding(5.dp)
    ) {
        Icon(
            imageVector = icon(),
            contentDescription = "Vær skjerm",
            modifier = Modifier.size(iconSize + 6.dp)
        )

    }
}
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavouriteButton(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val icon = {
        if (pagerState.currentPage == 1) {
            Icons.Filled.Bookmarks
        }
        else Icons.Outlined.Bookmarks
    }
    IconButton(
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(1)
            }
        },
        modifier = Modifier
            .size(50.dp)
    ) {
        Icon(
            imageVector = icon(),
            contentDescription = "Vær skjerm",
            modifier = Modifier.size(iconSize)
        )

    }
}
@SuppressLint("CoroutineCreationDuringComposition")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WarningButton(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val icon = {
        if (pagerState.currentPage == 2) {
            Icons.Default.Warning
        }
        else Icons.Outlined.WarningAmber
    }
    IconButton(
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(2)
            }
        },
        modifier = Modifier
            .size(50.dp)
    ) {
        Icon(
            imageVector = icon(),
            contentDescription = "Kart skjerm",
            modifier = Modifier.size(iconSize)
        )

    }
}
@SuppressLint("CoroutineCreationDuringComposition")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileButton(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val icon = {
        if (pagerState.currentPage == 3) {
            Icons.Filled.AccountCircle
        }
        else Icons.Outlined.AccountCircle
    }
    IconButton(
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(3)
            }
        },
        modifier = Modifier
            .size(50.dp)
    ) {
        Icon(
            imageVector = icon(),
            contentDescription = "Profil skjerm",
            modifier = Modifier.size(iconSize)
        )

    }
}