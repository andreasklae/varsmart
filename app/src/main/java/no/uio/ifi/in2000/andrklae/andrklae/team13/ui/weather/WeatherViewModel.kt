package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather

import android.annotation.SuppressLint
import android.util.TypedValue
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity

class WeatherViewModel(
    index: Int,
    activity: MainActivity,
) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    val activity = activity
    private val _data = MutableLiveData<DataHolder>(DataHolder.Favourites[index])
    val data: LiveData<DataHolder> = _data

    private val _GPTMain = MutableStateFlow("Trykk på meg for å spørre om praktiske tips!")
    val GPTMain = _GPTMain.asStateFlow()


    private val _GPTWeek = MutableStateFlow("Trykk på meg for å spørre om været det neste døgnet")
    val GPTWeek = _GPTWeek.asStateFlow()

    fun updateAll() {
        viewModelScope.launch {
            data.value!!.updateAll()
            _GPTMain.value = "Trykk på meg for å spørre om praktiske tips!"
            _GPTWeek.value = "Trykk på meg for å spørre om været det neste døgnet"
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    fun setLocation(i: Int) {
        print(
            "Changing location from ${_data.value?.location?.name}" +
                    " to ${DataHolder.Favourites[i].location.name}"
        )
        val isSame = data.value!!.location == DataHolder.Favourites[i].location

        if (!isSame) {
            viewModelScope.launch {
                _data.value = DataHolder.Favourites[i]
                _GPTMain.value = data.value!!.mainGpt.value
                _GPTWeek.value = data.value!!.weekGpt.value

            }
        }
    }

    fun updateMainGpt() {
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
                data.value!!.updateGPTCurrent()
                // done loading
                loading = false
                _GPTMain.value = ""
                // simulates writing
                data.value!!.mainGpt.value.forEach {
                    _GPTMain.value += it
                    delay(10)

                }
            }
        }
    }

    fun updateGPTWeek() {
        viewModelScope.launch {
            // to keep track of loading status
            var loading = true
            _GPTWeek.value = ""
            launch {
                while (loading) {
                    _GPTWeek.value = dotLoading(_GPTWeek.value)
                    delay(200)
                }
            }
            launch {
                data.value!!.updateGPTWeek()
                // done loading
                loading = false
                _GPTWeek.value = ""
                data.value!!.weekGpt.value.forEach {
                    _GPTWeek.value += it
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