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

    // variables for age and slider
    private val _age = MutableStateFlow(initAge)
    val age = _age.asStateFlow()
    private val _sliderPosition = MutableStateFlow(ageIntToFraction(_age.value))
    val sliderPosition = _sliderPosition.asStateFlow()

    // list of hobbies
    private var _hobbies = MutableStateFlow(initHobbies)
    val hobbies = _hobbies.asStateFlow()

    // background image
    private var _background = MutableStateFlow(Background.images[initBackground])
    val background = _background.asStateFlow()

    // mathematical function for converting slider position (float) to age between 13 and 25 (Int)
    fun changeAgeByFraction(fraction: Float = 0f, context: Context) {
        viewModelScope.launch {
            _age.value = 13 + ((25 - 13) * fraction).toInt()

            // stores the age for long term memory
            PreferenceManager.saveAge(context, _age.value)
        }
    }

    // mathematical function for converting age (between 13 and 25) to slider position (float)
    fun ageIntToFraction(int: Int): Float{
        val interval = (25 - 13)
        val fraction = (int - 13).toFloat() / interval.toFloat()
        return fraction
    }


    // change the position of the slider
    fun changeSliderPosition(newPosition: Float) {
        viewModelScope.launch {
            _sliderPosition.value = newPosition
        }
    }

    // function for adding a hobby to the list of hobbies
    fun addHobby(hobby: String, context: Context) {
        viewModelScope.launch {
            // adds the hobby to the list
            val currentHobbies = hobbies.value.toMutableList()
            currentHobbies.add(hobby)
            _hobbies.value = currentHobbies

            // stores the list for long term memory
            PreferenceManager.saveHobbies(context, _hobbies.value)
        }
    }

    // function for removing a hobby from the list of hobbies
    fun removeHobby(hobby: String, context: Context) {
        viewModelScope.launch {
            // removes the hobby
            val currentHobbies = hobbies.value
            _hobbies.value = currentHobbies.filter { it != hobby }

            // stores the list for long term memory
            PreferenceManager.saveHobbies(context, _hobbies.value)
        }
    }

    // function for changing the background
    fun changeBackround(background: Background, context: Context) {
        viewModelScope.launch {
            _background.value = background

            PreferenceManager.saveBackgroundIndex(context, Background.images.indexOf(_background.value))
        }
    }

}