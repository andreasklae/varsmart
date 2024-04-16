package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(): ViewModel() {
    private val _age = MutableStateFlow(9)
    val age = _age.asStateFlow()

    fun changeAgeByFraction(fraction: Double){
        _age.value = 9 + (16 * fraction).toInt()
    }
}