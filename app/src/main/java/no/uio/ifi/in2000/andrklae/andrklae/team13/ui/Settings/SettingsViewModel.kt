package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.PreferenceManager
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings.Background

class SettingsViewModel(
    initAge: Int,
    initHobbies: List<String>,
    initBackground: Int
    ) : ViewModel() {
    private val _age = MutableStateFlow(initAge)
    val age = _age.asStateFlow()
    private val _sliderPosition = MutableStateFlow(ageIntToFraction(_age.value))
    val sliderPosition = _sliderPosition.asStateFlow()

    private var _hobbies = MutableStateFlow(initHobbies)
    val hobbies = _hobbies.asStateFlow()

    private var _background = MutableStateFlow(Background.images[initBackground])
    val background = _background.asStateFlow()

    fun changeAgeByFraction(fraction: Float = 0f, context: Context) {
        viewModelScope.launch {
            _age.value = 13 + ((25 - 13) * fraction).toInt()
            PreferenceManager.saveAge(context, _age.value)
        }
    }

    fun ageIntToFraction(int: Int): Float{
        val interval = (25 - 13)
        val fraction = (int - 13).toFloat() / interval.toFloat()
        return fraction
    }

    fun changeSliderPosition(newPosition: Float) {
        viewModelScope.launch {
            _sliderPosition.value = newPosition
        }
    }

    fun addHobby(hobby: String, context: Context) {
        viewModelScope.launch {
            val currentHobbies = hobbies.value.toMutableList()
            currentHobbies.add(hobby)
            _hobbies.value = currentHobbies

            PreferenceManager.saveHobbies(context, _hobbies.value)
        }
    }

    fun removeHobby(hobby: String, context: Context) {
        viewModelScope.launch {
            val currentHobbies = hobbies.value
            _hobbies.value = currentHobbies.filter { it != hobby }

            PreferenceManager.saveHobbies(context, _hobbies.value)
        }
    }

    fun changeBackround(background: Background, context: Context) {
        viewModelScope.launch {
            _background.value = background

            PreferenceManager.saveBackgroundIndex(context, Background.images.indexOf(_background.value))
        }
    }

}