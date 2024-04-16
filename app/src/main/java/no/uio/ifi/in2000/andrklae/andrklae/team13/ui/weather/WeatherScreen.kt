package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.Next24
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.RainWind
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.UpperHalf
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.WarningRow
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.WeekTable
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect


@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState"
)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel) {
    val data = weatherViewModel.data.observeAsState().value
    val scrollState = rememberScrollState()
    Box {
        // contents
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            // keeps track of the status of the data
            when (data!!.weatherStatus.value) {
                // loading
                data.statusStates[0] -> {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(70.dp))
                        Text(text = data.location.name, fontSize = 35.sp)
                        Spacer(modifier = Modifier.height(70.dp))
                        CircularProgressIndicator(color = Color.Black)
                    }

                }
                // success
                data.statusStates[1] -> {
                    UpperHalf(weatherViewModel, data)

                    if (data.alertStatus.value == data.statusStates[1]) {

                        WarningRow(data, 500)

                    }
                    Next24(weatherViewModel, data)

                    RainWind(data)
                    WeekTable(data)
                    Spacer(modifier = Modifier.height(20.dp))
                }
                // failed
                data.statusStates[2] -> {
                    // if it doesnt have any data loaded previously
                    if (data.weather == null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.height(80.dp))
                            Box (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .glassEffect()
                                    .padding(vertical = 20.dp)
                            )
                            {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .width(180.dp)
                                ) {
                                    Text(text = data.location.name, fontSize = 35.sp, textAlign = TextAlign.Center)
                                }
                            }
                            Spacer(modifier = Modifier.height(70.dp))

                            Spacer(modifier = Modifier.height(20.dp))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                .glassEffect()
                                .background(Color.Red.copy(alpha = 0.3f))
                                .padding(20.dp)
                            ) {
                                Text(text = "Feilet", fontSize = 25.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Sjekk internett tilkobling", fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(20.dp))
                                Box(
                                    modifier = Modifier
                                        .shadow(3.dp, RoundedCornerShape(16.dp))
                                        .padding(3.dp)
                                        .clip(RoundedCornerShape(13.dp))
                                        .background(Color.White)
                                        .clickable { weatherViewModel.updateAll() }
                                        .padding(10.dp)
                                ) {

                                    Text(
                                        text = "Last på nyt",
                                        fontSize = 20.sp,
                                    )
                                }
                            }

                        }

                    }
                    // if it has previous data
                    else {
                        UpperHalf(weatherViewModel, data)

                        WarningRow(data, 500)

                        Next24(weatherViewModel, data)

                        RainWind(data)

                        WeekTable(data)

                        Spacer(modifier = Modifier.height(20.dp))

                    }

                }
            }

        }
    }


}





