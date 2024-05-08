package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Status
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository

class WarningViewModel() : ViewModel() {

    val warningRepo: WarningRepositoryInterface = WarningRepository()

    private val _warnings = MutableStateFlow(emptyList<Warning?>())
    val warnings = _warnings.asStateFlow()

    private val _loadingStatus = MutableStateFlow(Status.LOADING)
    val loadingStatus = _loadingStatus.asStateFlow()

    // warning selected on the map (is null when no warning is selected)
    private val _selectedWarning = MutableStateFlow<Feature?>(null)
    val selectedWarning = _selectedWarning.asStateFlow()

    init {
        loadWarnings()
    }

    // fetch warnings from api
    fun loadWarnings() {
        viewModelScope.launch(Dispatchers.IO) {
            // sets status to loading
            _loadingStatus.value = Status.LOADING

            // list of warnings from the api
            val newList = listOf(warningRepo.fetchAllWarnings())

            // if api call is successful
            if (!newList.contains(null)) {
                _warnings.value = newList
                _loadingStatus.value = Status.SUCCESS
            } else {
                _loadingStatus.value = Status.FAILED
            }
        }
    }

    // sets which warning to display on the map
    fun setPreview(feature: Feature) {
        _selectedWarning.value = feature
    }

    // deselect the warning on the map
    fun resetPreview() {
        _selectedWarning.value = null
    }
}

