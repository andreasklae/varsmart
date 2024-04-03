package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.glassEffect
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.HomeViewModel

@Composable
fun RainAndWind(homeVM: HomeViewModel){

    val wStatus by homeVM.wStatus.collectAsState()
    val weather by homeVM.currentWeather.collectAsState()
    Row(
        modifier = Modifier.padding(horizontal = 20.dp), // Apply padding to the Row instead of each Box
        horizontalArrangement = Arrangement.SpaceBetween // This will arrange children with space between them
    ) {
        Box(
            modifier = Modifier
                .weight(1f) // Each Box will fill half the available space
                .padding(end = 10.dp) // Apply padding to the right of the first Box
                .aspectRatio(1f) // Keep the aspect ratio 1:1
                .glassEffect(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${weather?.precipitation}mm",
                fontSize = 35.sp,
            )
        }
        Box(
            modifier = Modifier
                .weight(1f) // Each Box will fill half the available space
                .padding(start = 10.dp) // Apply padding to the left of the second Box
                .aspectRatio(1f) // Keep the aspect ratio 1:1
                .glassEffect(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${weather?.windSpeed}m/s",
                fontSize = 35.sp,
            )
        }
    }
}