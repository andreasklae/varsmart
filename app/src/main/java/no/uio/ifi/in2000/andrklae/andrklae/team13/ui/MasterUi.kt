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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.FavoriteScreen
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.FavoriteViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Search.SearchViewModel
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


    // hoisted for low coupling
    val background = settingsVM.background.collectAsState()
    val age = settingsVM.age.collectAsState()
    val sliderPosition = settingsVM.sliderPosition.collectAsState()
    var hobbies = settingsVM.hobbies.collectAsState()

    val gptCurrent by weatherVM.GPTCurrent.collectAsState()
    val currentAnimation by weatherVM.GPTCurrentAnimation.collectAsState()
    val gpt24h by weatherVM.GPT24h.collectAsState()
    val gpt24hAnimation by weatherVM.GPT24hAnimation.collectAsState()
    val gptWeek by weatherVM.GPTWeek.collectAsState()
    val gptWeekAnimation by weatherVM.GPTWeekAnimation.collectAsState()

    val currentData by weatherVM.data.collectAsState()

    val selected by weatherVM.selectedWarning.collectAsState()

    val pagerState = rememberPagerState(
        pageCount = { 4 },
    )

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            beyondBoundsPageCount = 4,
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = background.value.gradientList
                    )
                )
                .weight(1f)
        ) { page ->
            val coroutine = rememberCoroutineScope()
            when (page) {
                0 -> {
                    WeatherScreen(
                        data = currentData,
                        background = background.value,
                        updateAll = { weatherVM.updateAll() },
                        age = age.value,
                        gptMain = gptCurrent,
                        mainAnimation = currentAnimation,
                        hobbies = hobbies.value,
                        updateMainGpt = { age, list -> weatherVM.updateGptCurrent(age, list) },
                        gpt24h = gpt24h,
                        gpt24hAnimation = gpt24hAnimation,
                        update24hGpt = { age -> weatherVM.updateGPT24h(age) },
                        gptWeek = gptWeek,
                        weekAnimation = gptWeekAnimation,
                        updateGptWeek = { age, list -> weatherVM.updateGptWeek(age, list) },
                        setPreview = { weatherVM.setPreview() },
                        resetPreview = { weatherVM.resetPreview() },
                        selected = selected
                    )
                }

                1 -> FavoriteScreen(
                    favVM,
                    SearchViewModel(),
                    setHomeLocation = { data -> weatherVM.setLocation(data) },
                    navigateToHome = {
                        coroutine.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    activity,
                    pagerState
                )

                2 -> WarningScreen(warningVM, activity)
                3 -> {
                    SettingsScreen(
                        age = age.value,
                        onAgeChange = { fraction -> settingsVM.changeAgeByFraction(fraction, activity) },
                        sliderPosition = sliderPosition.value,
                        onSliderChange = { newPosition ->
                            settingsVM.changeSliderPosition(
                                newPosition
                            )
                        },
                        hobbies = hobbies.value,
                        onAddHobby = { hobby -> settingsVM.addHobby(hobby, activity) },
                        onRemoveHobby = { hobby -> settingsVM.removeHobby(hobby, activity) },
                        background = background.value,
                        onBackgroundChange = { background ->
                            settingsVM.changeBackround(background, activity)
                        }
                    )
                }
            }

        }
        BottomAppBar(weatherVM, pagerState)
    }

}
