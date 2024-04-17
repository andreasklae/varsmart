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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
    weatherVM: WeatherViewModel,
    favVM: FavoriteViewModel,
    settingsVM: SettingsViewModel,
    warningVM: WarningViewModel
) {
    val pagerState = rememberPagerState(
        pageCount = { 4 },
    )

    // hoisted for high cohesion
    val background = settingsVM.background.collectAsState()
    val age = settingsVM.age.collectAsState()
    val sliderPosition = settingsVM.sliderPosition.collectAsState()
    var hobbies = settingsVM.hobbies.collectAsState()
    val gptMain by weatherVM.GPTMain.collectAsState()
    val gpt24h by weatherVM.GPTWeek.collectAsState()
    val currentData by weatherVM.data.collectAsState()

    println("test")
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
                0 -> {
                    WeatherScreen(
                        data = currentData,
                        background = background.value,
                        updateAll = { weatherVM.updateAll() },
                        age = age.value,
                        gptMain = gptMain,
                        hobbies = hobbies.value,
                        updateMainGpt = { age, list -> weatherVM.updateMainGpt(age, list) },
                        gpt24h = gpt24h,
                        update24hGpt = { age -> weatherVM.updateGPT24h(age) }
                    )
                }
                1 -> FavoriteScreen(favVM, weatherVM, activity, pagerState)
                2 -> WarningScreen(warningVM)
                3 -> {
                    SettingsScreen(
                        age = age.value,
                        onAgeChange = { fraction -> settingsVM.changeAgeByFraction(fraction) },
                        sliderPosition = sliderPosition.value,
                        onSliderChange = { newPosition ->
                            settingsVM.changeSliderPosition(
                                newPosition
                            )
                        },
                        hobbies = hobbies.value,
                        onAddHobby = { hobby -> settingsVM.addHobby(hobby) },
                        onRemoveHobby = { hobby -> settingsVM.removeHobby(hobby) },
                        background = background.value,
                        onBackgroundChange = { background ->
                            settingsVM.changeBackround(background)
                        }
                    )
                }
            }

        }
        BottomAppBar(weatherVM, pagerState)
    }

}
