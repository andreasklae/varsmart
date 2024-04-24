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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect

@Composable
fun Next24(
    data: DataHolder,
    age: Int,
    gpt24h: String,
    updateGpt: (Int) -> Unit
) {
    val next24 = data.next24h
    val scrollState = rememberScrollState()
    Column {
        Header("Været det neste døgnet")
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            next24.forEach {
                HourlyForecast(it)
                Spacer(modifier = Modifier.width(10.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))

        }
        Spacer(modifier = Modifier.height(10.dp))
        GptSpeechBubble(gpt24h, { updateGpt(age) })
    }
}

@Composable
fun HourlyForecast(weather: WeatherTimeForecast) {
    Box(
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassEffect(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(10.dp)
            ) {
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


