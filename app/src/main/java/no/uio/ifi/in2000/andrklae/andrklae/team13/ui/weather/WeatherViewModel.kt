package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather

import android.annotation.SuppressLint
import android.util.TypedValue
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT.MrPraktiskAnimations
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity

class WeatherViewModel(
    dataHolder: DataHolder,
    activity: MainActivity,
) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    val activity = activity
    private val _data = MutableStateFlow(dataHolder)
    val data = _data.asStateFlow()

    // variables for GPT message for the current weather
    private val _GPTCurrent = MutableStateFlow("Trykk på meg for å spørre om praktiske tips!")
    val GPTCurrent = _GPTCurrent.asStateFlow()
    private val _GPTCurrentAnimation = MutableStateFlow(MrPraktiskAnimations.BLINK)
    val GPTCurrentAnimation = _GPTCurrentAnimation.asStateFlow()

    // Variables for GPT message for the next 24 hours
    private val _GPT24h = MutableStateFlow("Trykk på meg for å spørre om været det neste døgnet")
    val GPT24h = _GPT24h.asStateFlow()
    private val _GPT24hAnimation = MutableStateFlow(MrPraktiskAnimations.BLINK)
    val GPT24hAnimation = _GPT24hAnimation.asStateFlow()

    // Variables for GPT message for the next week
    private val _GPTWeek = MutableStateFlow("Trykk på meg for å spørre om været denne uka")
    val GPTWeek = _GPTWeek.asStateFlow()
    private val _GPTWeekAnimation = MutableStateFlow(MrPraktiskAnimations.BLINK)
    val GPTWeekAnimation = _GPTWeekAnimation.asStateFlow()

    // variable for what warning is selected in the map
    private val _selectedWarning = MutableStateFlow(false)
    val selectedWarning = _selectedWarning.asStateFlow()

    fun updateAll() {
        viewModelScope.launch {
            // updates all variables
            data.value.updateAll()

            // resets Mr. Praktisk message
            _GPTCurrent.value = "Trykk på meg for å spørre om praktiske tips!"
            _GPT24h.value = "Trykk på meg for å spørre om været det neste døgnet"
            _GPTWeek.value = "Trykk på meg for å spørre om været denne uka"
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    fun setLocation(dataHolder: DataHolder) {
        print(
            "Changing location from ${_data.value.location.name}" +
                    " to ${dataHolder.location.name}"
        )

        // sees if the data being navigated to is already loaded in the viewmodel
        val isSame = data.value.location == dataHolder.location

        // if the location is changed, instead of navigating back
        // to the location already loaded
        if (!isSame) {
            viewModelScope.launch {
                // change the data to show in the screen
                _data.value = dataHolder

                // if data is not loaded previously
                if (_data.value.weather == null) {
                    updateAll()
                }

                // if gpt current data is already loaded
                if (data.value.gptCurrent.value != "") {
                    _GPTCurrent.value = data.value.gptCurrent.value
                } else _GPTCurrent.value = "Trykk på meg for å spørre om praktiske tips!"

                // if gpt 24h data is already loaded
                if (data.value.gpt24h.value != "") {
                    _GPTCurrent.value = data.value.gptCurrent.value
                } else _GPT24h.value = "Trykk på meg for å spørre om været det neste døgnet"

                // if gpt week data is already loaded
                if (data.value.gptWeek.value != "") {
                    _GPTWeek.value = data.value.gptWeek.value
                } else _GPT24h.value = "Trykk på meg for å spørre om været denne uka"

            }
        }
    }

    fun updateGptCurrent(age: Int, hobbies: List<String>) {
        viewModelScope.launch {
            // to keep track of loading status
            var loading = true
            _GPTCurrentAnimation.value = MrPraktiskAnimations.THINKING
            _GPTCurrent.value = ""

            // show Mr. Praktisk thinking while the API is loading
            launch {
                // simulates three dots loading
                while (loading) {
                    _GPTCurrent.value = dotLoading(_GPTCurrent.value)
                    delay(200)
                }
            }

            launch {
                // fetches gpt message from api
                data.value.updateGPTCurrent(age, hobbies)
                // done loading
                loading = false
                _GPTCurrent.value = ""

                // changes animation of avatar
                _GPTCurrentAnimation.value = MrPraktiskAnimations.SPEAK

                // simulates writing
                data.value.gptCurrent.value.forEach {
                    _GPTCurrent.value += it
                    delay(1)
                }

                // changes animation of avatar
                _GPTCurrentAnimation.value = MrPraktiskAnimations.BLINK

            }
        }
    }

    fun updateGPT24h(age: Int) {
        viewModelScope.launch {
            // to keep track of loading status
            var loading = true
            _GPT24hAnimation.value = MrPraktiskAnimations.THINKING
            _GPT24h.value = ""

            // show Mr. Praktisk thinking while the API is loading
            launch {
                while (loading) {
                    _GPT24h.value = dotLoading(_GPT24h.value)
                    delay(200)
                }
            }
            launch {
                // fetches gpt message from api
                data.value.updateGPT24h(age)
                // done loading
                loading = false
                _GPT24h.value = ""

                // changes animation of avatar
                _GPT24hAnimation.value = MrPraktiskAnimations.SPEAK

                // simulates writing
                data.value.gpt24h.value.forEach {
                    _GPT24h.value += it
                    delay(1)
                }

                // changes animation of avatar
                _GPT24hAnimation.value = MrPraktiskAnimations.BLINK
            }
        }
    }

    fun updateGptWeek(age: Int, hobbies: List<String>) {
        viewModelScope.launch {
            _GPTWeekAnimation.value = MrPraktiskAnimations.THINKING
            // to keep track of loading status
            var loading = true
            _GPTWeek.value = ""

            // show Mr. Praktisk thinking while the API is loading
            launch {
                // simulates three dots loading
                while (loading) {
                    _GPTWeek.value = dotLoading(_GPTWeek.value)
                    delay(200)
                }
            }
            launch {
                // fetches gpt message from api
                data.value.updateGPTWeek(age, hobbies)
                // done loading
                loading = false
                _GPTWeek.value = ""

                // changes animation of avatar
                _GPTWeekAnimation.value = MrPraktiskAnimations.SPEAK

                // simulates writing
                data.value.gptWeek.value.forEach {
                    _GPTWeek.value += it
                    delay(1)
                }

                // changes animation of avatar
                _GPTWeekAnimation.value = MrPraktiskAnimations.BLINK

            }
        }
    }

    // simulating three dots loading for GPT speech bubble while Mr. Praktisk is "thinking"
    fun dotLoading(input: String): String {
        var dots = input
        if (dots == "• • • ") {
            dots = ""
        } else {
            dots += "• "
        }
        return dots
    }

    // function for converting sp to dp
    fun spToDp(sp: Float): Float {
        val metrics = activity.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics) / metrics.density
    }

    fun setPreview() {
        _selectedWarning.value = true
    }

    fun resetPreview() {
        _selectedWarning.value = false
    }

}