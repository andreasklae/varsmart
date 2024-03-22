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
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.MapState
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.ZoneClusterItem

class HomeViewModel(): ViewModel() {
    val wRepo = WeatherRepository()
    val aRepo = WarningRepository()
    val statusStates: List<String> = listOf("Loading", "Success", "Failed")

    // Variables for currentWeather
    val _currentWeather: MutableStateFlow<WeatherTimeForecast?> = MutableStateFlow(null)
    val currentWeather = _currentWeather.asStateFlow()
    val _symbol = MutableStateFlow("")
    val symbol = _symbol.asStateFlow()
    val _wStatus = MutableStateFlow(statusStates[0])
    val wStatus = _wStatus.asStateFlow()


    // Variables for next 24Hours
    val _dayWeatherStatus = MutableStateFlow(statusStates[0])
    val dayWeatherStatus = _dayWeatherStatus.asStateFlow()
    val _next24 = MutableStateFlow<List<WeatherTimeForecast>>(emptyList())
    val next24 = _next24.asStateFlow()

    // Variables for the week
    val _weekWeatherStatus = MutableStateFlow(statusStates[0])
    val weekWeatherStatus = _weekWeatherStatus.asStateFlow()
    val _week = MutableStateFlow<List<WeatherTimeForecast>>(emptyList())
    val week = _week.asStateFlow()

    // Variable for warning
    val _warning: MutableStateFlow<Feature?> = MutableStateFlow(null)
    val warning = _warning.asStateFlow()

    // Varibles for sunrise/set
    val _rise = MutableStateFlow("")
    val rise = _rise.asStateFlow()
    val _set = MutableStateFlow("")
    val set = _set.asStateFlow()
    
    // initialize location as Oslo
    val name = "Oslo"
    val type = "By"
    val fylke = "Oslo"
    val lat = 59.91
    val lon = 10.71

    val _loc = MutableStateFlow(CustomLocation(name, lat, lon, type, fylke))
    val loc = _loc.asStateFlow()

    // finds current datetime
    var current = LocalDateTime.now()
    var year = current.year.toString()
    var month = current.monthValue.toString()
    var day = current.dayOfMonth.toString()
    var hour = current.hour.toString()
    var dt = DateTime(
        year,
        month,
        day,
        hour
    )

    var data = DataHolder(
        dt,
        _currentWeather.value,
        _next24.value,
        _week.value,
        _warning.value,
        _rise.value,
        _set.value
    )
    init {
        println("init")
        update(true)
    }
    fun update(dataExist: Boolean) {
        println("updating for: ${_loc.value.name}")
        updateTime()
        if (dataExist && dt.hour > data.lastUpdate.hour){
            updateCurrentWeather()
            updateWeek()
            updateNext24h()
            fetchWarning()
            updateSunriseAndSunset()
            data.lastUpdate = dt
        }
        
        else if (dataExist){
            DataHolder.favourites.forEach{
                if (it.currentWeather?.customLocation == _loc.value){
                    data = it
                    data.lastUpdate = dt
                    _currentWeather.value = data.currentWeather
                    _next24.value = data.day
                    _week.value = data.week
                    _warning.value = data.warningFeature
                    _rise.value = data.rise
                    _set.value = data.set
                }
            }
        }
        
        
    }
    fun setLocation(loc: CustomLocation) {
        _loc.value = loc
        var dataExists = false
        DataHolder.favourites.forEach{
            if (it.currentWeather?.customLocation == loc){
                dataExists = true
            }
        }
        update(dataExists)
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

    

    fun updateCurrentWeather() {
        viewModelScope.launch {
            println("Fetching weather")
            _wStatus.value = statusStates[0]
            val fetchedWeather = wRepo.getCurrentWeather(dt, _loc.value)

            if (fetchedWeather != null) {
                _currentWeather.value = fetchedWeather
                _symbol.value = fetchedWeather.symbolName.toString()
                data.currentWeather = _currentWeather.value

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
                data.day = _next24.value
                
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
                data.week = _week.value
                
                _dayWeatherStatus.value = statusStates[1]
            } else {
                _weekWeatherStatus.value = statusStates[2]
            }
        }

    }

    fun updateSunriseAndSunset() {
        viewModelScope.launch {
            val sun = wRepo.getRiseAndSet(_loc.value, dt)
            _rise.value = sun.sunriseTime
            _set.value = sun.sunsetTime
            data.rise = _rise.value
            data.set = _set.value
        }
    }



    fun fetchWarning() {
        viewModelScope.launch {
            val warnings = aRepo.fetchAllWarnings().features
            _warning.value = aRepo.findClosestCoordinate(_loc.value, warnings)
            data.warningFeature = _warning.value
            
        }
    }
}