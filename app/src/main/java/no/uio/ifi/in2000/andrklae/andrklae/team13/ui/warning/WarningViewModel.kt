package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository

class WarningViewModel() : ViewModel() {

    val warningRepository: WarningRepository = WarningRepository()

    private val _data = MutableStateFlow(emptyList<Warning?>())
    val data = _data.asStateFlow()


    val statusStates = listOf("loading", "success", "failed")
    private val _loadingStatus = MutableStateFlow(statusStates[0])
    val loadingStatus = _loadingStatus.asStateFlow()

    init {
        loadWarnings1()
    }

    fun loadWarnings1() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStatus.value = statusStates[0]
            val newList = listOf(warningRepository.fetchAllWarnings())
            if (!newList.contains(null)) {
                _data.value = newList
                _loadingStatus.value = statusStates[1]
            } else {
                _loadingStatus.value = statusStates[2]
            }
        }
    }
}

