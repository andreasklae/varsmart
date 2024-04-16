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
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning

class WarningViewModel() : ViewModel() {

    private val _data = MutableStateFlow(listOf<Warning?>())
    val data = _data.asStateFlow()

    var warning = DataHolder.aRepo

    val statusStates = listOf("loading", "success", "failed")
    private val _loadingStatus = MutableStateFlow(statusStates[1])
    val loadingStatus = _loadingStatus.asStateFlow()

    fun loadSearch() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStatus.value = statusStates[0]
            val newList = listOf(warning.fetchAllWarnings())
            if (newList.isNotEmpty()) {
                _data.value = newList
                _loadingStatus.value = statusStates[1]
            } else {
                _loadingStatus.value = statusStates[2]
            }
        }
    }
}

