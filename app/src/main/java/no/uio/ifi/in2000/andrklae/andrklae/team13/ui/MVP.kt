package no.uio.ifi.in2000.andrklae.andrklae.team13.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

/* Commented out because its outdated and causes running errors



@SuppressLint("SuspiciousIndentation")
@Composable

fun MVP(homeVM: WeatherViewModel, activity: MainActivity) {

    val loc by homeVM.loc.collectAsState()
    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(10.dp)
    ) {
        if (loc.name == "My location"){
            Button(
                onClick = {
                    //homeVM.setLocation(0)
                }
            ){
                Text(text = DataHolder.Favourites[0].location.name)
            }
        }
        else{
            Button(onClick = {
                activity.getCurrentLocation()
            }) {
                Text(text = "My location")
            }
        }


        Text(
            text = loc.name,
            fontSize = 40.sp,
        )
        Widgets(homeVM)
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Widgets(homeVM: WeatherViewModel){
    val wStatus by homeVM.wStatus.collectAsState()
    val currentWeather by homeVM.currentWeather.collectAsState()

    val next24 by homeVM.next24.collectAsState()

    val week by homeVM.week.collectAsState()

    val alerts by homeVM.alerts.collectAsState()

    Column{
        // Temp Widget
        currentWeather?.symbolName?.let { DrawSymbol(symbol = it, size = 120.dp) }
        when (wStatus) {
            homeVM.statusStates[0] -> {
                CurrentTempWidget(homeVM.statusStates[0], "")
            }
            homeVM.statusStates[1] -> {


                Row(
                    modifier = Modifier.fillMaxWidth(), // Ensure the Row fills the screen width
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    currentWeather?.symbolName?.let {
                        CurrentTempWidget(currentWeather?.temperature.toString(),
                            it
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    CurrentRainWidget(currentWeather?.precipitation.toString())
                }

            }

            else -> {
                CurrentTempWidget(homeVM.statusStates[2], "")

            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Next 24h widget
        when (wStatus){
            homeVM.statusStates[0] -> {

            }
            homeVM.statusStates[1] -> {
                LazyRow{
                    items(next24) { weather ->
                        DayWidget(weather)

                        if (weather != next24.last()){
                            Spacer(modifier = Modifier.width(20.dp))
                        }

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AlertWidget(alerts.first().alert)

        Spacer(modifier = Modifier.height(20.dp))

        // Week forecast widget
        when (wStatus){
            homeVM.statusStates[0] -> {
                WeekTableWidget(listOf())
            }
            homeVM.statusStates[1] -> {
                WeekTableWidget(week)
            }
        }
    }

}
@Composable
fun CurrentTempWidget(temp: String, symbol: String) {


    Box(
        modifier = Modifier
            .size(120.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "Temperature", color = Color.White)
            Text(text = "$temp°C", color = Color.White)
        }
    }
}

@Composable
fun CurrentRainWidget(rain: String) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Rain", color = Color.White)
            Text(text = "${rain}mm", color = Color.White)
        }
    }
}

@Composable
fun DayWidget(weather: WeatherTimeForecast) {
    Box(
        modifier = Modifier
            .width(90.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(4.dp)) // Add a spacer for custom spacing
            DrawSymbol(symbol = weather.symbolName.toString(), size = 60.dp) // Adjusted size
            Spacer(modifier = Modifier.height(2.dp)) // Add a spacer for custom spacing
            Text(text = "${weather.temperature}°C", color = Color.White, modifier = Modifier.padding(top = 2.dp)) // Adjust padding as needed
            Text(text = weather.time.hour + ":00", color = Color.White)
            Spacer(modifier = Modifier.height(4.dp)) // Add a spacer for custom spacing

        }

    }
}

@Composable
fun WeekTableWidget(weatherForecastList: List<WeatherTimeForecast>) {
    Column(
        modifier = Modifier
            .background(color = Color.Gray, shape = RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Day",
                modifier = Modifier

                    .padding(8.dp),
                color = Color.White
            )
            Text(
                text = "Temperature",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End),
                color = Color.White

            )
        }
        Divider(color = Color.White, thickness = 1.dp)

        weatherForecastList.forEach { forecast ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = forecast.time.dayOfWeek,
                    modifier = Modifier

                        .padding(8.dp),
                    color = Color.White
                )
                Text(
                    text = "${forecast.temperature}°C",
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End),
                    color = Color.White
                )

            }

            if (forecast != weatherForecastList.last()){
                Divider(color = Color.White, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun AlertWidget(alerts: Feature?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Gray, shape = RoundedCornerShape(10.dp)),
    ) {


        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(10.dp)
        ) {

            Text(text = "Closest weather alert", color = Color.White)
            Divider(color = Color.White, thickness = 1.dp)
            if (alerts != null){
                Text(
                    text = alerts.properties.area + ":",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = alerts.properties.description,
                    color = Color.White
                )
            } else {
                Text(
                    text = "No nearby alerts",
                    color = Color.White
                )
            }


        }
    }
}

@Composable
fun DrawSymbol(symbol: String, size: Dp) {
    Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(coil.decode.SvgDecoder.Factory())
                .data("https://raw.githubusercontent.com/metno/weathericons/89e3173756248b4696b9b10677b66c4ef435db53/weather/svg/$symbol.svg")
                // Coil's .size() method is used to define a target size for image decoding, not the display size in Compose.
                // Since we're using Box's size to control the image size, this is not strictly necessary unless the SVG has a very large default size.
                .build(),
            // ContentScale.Fit will ensure the image scales properly within the bounds you've set without cropping.
            contentScale = ContentScale.Fit
        )
        Image(
            painter = painter,
            contentDescription = null,
            // Fill the max size of the parent Box, and adjust the image scaling with contentScale.
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Fit // This makes the image fit within the given dimensions, maintaining aspect ratio.
        )

    }
}
*/