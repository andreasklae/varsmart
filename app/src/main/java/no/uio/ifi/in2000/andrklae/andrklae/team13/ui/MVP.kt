package no.uio.ifi.in2000.andrklae.andrklae.team13.ui

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.asStateFlow
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CurrentLocation.LocationUtil
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.HomeViewModel




@SuppressLint("SuspiciousIndentation")
@Composable
fun MVP(homeVM: HomeViewModel, activity: MainActivity) {
 val loc by homeVM.loc.collectAsState()
    println("My location: ${loc.lat}, ${loc.lon}")
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.
        verticalScroll(scrollState)
            .padding(10.dp)
    ) {
        if (loc.name == "My location"){
            Button(
                onClick = {
                homeVM.setLocation(CustomLocation("Oslo", 59.91, 10.71, "By", "Oslo"))
                    homeVM.update()
            }
            ){
                Text(text = "Oslo")
            }
        }
        else{
            Button(onClick = { activity.getCurrentLocation() }) {
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

@Composable
fun Widgets(homeVM: HomeViewModel){
    val wStatus by homeVM.wStatus.collectAsState()
    val temp by homeVM.temp.collectAsState()
    val airPressure by homeVM.airPressure.collectAsState()
    val cloudCoverage by homeVM.cloudCoverage.collectAsState()
    val humidity by homeVM.humidity.collectAsState()
    val windSpeed by homeVM.windSpeed.collectAsState()
    val rain by homeVM.rain.collectAsState()

    val dayWeatherStatus by homeVM.dayWeatherStatus.collectAsState()
    val next24 by homeVM.next24.collectAsState()

    val weekWeatherStatus by homeVM.weekWeatherStatus.collectAsState()
    val week by homeVM.week.collectAsState()

    val alerts by homeVM.warning.collectAsState()

    Column{
        // Temp Widget
        when (wStatus) {
            homeVM.statusStates[0] -> {
                println(homeVM.statusStates[0])
                CurrentTempWidget(homeVM.statusStates[0])
            }
            homeVM.statusStates[1] -> {
                println(homeVM.statusStates[1])

                Row(
                    modifier = Modifier.fillMaxWidth(), // Ensure the Row fills the screen width
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CurrentTempWidget(temp)
                    Spacer(modifier = Modifier.weight(1f))
                    CurrentRainWidget(rain)
                }

            }

            else -> {
                CurrentTempWidget(homeVM.statusStates[2])
                println(homeVM.statusStates[2])

            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Next 24h widget
        when (dayWeatherStatus){
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

        AlertWidget(alerts)

        Spacer(modifier = Modifier.height(20.dp))

        // Week forecast widget
        when (weekWeatherStatus){
            homeVM.statusStates[0] -> {

            }
            homeVM.statusStates[1] -> {
                WeekTableWidget(week)
            }
        }
    }

}
@Composable
fun CurrentTempWidget(temp: String) {
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
            Text(text = weather.time.hour + ":00", color = Color.White)
            Text(text = "${weather.temperature}°C", color = Color.White)
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
                println(alerts)
                Text(
                    text = alerts.properties.area + ":",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = alerts.properties.description,
                    color = Color.White
                )
            }
            else{
                Text(
                    text = "No nearby alerts",
                    color = Color.White
                )
            }


        }
    }
}