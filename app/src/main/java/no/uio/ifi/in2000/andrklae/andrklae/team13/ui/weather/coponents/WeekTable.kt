package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT.MrPraktiskAnimations
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.DrawSymbol
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect

@Composable
fun WeekTable(
    data: DataHolder,
    age: Int,
    hobbies: List<String>,
    gptText: String,
    updateWeekGpt: (Int, List<String>) -> Unit,
    animation: MrPraktiskAnimations
) {
    val week = data.week

    // table of the week
    Column {
        Header(header = "Været til uka")
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .glassEffect(),
            contentAlignment = Alignment.Center

        ) {
            Column(
            ) {
                // headers
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 15.dp)
                ) {
                    Text(
                        text = "Dag",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Temp",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Vær",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 10.dp), thickness = 1.dp,
                    color = Color.Black
                )

                // a row of info for each day in a week
                week.forEach {
                    DayRow(it)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Mr. praktisk with a speech bubble
        GptSpeechBubble(
            gptText,
            { updateWeekGpt(age, hobbies) },
            animation
            )

    }

}

@Composable
fun DayRow(weather: WeatherTimeForecast) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 15.dp)
            .fillMaxWidth()
    ) {
        // day
        Text(
            text = weather.time.dayOfWeek.take(3),
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.weight(1f))

        // temp
        Text(
            text = weather.temperature.toString() + "°C",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.weight(1f))

        // weather symbol
        DrawSymbol(weather.symbolName, 60.dp)
    }

}



