package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

@Composable
fun Next24(weatherVM: WeatherViewModel, data: DataHolder){
    val weatherStatus = data.weatherStatus
    val next24 = data.next24h
    val gptWeek = data.weekGpt
    val scrollState = rememberScrollState()


    Button(
        onClick = {
            weatherVM.updateGPTWeek()
        },
        colors = buttonColors(Color.Transparent),
        modifier = Modifier.offset(x = -10.dp, y = -0.dp),
    ) {
        MrPraktisk()
    }
    SpeechBubble(gptWeek)
    Spacer(modifier = Modifier.height(15.dp))


    Row (
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ){
        when (weatherStatus.value) {
            data.statusStates[0] -> {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 4.dp,
                    modifier = Modifier.padding(20.dp)
                )
            }

            data.statusStates[1] -> {
                next24.forEach {
                    Spacer(modifier = Modifier.width(20.dp))
                    HourlyForecast(it)
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }

            data.statusStates[2] -> {

            }
        }
    }

}

@Composable
fun HourlyForecast(weather: WeatherTimeForecast) {
    Box(
        contentAlignment = Alignment.Center
    ){

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassEffect()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(10.dp)
            ){
                Text(
                    text = weather.time.hour + ":00",
                    fontSize = 20.sp,
                )
                DrawSymbol(symbol = weather.symbolName, size = 80.dp)
                Text(
                    text = weather.temperature.toString() + "°C",
                    fontSize = 20.sp,

                    )
            }
        }


    }
}


