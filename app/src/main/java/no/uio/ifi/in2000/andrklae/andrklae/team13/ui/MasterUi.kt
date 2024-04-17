package no.uio.ifi.in2000.andrklae.andrklae.team13.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings.BackgroundImage
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.FavoriteScreen
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.FavoriteViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings.SettingsScreen
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings.SettingsViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning.WarningScreen
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning.WarningViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherScreen
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MasterUi(
    activity: MainActivity,
    weatherVM: WeatherViewModel = WeatherViewModel(0, activity),
    favVM: FavoriteViewModel = FavoriteViewModel(),
    settingsVM: SettingsViewModel = SettingsViewModel(),
    warningVM: WarningViewModel = WarningViewModel()
) {
    val pagerState = rememberPagerState(
        pageCount = { 4 },
    )
    val background = settingsVM.background.collectAsState()
    Column(verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            beyondBoundsPageCount = 2,
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = background.value.gradientList
                    )
                )
                .weight(1f)
        ) {
            page ->
            when (page){
                0 -> WeatherScreen(weatherViewModel = weatherVM, background.value)
                1 -> FavoriteScreen(favVM, weatherVM, activity, pagerState)
                2 -> WarningScreen(warningVM)
                3 -> SettingsScreen(settingsVM)

            }

        }
        BottomAppBar(weatherVM, pagerState)
    }

}
