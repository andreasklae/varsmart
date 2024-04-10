package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.glassEffect
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

@Composable
fun WeekTable(homeVM: WeatherViewModel){
    val wStatus by homeVM.wStatus.collectAsState()
    val week by homeVM.week.collectAsState()

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .glassEffect(),
        contentAlignment = Alignment.Center

    ){
        Column(
        ) {
            Text(
                text = "Været den neste uken",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, bottom = 5.dp)
            )
            Divider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 15.dp))
            when (wStatus) {
                homeVM.statusStates[0] ->{
                    CircularProgressIndicator(
                        color = Color.Black,
                        strokeWidth = 4.dp,
                        modifier = Modifier.padding(20.dp)
                    )
                }

                homeVM.statusStates[1] -> {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 15.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Dag",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(0.12f)
                        )

                        Text(
                            text = "Vær",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(0.15f)
                        )

                        Text(
                            text = "Temp",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(0.15f)
                                .fillMaxWidth()
                        )
                        Text(
                            text = "Regn",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(0.2f)
                        )
                    }
                    week.forEach{
                        val symbol = it.symbolName
                        DayRow(it)
                    }
                }

                homeVM.statusStates[2] ->{

                }
            }


        }

    }
}
@Composable
fun DayRow(weather: WeatherTimeForecast){
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 15.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = weather.time.dayOfWeek.take(3),
            fontSize = 20.sp,
            modifier = Modifier
                .weight(0.12f)
        )

        DrawSymbol(
            weather.symbolName, 30.dp,
            modifier = Modifier
                .weight(0.15f)
        )

        Text(
            text = weather.temperature.toString() + "°C",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier
                .weight(0.15f)
                .fillMaxWidth()
        )
        Text(
            text = weather.precipitation.toString() + "mm",
            textAlign = TextAlign.End,
            fontSize = 20.sp,
            modifier = Modifier
                .weight(0.2f)
        )

    }

}



