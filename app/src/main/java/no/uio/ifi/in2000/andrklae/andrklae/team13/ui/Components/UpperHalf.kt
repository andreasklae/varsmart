package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

@Composable
fun UpperHalf(weatherVM: WeatherViewModel, data: DataHolder){
    val loc = data.location.name

    val weatherStatus = data.weatherStatus
    val weather = data.currentWeather

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.flowers),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .height(450.dp)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Text(text = loc, fontSize = 35.sp)
            Spacer(modifier = Modifier.height(30.dp))
            when (weatherStatus.value) {
                data.statusStates[0] -> {
                    CircularProgressIndicator(
                        color = Color.Black, // Sets the color of the spinner
                        strokeWidth = 4.dp // Sets the stroke width of the spinner
                    )
                }

                data.statusStates[1] -> {
                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        weatherVM.updateMainGpt()
                        GptBox(weatherVM, data)
                    }
                }
            }

        }
        if (weatherStatus.value == data.statusStates[1]){
            DrawSymbol(
                weather!!.symbolName,
                size = 120.dp,
                modifier = Modifier
                    .offset(x = 190.dp, y = 90.dp)
            )
        }

    }
}
@Composable
fun WeatherBox(weather: WeatherTimeForecast){
    Box(
        modifier = Modifier
            .fillMaxSize(0.5f)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.7f)) // Semi-transparent background
            .border(1.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
            .aspectRatio(1.0F),

        contentAlignment = Alignment.Center

    ) {


    }
}
@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Composable
fun GptBox(weatherVM: WeatherViewModel, data: DataHolder){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(0.7f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(Modifier.padding(15.dp)) {
                Text(
                    text = "${data.currentWeather!!.temperature}°C",
                    fontSize = 35.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Mr. Praktisk sier:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Column(
                    modifier = Modifier
                ) {
                    var gptText by remember { mutableStateOf("") }
                    when(data.mainGptStatus.value){
                        data.statusStates[0] -> {
                            LaunchedEffect(key1 = true) {
                                while (isActive) {  // Check if coroutine is still active
                                    gptText = weatherVM.dotLoading(gptText)
                                    delay(200)
                                }
                            }
                        }

                        data.statusStates[1] -> {
                            gptText = data.mainGpt
                            /*
                            LaunchedEffect(key1 = true) {

                                gptText = ""
                                data.mainGpt.forEach {
                                    gptText += it
                                    delay(20)
                                }
                            }*/

                        }

                        data.statusStates[2] -> {

                        }

                    }
                    Text(
                        text = gptText,
                        fontSize = 14.sp,
                        lineHeight = 22.sp, // Adjusted for visual consistency
                        modifier = Modifier.fillMaxWidth()
                    )

                }

            }
        }
        ImageIcon(y = 0, x = 0 , symbolId =R.drawable.arrowright , width =30 , height =60 )

        MrPraktisk()

    }




}



