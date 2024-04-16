package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning

class SettingsViewModel(): ViewModel() {
    private val _age = MutableStateFlow(9)
    val age = _age.asStateFlow()

    private val _hobbies = MutableStateFlow(mutableListOf<String>("test"))
    val hobbies = _hobbies.asStateFlow()

    fun changeAgeByFraction(fraction: Double){
        _age.value = 9 + (16 * fraction).toInt()
    }

    fun addHobby(hobby: String) {
        _hobbies.value.add(hobby)
    }
    fun removeHobby(hobby: String) {
        _hobbies.value.remove(hobby)
    }

}