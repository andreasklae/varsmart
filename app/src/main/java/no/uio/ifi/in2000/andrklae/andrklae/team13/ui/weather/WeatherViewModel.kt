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
    val data: LiveData<DataHolder> = _data

    //val activity = activity

    /* init {
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

    }*/
    @OptIn(ExperimentalFoundationApi::class)
    fun setLocation(i: Int) {
        print("Changing location from ${_data.value?.location?.name} to ${DataHolder.Favourites[i].location.name}")
        val isSame = data.value!!.location == DataHolder.Favourites[i].location

        if (!isSame) {
            viewModelScope.launch {
                _data.value = DataHolder.Favourites[i]
            }

        }
    }
    fun updateMainGpt(){
        viewModelScope.launch{
            if(data.value!!.mainGpt == ""){
                data.value!!.updateGPTCurrent()
            }
        }
    }
    fun updateGPTWeek() {
        viewModelScope.launch {
            data.value!!.updateGPTWeek()
        }
    }

    fun dotLoading(input: String): String {
        var dots = input
        if (dots == ". . . "){
            dots = ""
        } else{
            dots += ". "
        }
        return dots
    }
    /*

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
    } */
}