package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.ImageIcon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect

val fontSize = 20.sp
val iconHeight = 75

@Composable
fun RainWind(data: DataHolder) {

    val boxes = listOf<String>("Regn", "Vind")

    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        boxes.forEach {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .glassEffect()
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                when (it) {
                    "Regn" -> Rain(data)
                    "Vind" -> Wind(data)
                }
            }
        }
    }
}

@Composable
fun Rain(data: DataHolder) {
    val weather = data.currentWeather
    val symbol = weather!!.symbolName!!
    // checks if it is rain or snow
    val rainOrSnow = {
        if (symbol.contains("snow") || symbol.contains("sleet")) {
            "Snø"
        } else {
            "Regn"
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = rainOrSnow(),
            fontSize = fontSize,
        )
        Spacer(modifier = Modifier.height(15.dp))
        RainSymbol(weather.precipitation, rainOrSnow())
        Text(
            text = "${weather.precipitation}mm",
            fontSize = fontSize,
        )
    }
}

@Composable
fun Wind(data: DataHolder) {
    val weather = data.currentWeather
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Vind",
            fontSize = fontSize,
        )
        Spacer(modifier = Modifier.height(15.dp))
        weather!!.windSpeed?.let { WindSymbol(it) }
        Text(
            text = "${weather.windSpeed}m/s",
            fontSize = fontSize,
        )
    }
}

@Composable
private fun RainSymbol(precipitation: Double, rainOrSnow: String) {
    when {
        // light rain
        precipitation > 0 && precipitation <= 2.5 -> {
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.light, width = 200, height = iconHeight)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Lett ${rainOrSnow.lowercase()}"
            )
        }
        // moderate
        precipitation > 2.5 && precipitation <= 7.6 -> {
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.moderate, width = 200, height = iconHeight)
            Spacer(modifier = Modifier.height(10.dp))

            // gets the verb version of rainOrSnow
            val text = { if (rainOrSnow == "Snø") "snør" else "regner" }
            Text(
                text = "Det $text"
            )
        }

        // Heavy
        precipitation > 7.6 -> {
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.heavy, width = 200, height = iconHeight)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det bøtter ned!"
            )
        }

        // No rain
        else -> {
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.no, width = 200, height = iconHeight)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ingen ${rainOrSnow.lowercase()}"
            )
        }
    }
}

@Composable
fun WindSymbol(windSpeed: Double) {

    when {
        // Light wind
        windSpeed <= 5.5 -> {
            ImageIcon(
                y = 0, x = 0, symbolId = R.drawable.lightwind, width = 300, height = iconHeight
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Svak vind"
            )
        }
        // moderate Wind
        windSpeed > 5.5 && windSpeed <= 10.7 -> {
            ImageIcon(
                y = 0, x = 0, symbolId = R.drawable.moderatewind, width = 200, height = iconHeight
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det blåser"
            )
        }
        // strong wind
        windSpeed > 10.7 && windSpeed <= 17.1 -> {
            ImageIcon(
                y = 0, x = 0, symbolId = R.drawable.strongwind, width = 300, height = iconHeight
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sterk vind"
            )
        }
        // extreme wind
        windSpeed > 17.1 -> {
            ImageIcon(
                y = 0, x = 0, symbolId = R.drawable.extremewind, width = 300, height = iconHeight
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ekstrem vind!"
            )
        }
        // no wind
        else -> {
            ImageIcon(
                y = 0, x = 0, symbolId = R.drawable.nowind, width = 300, height = iconHeight
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det er vindstille!"
            )
        }
    }

}
