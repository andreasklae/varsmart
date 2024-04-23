package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Status
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository

class WarningViewModel() : ViewModel() {

    val warningRepositoryInterface: WarningRepositoryInterface = WarningRepository()

    private val _warnings = MutableStateFlow(emptyList<Warning?>())
    val warnings = _warnings.asStateFlow()


    val statusStates = listOf("loading", "success", "failed")
    private val _loadingStatus = MutableStateFlow(Status.LOADING)
    val loadingStatus = _loadingStatus.asStateFlow()

    init {
        loadWarnings()
    }

    fun loadWarnings() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStatus.value = Status.LOADING
            val newList = listOf(warningRepositoryInterface.fetchAllWarnings())
            if (!newList.contains(null)) {
                _warnings.value = newList
                _loadingStatus.value = Status.SUCCESS
            } else {
                _loadingStatus.value = Status.FAILED
            }
        }
    }
}

