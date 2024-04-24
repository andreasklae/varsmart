package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Status
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.ImageIcon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect

val fontSize = 20.sp
val iconHeight = 75
val windIconSize = 100

@Composable
fun RainSunWind(data: DataHolder) {

    val boxes = mutableListOf<String>("Rain")
    var rise = ""
    var set = ""

    // if sunrise api is successful
    if (data.sunStatus.value == Status.SUCCESS) {
        rise = data.rise!!.substringAfter("T").substringBefore("+")
        set = data.set!!.substringAfter("T").substringBefore("+")
        
        boxes.add("Sun")
    }

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
                    "Rain" -> Rain(data)
                    "Sun" -> Sun(data, rise, set)
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .glassEffect()
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ){
        Wind(data = data)

    }
}

@Composable
fun Sun(data: DataHolder, rise: String, set: String) {

    val current = data.lastUpdate

    val riseHour = rise.split(":")[0]
    val riseMinute = rise.split(":")[1]
    val riseDt = DateTime(
        current.year,
        current.month,
        current.day,
        riseHour,
        riseMinute
    )
    val setHour = set.split(":")[0]
    val setMinute = set.split(":")[1]
    val setDt = DateTime(
        current.year,
        current.month,
        current.day,
        setHour,
        setMinute
    )


    val isSunrise = {
        // if its after sunrise
        if (current > riseDt) {
            // if its after sunset
            if (current > setDt){
                true
            }
            else false
        } else {
            true
        }
    }
    val riseOrSet = {
        if (isSunrise()) "Oppgang"
        else "Nedgang"
    }

    val icon = {
        if (isSunrise()) R.drawable.sunrise
        else R.drawable.sunset
    }

    val time = {
        if (isSunrise()) riseHour + ":" + riseMinute
        else setHour + ":" + setMinute
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Sol",
            fontSize = fontSize,
        )
        Spacer(modifier = Modifier.height(15.dp))
        ImageIcon(y = 0, x = 0, symbolId = icon(), width = 200, height = iconHeight)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = riseOrSet())
        Text(
            text = time(),
            fontSize = fontSize,
        )
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp)
    )
    {
        Text(
            text = "Vind",
            fontSize = fontSize,
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                weather!!.windSpeed?.let { WindSymbol(it) }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size((windIconSize + 15).dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)

            ){
                Text(
                    text = "N",
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                Text(
                    text = "Ø",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
                Text(
                    text = "S",
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
                Text(
                    text = "V",
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Icon(
                    imageVector = Icons.Default.ArrowRightAlt,
                    contentDescription = "kurs",
                    modifier = Modifier
                        .size((windIconSize - 30).dp)
                        .rotate((data.currentWeather!!.windDirection!! - 90).toFloat())
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Svak vind"
        )
        Text(
            text = "${weather!!.windSpeed}m/s",
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
                y = 0, x = 0, symbolId = R.drawable.lightwind, width = windIconSize, height = windIconSize
            )
        }
        // moderate Wind
        windSpeed > 5.5 && windSpeed <= 10.7 -> {
            ImageIcon(
                y = 0, x = 0, symbolId = R.drawable.moderatewind, width = windIconSize, height = windIconSize
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det blåser"
            )
        }
        // strong wind
        windSpeed > 10.7 && windSpeed <= 17.1 -> {
            ImageIcon(
                y = 0, x = 0, symbolId = R.drawable.strongwind, width = windIconSize, height = windIconSize
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sterk vind"
            )
        }
        // extreme wind
        windSpeed > 17.1 -> {
            ImageIcon(
                y = 0, x = 0, symbolId = R.drawable.extremewind, width = windIconSize, height = windIconSize
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ekstrem vind!"
            )
        }
        // no wind
        else -> {
            ImageIcon(
                y = 0, x = 0, symbolId = R.drawable.nowind, width = windIconSize, height = windIconSize
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det er vindstille!"
            )
        }
    }

}
