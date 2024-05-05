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

    private val _GPTCurrent = MutableStateFlow("Trykk på meg for å spørre om praktiske tips!")
    val GPTCurrent = _GPTCurrent.asStateFlow()
    private val _GPTCurrentAnimation = MutableStateFlow(MrPraktiskAnimations.BLINK)
    val GPTCurrentAnimation = _GPTCurrentAnimation.asStateFlow()


    private val _GPT24h = MutableStateFlow("Trykk på meg for å spørre om været det neste døgnet")
    val GPT24h = _GPT24h.asStateFlow()
    private val _GPT24hAnimation = MutableStateFlow(MrPraktiskAnimations.BLINK)
    val GPT24hAnimation = _GPT24hAnimation.asStateFlow()

    private val _GPTWeek = MutableStateFlow("Trykk på meg for å spørre om været denne uka")
    val GPTWeek = _GPTWeek.asStateFlow()
    private val _GPTWeekAnimation = MutableStateFlow(MrPraktiskAnimations.BLINK)
    val GPTWeekAnimation = _GPTWeekAnimation.asStateFlow()

    private val _selectedWarning = MutableStateFlow(false)
    val selectedWarning = _selectedWarning.asStateFlow()

    fun updateAll() {
        viewModelScope.launch {
            data.value.updateAll()
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
        val isSame = data.value.location == dataHolder.location

        if (!isSame) {
            viewModelScope.launch {
                if (dataHolder.location.name.uppercase().equals("MIN POSISJON")) {
                    dataHolder.location = DataHolder.locRepo.coordsToCity(
                        dataHolder.location.lat,
                        dataHolder.location.lon
                    )
                        ?.let { customLocation ->
                            customLocation
                        } ?: dataHolder.location
                    println(dataHolder.location.name)
                    _data.value = dataHolder
                } else {
                    _data.value = dataHolder
                }
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
            launch {
                // simulates three dots loading
                while (loading) {
                    _GPTCurrent.value = dotLoading(_GPTCurrent.value)
                    delay(200)
                }
            }
            launch {
                data.value.updateGPTCurrent(age, hobbies)
                // done loading
                loading = false
                _GPTCurrent.value = ""
                _GPTCurrentAnimation.value = MrPraktiskAnimations.SPEAK
                // simulates writing
                data.value.gptCurrent.value.forEach {
                    _GPTCurrent.value += it
                    delay(1)
                }
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
            launch {
                while (loading) {
                    _GPT24h.value = dotLoading(_GPT24h.value)
                    delay(200)
                }
            }
            launch {
                data.value.updateGPT24h(age)
                // done loading
                loading = false
                _GPT24h.value = ""
                _GPT24hAnimation.value = MrPraktiskAnimations.SPEAK
                data.value.gpt24h.value.forEach {
                    _GPT24h.value += it
                    delay(1)
                }
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
            launch {
                // simulates three dots loading
                while (loading) {
                    _GPTWeek.value = dotLoading(_GPTWeek.value)
                    delay(200)
                }
            }
            launch {
                data.value.updateGPTWeek(age, hobbies)
                // done loading
                loading = false
                _GPTWeek.value = ""
                _GPTWeekAnimation.value = MrPraktiskAnimations.SPEAK
                data.value.gptWeek.value.forEach {
                    _GPTWeek.value += it
                    delay(1)
                }
                _GPTWeekAnimation.value = MrPraktiskAnimations.BLINK

            }
        }
    }

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