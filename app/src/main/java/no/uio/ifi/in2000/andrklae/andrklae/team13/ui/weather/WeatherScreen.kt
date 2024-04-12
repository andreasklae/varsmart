package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.Next24
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.RainWindSun
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.UpperHalf
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.WarningRow
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.WeekTable


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WeatherScreen(weatherVM: WeatherViewModel, pagerState: PagerState) {
    MainComponent(weatherVM)
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MainComponent(weatherViewModel: WeatherViewModel){
    // test
    val data = weatherViewModel.data.observeAsState().value
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .fillMaxHeight()
            .padding(0.dp)

    ) {
        item {
            UpperHalf(data!!)
        }

        item {
            WarningRow(weatherViewModel, 40)
        }

        item {
            Next24(weatherViewModel)
        }
        item {
            RainWindSun(weatherViewModel)
        }
        item {
            WeekTable(weatherViewModel)
        }
        item{
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}





