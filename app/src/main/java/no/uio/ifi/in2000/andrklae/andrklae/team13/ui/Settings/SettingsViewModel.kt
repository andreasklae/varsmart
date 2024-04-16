package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning

class SettingsViewModel(): ViewModel() {
    private val _age = MutableStateFlow(9)
    val age = _age.asStateFlow()

    private var _hobbies = MutableStateFlow(listOf<String>())
    val hobbies = _hobbies.asStateFlow()

    fun changeAgeByFraction(fraction: Double){
        _age.value = 9 + (16 * fraction).toInt()
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

}