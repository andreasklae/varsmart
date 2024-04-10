package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.Next24
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.RainWindSun
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.TopAppBar
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.UpperHalf
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.WeekTable
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.status.ErrorScreen
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.Team13Theme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(homeVM: WeatherViewModel) {
    val wStatus by homeVM.wStatus.collectAsState()
    val sunStatus by homeVM.sunStatus.collectAsState()
    Team13Theme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold { innerPadding ->
                when {
                    wStatus == homeVM.statusStates[0]
                            ||
                            sunStatus == homeVM.statusStates[0] -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFFB3E5FC), // Light blue
                                            Color(0xFF01579B)  // Dark blue
                                        )
                                    )
                                )
                        ) {
                            CircularProgressIndicator()
                        }

                    }

                    wStatus == homeVM.statusStates[1]
                            &&
                    sunStatus == homeVM.statusStates[1] -> {
                        MainComponent(innerPadding = innerPadding, homeVM)
                        TopAppBar(homeVM)
                    }

                    else -> ErrorScreen(homeVM)

                }


            }
        }
    }
}

@Composable
fun MainComponent(innerPadding: PaddingValues, homeVM: WeatherViewModel){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .fillMaxHeight()
            .padding(0.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB3E5FC), // Light blue
                        Color(0xFF01579B)  // Dark blue
                    )
                )
            )
    ) {
        item {
            UpperHalf(homeVM)
        }

        item {
            Warning(homeVM, 40)
        }

        item {
            Next24(homeVM)
        }
        item {
            RainWindSun(homeVM)
        }
        item {
            WeekTable(homeVM)
        }
        item{
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}





