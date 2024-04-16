package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

@Composable
fun UpperHalf(weatherVM: WeatherViewModel, data: DataHolder){
    val loc = data.location.name
    Box {
        // background image
        Image(
            painter = painterResource(id = R.drawable.space),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .glassEffect()
                    .padding(vertical = 20.dp)
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(180.dp)
                ) {
                    Text(text = loc, fontSize = 35.sp, textAlign = TextAlign.Center)
                    Text(
                        text = "Oppdatert: ${data.lastUpdate.hour}:${data.lastUpdate.minute}",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clickable {
                            weatherVM.updateAll()
                        }
                        .clip(CircleShape)
                        .align(Alignment.CenterEnd)
                        .padding(end = 20.dp)
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        "refresh",
                        Modifier.size(40.dp)


                    )
                }

            }
            WeatherBox(weatherVM, data)
            Spacer(modifier = Modifier.height(20.dp))
            val GPTMain by weatherVM.GPTMain.collectAsState()
            GptSpeechBubble(GPTMain, { weatherVM.updateMainGpt() })

        }
    }
}

@Composable
fun WeatherBox(weatherVM: WeatherViewModel, data: DataHolder){
    val sunStatus = data.sunStatus
    val set = data.set
    val rise = data.rise
    val fontSize = 20
    val iconHeight = weatherVM.spToDp(fontSize.toFloat())
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = 20.dp)
            .glassEffect()
            .padding(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {

            Spacer(modifier = Modifier.weight(1f))
            DrawSymbol(
                data.currentWeather!!.symbolName,
                size = 120.dp,
            )
            //Spacer(modifier = Modifier.weight(1f))
            if (sunStatus.value == data.statusStates[1]){
                val riseTime = rise!!.substringAfter("T").substringBefore("+")
                val setTime = set!!.substringAfter("T").substringBefore("+")
                Row(
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sunrise),
                        contentDescription = "Soloppgang",
                        modifier = Modifier.height(iconHeight.dp)
                    )
                    Text(
                        text = riseTime,
                        fontSize = fontSize.sp
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sunset),
                        contentDescription = "Solnedgang",
                        modifier = Modifier.height(iconHeight.dp)
                    )
                    Text(
                        text = setTime,
                        fontSize = fontSize.sp
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxHeight()
        ){
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${data.currentWeather!!.temperature}°C",
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            val highestAndLowest = data.findHighestAndLowestTemp()
            Text(
                text = "H: ${highestAndLowest[1]}°C",
                fontSize = fontSize.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "L: ${highestAndLowest[0]}°C",
                fontSize = fontSize.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }

}



