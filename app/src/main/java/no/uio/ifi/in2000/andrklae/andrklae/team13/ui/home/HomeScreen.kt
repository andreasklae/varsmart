package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.glassEffect
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components.Next24
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components.RainAndWind
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components.TopAppBar
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components.UpperHalf
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components.WeekTable
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.Team13Theme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeVM: HomeViewModel) {
    Team13Theme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(

            ) { innerPadding ->
                MainComponent(innerPadding = innerPadding, homeVM)
                TopAppBar()
            }
        }
    }
}

@Composable
fun MainComponent(innerPadding: PaddingValues, homeVM: HomeViewModel){

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxHeight()
            .padding(innerPadding)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB3E5FC), // Light blue
                        Color(0xFF01579B)  // Dark blue
                    )
                )
            )
    ) {
        item {
            UpperHalf(homeVM)
        }

        item {
            Warning(homeVM)
        }

        item {
            RainAndWind(homeVM)
        }

        item {
            Next24(homeVM)
        }
        item {
            WeekTable(homeVM)

        }

    }
}





