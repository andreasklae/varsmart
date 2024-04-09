package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import androidx.compose.runtime.mutableStateOf
import com.aallam.openai.api.BetaOpenAI
import kotlinx.coroutines.delay

class HomeViewModel(index: Int): ViewModel() {
    var data = DataHolder.Favourites[index]

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
    val _warning: MutableStateFlow<Feature?> = MutableStateFlow(null)
    val warning = _warning.asStateFlow()

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
        println("Updating data for ${data.location.name}")
        updateWeather()
        updateWarning()
        updateSunriseAndSunset()
    }
    fun setLocation(i: Int) {
        print("Changing location from ${data.location.name} to ")
        data = DataHolder.Favourites[i]
        println(data.location.name)

        _loc.value = data.location
        updateAll()
    }

    fun updateWeather() {
        viewModelScope.launch {

            // loading dots while waiting for GPT response
            launch {
                while (data.GPTCurrent == ""){
                    _gptCurrent.value = dotLoading(_gptCurrent.value)
                    delay(500)
                }
            }

            launch {
                _wStatus.value = statusStates[0]
                data.updateWeather()
                val weather = data.currentWeather

                if (weather != null) {
                    _currentWeather.value = weather
                    _symbol.value = weather.symbolName.toString()
                    _next24.value = data.next24h
                    _week.value = data.week
                    _wStatus.value = statusStates[1]

                    data.updateGPTCurrent()
                    val response = data.GPTCurrent
                    _gptCurrent.value = ""

                    // simulate writing
                    response.forEach {
                        _gptCurrent.value += it
                        delay(20)
                    }
                } else {
                    println("Failed fetching current weather for ${data.location.name}")
                    _wStatus.value = statusStates[2]
                }
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

    fun updateGPTWeek() {
        viewModelScope.launch {
            launch {
                _gptWeek.value = ""
                while (data.gptWeek == "") {
                    _gptWeek.value = dotLoading(_gptWeek.value)
                    delay(500)
                }
            }
            launch {
                data.updateGPTWeek()
                val weekResponse = data.gptWeek
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