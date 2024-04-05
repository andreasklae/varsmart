package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.glassEffect
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.HomeViewModel

@Composable
fun RainWindSun(homeVM: HomeViewModel){

    val fontSize = 20.sp

    val wStatus by homeVM.wStatus.collectAsState()
    val weather by homeVM.currentWeather.collectAsState()

    val sStatus by homeVM.sunStatus.collectAsState()
    val set by homeVM.set.collectAsState()
    val rise by homeVM.rise.collectAsState()

    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .glassEffect(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Rain",
                    fontSize = fontSize
                )

                when(wStatus){
                    homeVM.statusStates[0] -> {
                        CircularProgressIndicator(
                            color = Color.Black,
                            strokeWidth = 4.dp,
                            modifier = Modifier.padding(20.dp)
                        )
                    }

                    homeVM.statusStates[1] -> {
                        Text(
                            text = "${weather?.precipitation}mm",
                            fontSize = fontSize,
                        )
                    }
                }

            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .glassEffect(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Wind",
                    fontSize = fontSize,
                )
                when(wStatus){
                    homeVM.statusStates[0] -> {
                        CircularProgressIndicator(
                            color = Color.Black,
                            strokeWidth = 4.dp,
                            modifier = Modifier.padding(20.dp)
                        )
                    }

                    homeVM.statusStates[1] -> {
                        Text(
                            text = "${weather!!.windSpeed}m/s",
                            fontSize = fontSize,
                        )
                    }
                }

            }

        }
        Box(
            modifier = Modifier
                .weight(1f)
                .glassEffect(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                when(wStatus){
                    homeVM.statusStates[0] -> {
                        Text(
                            text = "Sun",
                            fontSize = fontSize,
                        )
                        CircularProgressIndicator(
                            color = Color.Black,
                            strokeWidth = 4.dp,
                            modifier = Modifier.padding(20.dp)
                        )
                    }

                    homeVM.statusStates[1] -> {
                        val currentTime = weather!!.time

                        val riseTime = rise.substringAfter("T").substringBefore("+")
                        val riseDt = DateTime(
                            currentTime.year,
                            currentTime.month,
                            currentTime.day,
                            riseTime.substringBefore(":")
                        )

                        val setTime = set.substringAfter("T").substringBefore("+")

                        if (riseDt >= currentTime){
                            Text(
                                text = "Sunrise",
                                fontSize = fontSize,
                            )
                            Text(
                                text = riseTime,
                                fontSize = fontSize,
                            )
                        }
                        else{
                            Text(
                                text = "Sunset",
                                fontSize = fontSize,
                            )
                            Text(
                                text = setTime,
                                fontSize = fontSize,
                            )
                        }
                        
                    }
                }
            }

        }
    }
}