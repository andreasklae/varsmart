package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

val fontSize = 20.sp
val iconHeight = 70
@Composable
fun RainWindSun(homeVM: WeatherViewModel, data: DataHolder){

    val boxes = listOf<String>("Regn", "Vind", "Sol")

    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        boxes.forEach {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .glassEffect()
                    .height(200.dp)
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                when (it){
                    "Regn" -> Rain(homeVM, data)
                    "Vind" -> Wind(homeVM, data)
                    "Sol" -> Sun(homeVM, data)
                }
            }
        }
    }
}
@Composable
fun Rain(homeVM: WeatherViewModel, data: DataHolder) {
    val weatherStatus = data.weatherStatus
    val weather = data.currentWeather
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Regn",
            fontSize = fontSize,
        )
        Spacer(modifier = Modifier.weight(1f))
        when (weatherStatus.value) {
            data.statusStates[0] -> {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 4.dp,
                    modifier = Modifier.padding(20.dp)
                )
            }

            data.statusStates[1] -> {

                Spacer(modifier = Modifier.weight(1f))
                RainSymbol(weather!!.precipitation)
                Text(
                    text = "${weather?.precipitation}mm",
                    fontSize = fontSize,
                )
            }
        }
    }
}
@Composable
fun Wind(homeVM: WeatherViewModel, data: DataHolder) {
    val weatherStatus = data.weatherStatus
    val weather = data.currentWeather
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Vind",
            fontSize = fontSize,
        )
        Spacer(modifier = Modifier.weight(1f))
        when (weatherStatus.value) {
            data.statusStates[0] -> {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 4.dp,
                    modifier = Modifier.padding(20.dp)
                )
            }

            data.statusStates[1] -> {
                Spacer(modifier = Modifier.weight(1f))
                weather!!.windSpeed?.let { WindSymbol(it) }
                Text(
                    text = "${weather!!.windSpeed}m/s",
                    fontSize = fontSize,
                )
            }
        }
    }

}
@Composable
private fun RainSymbol(precipitation: Double) {
    when {
        // light rain
        precipitation > 0 && precipitation <= 2.5 ->{
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.light, width = 90, height = iconHeight )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Lett regn"
            )
        }
        // moderate
        precipitation > 2.5 && precipitation <= 7.6 -> {
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.moderate, width = 90, height = iconHeight )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det regner"
            )
        }

        // Heavy
        precipitation > 7.6 -> {
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.heavy, width = 90, height = iconHeight )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det bøtter ned!"
            )
        }

        // No rain
        else -> {
            Row {
                ImageIcon(y = 0, x = 0, symbolId = R.drawable.no, width = 15, height = iconHeight )
                ImageIcon(y = 0, x = 0, symbolId = R.drawable.smile, width = 70, height = iconHeight )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ingen regn"
            )
        }
    }
}

@Composable
fun WindSymbol(windSpeed: Double){

    when {
        // Light wind
        windSpeed <= 5.5 -> {
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.lightwind, width = 90, height = iconHeight )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Svak vind"
            )
        }
        // moderate Wind
        windSpeed > 5.5 && windSpeed <= 10.7 -> {
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.moderatewind, width = 90, height = iconHeight )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det blåser"
            )
        }
        // strong wind
        windSpeed > 10.7 && windSpeed <= 17.1 -> {
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.strongwind, width = 90, height = iconHeight )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sterk vind"
            )
        }
        // extreme wind
        windSpeed > 17.1 -> {
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.extremewind, width = 90, height = iconHeight )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ekstrem vind!"
            )
        }
        // no wind
        else -> {
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.nowind, width = 90, height = iconHeight
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det er vindstille!"
            )
        }
    }

}

@Composable
fun Sun(homeVM: WeatherViewModel, data: DataHolder) {
    val sunStatus = data.sunStatus
    val set = data.set
    val rise = data.rise

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sol",
            fontSize = fontSize,
        )
        Spacer(modifier = Modifier.weight(1f))

        when(sunStatus.value){
            data.statusStates[0] -> {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 4.dp,
                    modifier = Modifier.padding(20.dp)
                )
            }

            data.statusStates[1] -> {
                val currentTime = data.dt

                val riseTime = rise!!.substringAfter("T").substringBefore("+")
                val riseDt = DateTime(
                    currentTime.year,
                    currentTime.month,
                    currentTime.day,
                    riseTime.substringBefore(":")
                )

                val setTime = set!!.substringAfter("T").substringBefore("+")

                if (riseDt >= currentTime){
                    ImageIcon(y = 0, x = 0, symbolId = R.drawable.sunrise, width = 90, height = iconHeight)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "Oppgang:")
                    Text(
                        text = riseTime,
                        fontSize = fontSize,
                    )
                } else {
                    ImageIcon(y = 0, x = 0, symbolId = R.drawable.sunset, width = 90, height = iconHeight)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "Nedgang:")
                    Text(
                        text = setTime,
                        fontSize = fontSize,
                    )
                }

            }
        }
    }

}
