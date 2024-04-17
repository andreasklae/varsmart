package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings.Background

class SettingsViewModel(): ViewModel() {
    private val _age = MutableStateFlow(9)
    val age = _age.asStateFlow()
    private val _sliderPosition = MutableStateFlow(0f)
    val sliderPosition = _sliderPosition.asStateFlow()

    private var _hobbies = MutableStateFlow(listOf<String>())
    val hobbies = _hobbies.asStateFlow()

    private var _background = MutableStateFlow(Background.images[0])
    val background = _background.asStateFlow()

    fun changeAgeByFraction(fraction: Float = 0f){
        viewModelScope.launch {
            _age.value = 9 + (16 * fraction).toInt()
        }
    }

    fun changeSliderPosition(newPosition: Float){
        viewModelScope.launch {
            _sliderPosition.value = newPosition
        }
    }

    fun addHobby(hobby: String) {
        viewModelScope.launch {
            val currentHobbies = hobbies.value.toMutableList()
            currentHobbies.add(hobby)
            _hobbies.value = currentHobbies
        }
    }
    fun removeHobby(hobby: String) {
        viewModelScope.launch {
            val currentHobbies = hobbies.value
            _hobbies.value = currentHobbies.filter { it != hobby }
        }
    }

    fun changeBackround(background: Background){
        viewModelScope.launch {
            _background.value = background
        }
    }

}