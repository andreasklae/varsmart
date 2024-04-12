package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Alert
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity

class WeatherViewModel(index: Int, activity: MainActivity): ViewModel() {
    private val _data = MutableLiveData<DataHolder>(DataHolder.Favourites[index])

    // Immutable LiveData for external access
    val data: LiveData<DataHolder> = _data

    @SuppressLint("StaticFieldLeak")
    val activity = activity
    val statusStates: List<String> = listOf("Loading", "Success", "Failed")
    val _wStatus = MutableStateFlow(statusStates[0])
    val wStatus = _wStatus.asStateFlow()

    // Variables for currentWeather
    val _currentWeather: MutableStateFlow<WeatherTimeForecast?> = MutableStateFlow(null)
    val currentWeather = _currentWeather.asStateFlow()
    val _symbol = MutableStateFlow("")
    val symbol = _symbol.asStateFlow()
    val _gptCurrent = MutableStateFlow("")
    val gptCurrent = _gptCurrent.asStateFlow()


    // Variables for next 24Hours
    val _next24 = MutableStateFlow<List<WeatherTimeForecast>>(emptyList())
    val next24 = _next24.asStateFlow()
    val _gptWeek = MutableStateFlow("Trykk på meg for å spørre om været det neste døgnet")
    val gptWeek = _gptWeek.asStateFlow()

    // Variables for the week
    val _week = MutableStateFlow<List<WeatherTimeForecast>>(emptyList())
    val week = _week.asStateFlow()

    // Variables for warning
    val _alerts: MutableStateFlow<List<Alert>> = MutableStateFlow(mutableListOf())
    val alerts = _alerts.asStateFlow()

    // Varibles for sunrise/set
    val _sunStatus = MutableStateFlow(statusStates[0])
    val sunStatus = _sunStatus.asStateFlow()
    val _rise = MutableStateFlow("")
    val rise = _rise.asStateFlow()
    val _set = MutableStateFlow("")
    val set = _set.asStateFlow()


    val _loc = MutableStateFlow(DataHolder.Favourites[0].location)
    val loc = _loc.asStateFlow()

    init {
        updateAll()
    }
    fun updateAll(){
        if (activity.isOnline()){
            println("has internet")
            println("Updating data for ${_data.value?.location?.name}")
            updateWeather()
            updateWarning()
            updateSunriseAndSunset()
        }
        else{
            _wStatus.value = statusStates [2]
            _sunStatus.value = statusStates [2]
        }

    }
    @OptIn(ExperimentalFoundationApi::class)
    fun setLocation(i: Int) {
        print("Changing location from ${_data.value?.location?.name} to ${DataHolder.Favourites[i].location.name}")
        val isSame = _loc.value == DataHolder.Favourites[i].location

        if (!isSame) {
            _data.value = DataHolder.Favourites[i]
            println("       ")
            println("ny data " + data.value!!.location!!.name)
            _loc.value = data.value!!.location
            updateAll()
        }
    }

    fun updateWeather() {
        viewModelScope.launch {

            // loading dots while waiting for GPT response
            launch {
                while (data.value!!.GPTCurrent == ""){
                    _gptCurrent.value = dotLoading(_gptCurrent.value)
                    delay(500)
                }
            }

            launch {
                _wStatus.value = statusStates[0]
                data.value!!.updateAll()
                val weather = data.value!!.currentWeather

                if (weather != null) {
                    _currentWeather.value = weather
                    _symbol.value = weather.symbolName.toString()
                    _next24.value = data.value!!.next24h
                    _week.value = data.value!!.week
                    _wStatus.value = statusStates[1]

                    data.value!!.updateGPTCurrent()
                    val response = data.value!!.GPTCurrent
                    _gptCurrent.value = ""

                    // simulate writing
                    response.forEach {
                        _gptCurrent.value += it
                        delay(20)
                    }
                } else {
                    println("Failed fetching current weather for ${data.value!!.location.name}")
                    _wStatus.value = statusStates[2]
                }
            }

        }
    }

    fun updateSunriseAndSunset() {
        viewModelScope.launch {
            _sunStatus.value = statusStates[0]
            data.value!!.updateSunriseAndSunset()
            _rise.value = data.value!!.rise.toString()
            _set.value = data.value!!.set.toString()

            if (data.value!!.rise == null || data.value!!.set == null){
                _sunStatus.value = statusStates[2]
            }
            else{
                _sunStatus.value = statusStates[1]
            }

        }
    }
    fun updateWarning() {
        viewModelScope.launch {
            data.value!!.updateWarning()
            _alerts.value = data.value!!.alertList

        }
    }

    fun updateGPTWeek() {
        viewModelScope.launch {
            launch {
                _gptWeek.value = ""
                while (data.value!!.gptWeek == "") {
                    _gptWeek.value = dotLoading(_gptWeek.value)
                    delay(500)
                }
            }
            launch {
                data.value!!.updateGPTWeek()
                val weekResponse = data.value!!.gptWeek
                _gptWeek.value = ""
                // simulate writing
                weekResponse.forEach {
                    _gptWeek.value += it
                    delay(20)
                }
            }

        }
    }
    private suspend fun dotLoading(input: String): String {
        var dots = input
        if (dots == ". . . "){
            dots = ""
        } else{
            dots += ". "
        }
        return dots
    }
}