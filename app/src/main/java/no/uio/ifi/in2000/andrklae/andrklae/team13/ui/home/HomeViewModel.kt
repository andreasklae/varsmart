package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import java.time.LocalDateTime
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.util.copy
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.model.polygonOptions
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.MapState
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.ZoneClusterItem

class HomeViewModel(index: Int): ViewModel() {
    var data = DataHolder.favourites[index]

    val statusStates: List<String> = listOf("Loading", "Success", "Failed")
    val _wStatus = MutableStateFlow(statusStates[0])
    val wStatus = _wStatus.asStateFlow()

    // Variables for currentWeather
    val _currentWeather: MutableStateFlow<WeatherTimeForecast?> = MutableStateFlow(null)
    val currentWeather = _currentWeather.asStateFlow()
    val _symbol = MutableStateFlow("")
    val symbol = _symbol.asStateFlow()


    // Variables for next 24Hours
    val _next24 = MutableStateFlow<List<WeatherTimeForecast>>(emptyList())
    val next24 = _next24.asStateFlow()

    // Variables for the week
    val _week = MutableStateFlow<List<WeatherTimeForecast>>(emptyList())
    val week = _week.asStateFlow()

    // Variables for warning
    val _warning: MutableStateFlow<Feature?> = MutableStateFlow(null)
    val warning = _warning.asStateFlow()

    // Varibles for sunrise/set
    val _sunStatus = MutableStateFlow(statusStates[0])
    val sunStatus = _sunStatus.asStateFlow()
    val _rise = MutableStateFlow("")
    val rise = _rise.asStateFlow()
    val _set = MutableStateFlow("")
    val set = _set.asStateFlow()


    val _loc = MutableStateFlow(DataHolder.favourites[0].location)
    val loc = _loc.asStateFlow()

    init {
        updateAll()
    }
    fun updateAll(){
        println("Updating data for ${data.location.name}")
        updateWeather()
        updateWarning()
        updateSunriseAndSunset()
    }
    fun setLocation(i: Int) {
        print("Changing location from ${data.location.name} to ")
        data = DataHolder.favourites[i]
        println(data.location.name)

        _loc.value = data.location
        updateAll()
    }

    fun updateWeather() {
        viewModelScope.launch {
            _wStatus.value = statusStates[0]
            data.updateWeather()
            val weather = data.currentWeather

            if (weather != null) {
                _currentWeather.value = weather
                _symbol.value = weather.symbolName.toString()
                _next24.value = data.next24h
                _week.value = data.week


                _wStatus.value = statusStates[1]
            } else {
                println("Failed fetching current weather for ${data.location.name}")
                _wStatus.value = statusStates[2]
            }
        }
    }

    fun updateSunriseAndSunset() {
        viewModelScope.launch {
            _sunStatus.value = statusStates[0]
            data.updateSunriseAndSunset()
            _rise.value = data.rise.toString()
            _set.value = data.set.toString()

            if (data.rise == null || data.set == null){
                _sunStatus.value = statusStates[2]
            }
            else{
                _sunStatus.value = statusStates[1]
            }

        }
    }



    fun updateWarning() {
        viewModelScope.launch {
            data.updateWarning()
            _warning.value = data.warning

        }
    }
}