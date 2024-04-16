package no.uio.ifi.in2000.andrklae.andrklae.team13.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.BottomAppBar
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.FavoriteScreen
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.FavoriteViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherScreen
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MasterUi(
    activity: MainActivity,
    weatherVM: WeatherViewModel = WeatherViewModel(0, activity),
    favVM: FavoriteViewModel = FavoriteViewModel()
) {
    val pagerState = rememberPagerState(
        pageCount = { 2 },
        initialPage = 1
    )
    HorizontalPager(
        state = pagerState,
        beyondBoundsPageCount = 2,
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB3E5FC), // Light blue
                        Color(0xFF01579B)  // Dark blue
                    )
                )
            )
    ) { page ->
        if (page == 0){
            FavoriteScreen(favVM, weatherVM, activity, pagerState)
        } else {
            WeatherScreen(weatherViewModel = weatherVM)
        }

    }
    BottomAppBar(weatherVM, pagerState)




}
