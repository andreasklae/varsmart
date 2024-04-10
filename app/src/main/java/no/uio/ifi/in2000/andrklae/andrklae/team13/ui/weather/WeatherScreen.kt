package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.Next24
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.RainWindSun
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.UpperHalf
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.WarningRow
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.WeekTable
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.status.ErrorScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WeatherScreen(weatherVM: WeatherViewModel, pagerState: PagerState) {
    MainComponent(weatherVM)
}

@Composable
fun MainComponent(homeVM: WeatherViewModel){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .fillMaxHeight()
            .padding(0.dp)

    ) {
        item {
            UpperHalf(homeVM)
        }

        item {
            WarningRow(homeVM, 40)
        }

        item {
            Next24(homeVM)
        }
        item {
            RainWindSun(homeVM)
        }
        item {
            WeekTable(homeVM)
        }
        item{
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}





