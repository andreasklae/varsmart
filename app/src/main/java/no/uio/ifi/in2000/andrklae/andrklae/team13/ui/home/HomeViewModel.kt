package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseAndSunset
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeViewModel: ViewModel() {
    val wRepo = WeatherRepository()
    
    val statusStates = listOf("Loading", "Sucess", "Failed")
    val currentDateTime = LocalDateTime.now()
    //val currentDateTime = _currentDateTime.asStateFlow()
    val _currentTime = MutableStateFlow<DateTime>(DateTime(
        currentDateTime.year.toString(),
        currentDateTime.monthValue.toString(),
        currentDateTime.dayOfMonth.toString(),
        currentDateTime.hour.toString())
    )
    val currentTime = _currentTime.asStateFlow()


    // Variables for currentWeather
    val _wStatus = MutableStateFlow("")
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
    fun updateCurrentWeather(time: DateTime, loc: Location){
        viewModelScope.launch {
            _wStatus.value = statusStates[0]
            val weather = wRepo.getCurrentWeather(time, loc)
            if (weather != null){
                _temp.value = weather.temperature.toString()
                _airPressure.value = weather.airPressure.toString()
                _cloudCoverage.value = weather.cloudCoverage.toString()
                _humidity.value = weather.humidity.toString()
                _windSpeed.value = weather.windSpeed.toString()

                _wStatus.value = statusStates[1]
            }

            else{
                _wStatus.value = statusStates[2]

            }
        }
    }

    // Variables for next 24Hours
    val _dayWeatherStatus = MutableStateFlow("Loading")
    val dayWeatherStatus = _wStatus.asStateFlow()
    val _next24 = MutableStateFlow<List<WeatherTimeForecast>>(emptyList())
    fun updateNext24h(dateTime: DateTime, location: Location){
        viewModelScope.launch {
            _dayWeatherStatus.value = statusStates[0]
            val list = wRepo.getWeather24h(dateTime, location)
            if (list.isNotEmpty()){
                _next24.value = list
                _dayWeatherStatus.value = statusStates[1]
            }
            else{
                _dayWeatherStatus.value = statusStates[2]
            }
        }
        
    }
    // Variables for the week
    val _weekWeatherStatus = MutableStateFlow("Loading")
    val weekWeatherStatus = _wStatus.asStateFlow()
    val _week = MutableStateFlow<List<WeatherTimeForecast>>(emptyList())
    fun updateWeek(dateTime: DateTime, location: Location){
        viewModelScope.launch {
            _weekWeatherStatus.value = statusStates[0]
            val list = wRepo.getWeatherWeek(dateTime, location)
            if (list.isNotEmpty()){
                _week.value = list
                _dayWeatherStatus.value = statusStates[1]
            }
            else{
                _weekWeatherStatus.value = statusStates[2]
            }
        }

    }
     fun updateSunriseAndSunset(dateTime: DateTime, location: Location) {
        viewModelScope.launch{
            wRepo.getRiseAndSet(location, dateTime)
        }
    }
    fun updateTime(){
        viewModelScope.launch{
               val newcurrentDateTime = LocalDateTime.now()
               //val currentDateTime = _currentDateTime.asStateFlow()
               _currentTime.value = DateTime(
                   newcurrentDateTime.year.toString(),
                   newcurrentDateTime.monthValue.toString(),
                   newcurrentDateTime.dayOfMonth.toString(),
                   newcurrentDateTime.hour.toString())
        }
    }
}