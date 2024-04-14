package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.Next24
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.RainWindSun
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.UpperHalf
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.WarningRow
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.WeekTable
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.MapWithPolygon


@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState"
)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel, pagerState: PagerState) {
    val data = weatherViewModel.data.observeAsState().value
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .fillMaxSize()

    ) {
        item {
            UpperHalf(weatherViewModel, data!!)
        }

        item {
            WarningRow(data!!, 40)
        }

        item {
            Next24(weatherViewModel, data!!)
        }
        item {
            RainWindSun(weatherViewModel, data!!)
        }
        item {
            WeekTable(weatherViewModel, data!!)
        }
        item{
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}





