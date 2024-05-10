package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.NearMe
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
val windIconSize = 120

@Composable
fun RainSunWind(data: DataHolder) {
    // list to keep track of what boxes/widgets to show
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
            // boxes for rain and sunrise/set
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
    // box for wind
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

    // seperates the hour and minute into two variables
    val riseHour = rise.split(":")[0]
    val riseMinute = rise.split(":")[1]

    // creates a DateTime object og the sunrise
    val riseDt = DateTime(
        current.year,
        current.month,
        current.day,
        riseHour,
        riseMinute
    )

    // same as above, but for sunset
    val setHour = set.split(":")[0]
    val setMinute = set.split(":")[1]
    val setDt = DateTime(
        current.year,
        current.month,
        current.day,
        setHour,
        setMinute
    )

    // compares the current time with the sun set and rise
    val isSunrise = {
        // if its after sunrise
        if (current > riseDt) {
            // if its after sunset (but still before midnight)
            if (current > setDt){
                true
            }
            // if its between sunrise and sunset
            else false
        }
        // if its before sunrise
        else {
            true
        }
    }
    // Text to show in the ui
    val riseOrSet = {
        if (isSunrise()) "Oppgang"
        else "Nedgang"
    }

    // What icon to show in the ui
    val icon = {
        if (isSunrise()) R.drawable.sunrise
        else R.drawable.sunset
    }

    // whether to show time of rise or time of set
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
        ImageIcon(
            y = 0,
            x = 0,
            symbolId = icon(),
            width = 200,
            height = iconHeight,
            "sol ikon"
        )
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
    // checks if it is raining or snowing
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
        RainSymbol(weather.precipitation, rainOrSnow(), "regn ikon")
        Text(
            text = "${weather.precipitation}mm",
            fontSize = fontSize,
        )
    }
}

@Composable
fun Wind(data: DataHolder) {
    val weather = data.currentWeather
    val direction = data.currentWeather!!.windDirection!!

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp)
    )
    {
        // header
        Text(
            text = "Vind",
            fontSize = fontSize,
        )
        Spacer(modifier = Modifier.height(15.dp))

        // wind symbol and compass
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // wind symbol
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                weather!!.windSpeed?.let { WindSymbol(it) }
            }
            Spacer(modifier = Modifier.weight(1f))

            // compass of wind direction
            Compass(direction = direction)
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Written explanation of where the wind is coming from
        val directionString = {
            when {
                direction >= 0 && direction < 22.5 -> "Sørlig"
                direction >= 22.5 && direction < 67.5 -> "Sørvestlig"
                direction >= 67.5 && direction < 112.5 -> "Vestlig"
                direction >= 112.5 && direction < 157.5 -> "Nordvestlig"
                direction >= 157.5 && direction < 202.5 -> "Nordlig"
                direction >= 202.5 && direction < 247.5 -> "Nordøstlig"
                direction >= 247.5 && direction < 292.5 -> "Østlig"
                else -> "Sørøstlig"
            }
        }

        // wind speed and direction
        Text(
            text = "${weather!!.windSpeed}m/s ${directionString()}",
            fontSize = fontSize,
        )
    }
}

@Composable
fun Compass(direction: Double){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size((windIconSize + 15).dp)
            // draws a circle
            .border(
                border = BorderStroke(5.dp, Color.Black),
                CircleShape
            )
            .padding(10.dp)

    ){
        // each direction
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
        // arrow / needle
        Icon(
            imageVector = Icons.Default.ArrowRightAlt,
            contentDescription = "kurs",
            modifier = Modifier
                .size((windIconSize - 40).dp)
                .rotate(direction.toFloat() - 90)
        )
    }


}

@Composable
private fun RainSymbol(
    precipitation: Double,
    rainOrSnow: String,
    contentDescription: String
) {
    // checks the precipitation to set the correct symbol and description
    when {
        // light rain
        precipitation > 0 && precipitation <= 2.5 -> {
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.light,
                width = 200,
                height = iconHeight,
                contentDescription
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Lett ${rainOrSnow.lowercase()}"
            )
        }
        // moderate
        precipitation > 2.5 && precipitation <= 7.6 -> {
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.moderate,
                width = 200,
                height = iconHeight,
                contentDescription
            )
            Spacer(modifier = Modifier.height(10.dp))

            // gets the verb version of rainOrSnow
            val text = { if (rainOrSnow == "Snø") "snør" else "regner" }
            Text(
                text = "Det $text"
            )
        }

        // Heavy
        precipitation > 7.6 -> {
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.heavy,
                width = 200,
                height = iconHeight,
                contentDescription
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det bøtter ned!"
            )
        }

        // No rain
        else -> {
            ImageIcon(y = 0,
                x = 0,
                symbolId = R.drawable.no,
                width = 200,
                height = iconHeight,
                contentDescription
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ingen ${rainOrSnow.lowercase()}"
            )
        }
    }
}

@Composable
fun WindSymbol(windSpeed: Double) {
    // Checks the wind speed and sets the correct icon and description
    when {
        // Light wind
        windSpeed <= 5.5 -> {
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.lightwind,
                width = windIconSize,
                height = windIconSize,
                contentDescription = "vind ikon"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Svak vind"
            )
        }
        // moderate Wind
        windSpeed > 5.5 && windSpeed <= 10.7 -> {
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.moderatewind,
                width = windIconSize,
                height = windIconSize,
                contentDescription = "vind ikon"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det blåser"
            )
        }
        // strong wind
        windSpeed > 10.7 && windSpeed <= 17.1 -> {
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.strongwind,
                width = windIconSize,
                height = windIconSize,
                contentDescription = "vind ikon"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sterk vind"
            )
        }
        // extreme wind
        windSpeed > 17.1 -> {
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.extremewind,
                width = windIconSize,
                height = windIconSize,
                contentDescription = "vind ikon"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ekstrem vind!"
            )
        }
        // no wind
        else -> {
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.nowind,
                width = windIconSize,
                height = windIconSize,
                contentDescription = "vind ikon"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Det er vindstille!"
            )
        }
    }

}
