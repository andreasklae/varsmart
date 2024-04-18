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
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity

class WeatherViewModel(
    dataHolder: DataHolder,
    activity: MainActivity,
) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    val activity = activity
    private val _data = MutableStateFlow(dataHolder)
    val data = _data.asStateFlow()

    private val _GPTMain = MutableStateFlow("Trykk på meg for å spørre om praktiske tips!")
    val GPTMain = _GPTMain.asStateFlow()


    private val _GPT24h = MutableStateFlow("Trykk på meg for å spørre om været det neste døgnet")
    val GPT24h = _GPT24h.asStateFlow()

    fun updateAll() {
        viewModelScope.launch {
            data.value.updateAll()
            _GPTMain.value = "Trykk på meg for å spørre om praktiske tips!"
            _GPT24h.value = "Trykk på meg for å spørre om været det neste døgnet"
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
                _data.value = dataHolder
                if (_data.value.weather == null) {
                    updateAll()
                }

                // if gpt data is already loaded
                if (data.value.mainGpt.value != "") {
                    _GPTMain.value = data.value.mainGpt.value
                } else _GPTMain.value = "Trykk på meg for å spørre om praktiske tips!"

                // if gpt data is already loaded
                if (data.value.mainGpt.value != "") {
                    _GPTMain.value = data.value.mainGpt.value
                } else _GPT24h.value = "Trykk på meg for å spørre om været det neste døgnet"
                _GPT24h.value = data.value.gpt24h.value

            }
        }
    }

    fun updateMainGpt(age: Int, hobbies: List<String>) {
        viewModelScope.launch {
            // to keep track of loading status
            var loading = true
            _GPTMain.value = ""
            launch {
                // simulates three dots loading
                while (loading) {
                    _GPTMain.value = dotLoading(_GPTMain.value)
                    delay(200)
                }
            }
            launch {
                data.value.updateGPTCurrent(age, hobbies)
                // done loading
                loading = false
                _GPTMain.value = ""
                // simulates writing
                data.value.mainGpt.value.forEach {
                    _GPTMain.value += it
                    delay(10)

                }
            }
        }
    }

    fun updateGPT24h(age: Int) {
        viewModelScope.launch {
            // to keep track of loading status
            var loading = true
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
                data.value.gpt24h.value.forEach {
                    _GPT24h.value += it
                    delay(15)
                }
            }
        }
    }

    fun dotLoading(input: String): String {
        var dots = input
        if (dots == ". . . ") {
            dots = ""
        } else {
            dots += ". "
        }
        return dots
    }

    // function for converting sp to dp
    fun spToDp(sp: Float): Float {
        val metrics = activity.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics) / metrics.density
    }

}