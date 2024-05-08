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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.YoutubeSearchedFor
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

val iconSize = 35.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomAppBar(pagerState: PagerState) {
    // container for each button
    Column(verticalArrangement = Arrangement.Bottom) {
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
            SearchButton(pagerState)
            Spacer(modifier = Modifier.weight(1f))
            WarningButton(pagerState)
            Spacer(modifier = Modifier.weight(1f))
            SettingsButton(pagerState)
            Spacer(modifier = Modifier.weight(0.5f))

        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeButton(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    // sets the icon according to whether its on that screen or another
    val icon = {
        if (pagerState.currentPage == 0) {
            Icons.Filled.Home
        } else Icons.Outlined.Home
    }
    IconButton(
        // scrolls to the screen clicked on
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
fun SearchButton(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    // sets the icon according to whether its on that screen or another
    var color = {
        if (pagerState.currentPage == 1) {
            Color.Black
        } else {
            Color.DarkGray
        }
    }
    val icon = Icons.Outlined.Search
    IconButton(
        // scrolls to the screen clicked on
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(1)
            }
        },
        modifier = Modifier
            .size(50.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Vær skjerm",
            modifier = Modifier.size(iconSize),
            tint = color()
        )

    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WarningButton(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    // sets the icon according to whether its on that screen or another
    val icon = {
        if (pagerState.currentPage == 2) {
            Icons.Default.Warning
        } else Icons.Outlined.WarningAmber
    }
    IconButton(
        // scrolls to the screen clicked on
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
fun SettingsButton(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    // sets the icon according to whether its on that screen or another
    val icon = {
        if (pagerState.currentPage == 3) {
            Icons.Filled.Settings
        } else Icons.Outlined.Settings
    }
    IconButton(
        // scrolls to the screen clicked on
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
            contentDescription = "Innstillinger skjerm",
            modifier = Modifier.size(iconSize)
        )

    }
}