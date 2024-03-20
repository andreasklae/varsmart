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
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.MapState
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.ZoneClusterItem

class HomeViewModel(): ViewModel() {
    val wRepo = WeatherRepository()
    val aRepo = WarningRepository()
    // initialize location as Oslo
    val name = "Oslo"
    val type = "By"
    val fylke = "Oslo"
    val lat = 59.91
    val lon = 10.71

    val _loc = MutableStateFlow(CustomLocation(name, lat, lon, type, fylke))
    val loc = _loc.asStateFlow()

    // finds current datetime
    val current = LocalDateTime.now()
    val year = current.year.toString()
    val month = current.monthValue.toString()
    val day = current.dayOfMonth.toString()
    val hour = current.hour.toString()
    var dt = DateTime(
        year,
        month,
        day,
        hour
    ) // Assuming DateTime is a custom class you've defined to hold these values

    val statusStates: List<String> = listOf("Loading", "Success", "Failed")

    fun setLocation(loc: CustomLocation) {
        _loc.value = loc
        println("new location set")
    }




    // Variables for currentWeather
    val _wStatus = MutableStateFlow(statusStates[0])
    val wStatus = _wStatus.asStateFlow()
    val _temp = MutableStateFlow("")
    val temp = _temp.asStateFlow()
    val _airPressure = MutableStateFlow("")
    val airPressure = _airPressure.asStateFlow()
    val _cloudCoverage = MutableStateFlow("")
    val cloudCoverage = _cloudCoverage.asStateFlow()
    val _humidity = MutableStateFlow("")
    val humidity = _humidity.asStateFlow()
    val _windSpeed = MutableStateFlow("")
    val windSpeed = _windSpeed.asStateFlow()
    val _rain = MutableStateFlow("")
    val rain = _rain.asStateFlow()
    val _symbol = MutableStateFlow("")
    val symbol = _symbol.asStateFlow()

    // Variables for next 24Hours
    val _dayWeatherStatus = MutableStateFlow(statusStates[0])
    val dayWeatherStatus = _wStatus.asStateFlow()
    val _next24 = MutableStateFlow<List<WeatherTimeForecast>>(emptyList())
    val next24 = _next24.asStateFlow()

    // Variables for the week
    val _weekWeatherStatus = MutableStateFlow(statusStates[0])
    val weekWeatherStatus = _wStatus.asStateFlow()
    val _week = MutableStateFlow<List<WeatherTimeForecast>>(emptyList())
    val week = _week.asStateFlow()

    // Variable for warning
    val _warning: MutableStateFlow<Feature?> = MutableStateFlow(null)
    val warning = _warning.asStateFlow()

    init {
        println("init")
        update()
    }

    fun update() {
        updateCurrentWeather()
        updateWeek()
        updateNext24h()
        fetchWarning()
        println("updating for: ${_loc.value.name}")
    }

    fun updateCurrentWeather() {
        viewModelScope.launch {
            println("Fetching weather")
            _wStatus.value = statusStates[0]
            val weather = wRepo.getCurrentWeather(dt, _loc.value)

            if (weather != null) {
                _temp.value = weather.temperature.toString()
                _airPressure.value = weather.airPressure.toString()
                _cloudCoverage.value = weather.cloudCoverage.toString()
                _humidity.value = weather.humidity.toString()
                _windSpeed.value = weather.windSpeed.toString()
                _rain.value = weather.percipitation.toString()
                _symbol.value = weather.symbolName.toString()

                _wStatus.value = statusStates[1]
            } else {
                _wStatus.value = statusStates[2]
            }
        }
    }


    fun updateNext24h() {
        viewModelScope.launch {
            _dayWeatherStatus.value = statusStates[0]
            val list = wRepo.getWeather24h(dt, _loc.value)
            if (list.isNotEmpty()) {
                _next24.value = list
                _dayWeatherStatus.value = statusStates[1]
            } else {
                _dayWeatherStatus.value = statusStates[2]
            }
        }

    }

    fun updateWeek() {
        viewModelScope.launch {
            _weekWeatherStatus.value = statusStates[0]
            val list = wRepo.getWeatherWeek(dt, _loc.value)
            if (list.isNotEmpty()) {
                _week.value = list
                _dayWeatherStatus.value = statusStates[1]
            } else {
                _weekWeatherStatus.value = statusStates[2]
            }
        }

    }

    fun updateSunriseAndSunset() {
        viewModelScope.launch {
            wRepo.getRiseAndSet(_loc.value, dt)
        }
    }

    fun updateTime() {
        viewModelScope.launch {
            val newcurrentDateTime = LocalDateTime.now()
            dt = DateTime(
                newcurrentDateTime.year.toString(),
                newcurrentDateTime.monthValue.toString(),
                newcurrentDateTime.dayOfMonth.toString(),
                newcurrentDateTime.hour.toString()
            )
        }
    }

    fun fetchWarning() {
        viewModelScope.launch {
            val warnings = aRepo.fetchAllWarnings().features
            _warning.value = aRepo.findClosestCoordinate(_loc.value, warnings)
        }
    }
}