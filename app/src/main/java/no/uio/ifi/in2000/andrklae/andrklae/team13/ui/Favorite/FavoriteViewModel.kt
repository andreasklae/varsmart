package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepositoryImpl


class FavoriteViewModel() : ViewModel() {
    val locationRepository: LocationRepository = LocationRepositoryImpl()
    var favourites = DataHolder.Favourites

    init {
        loadData()
    }

    // variables for searching

    // variables for bottom sheet
    val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet = _showBottomSheet.asStateFlow()

    fun toggleBottomSheet() {
        // hides / shows bottom sheet
        _showBottomSheet.value = !showBottomSheet.value
    }

    fun loadData() {
        viewModelScope.launch {
            favourites.forEach {
                // loads weather
                launch {
                    if (it.weather == null) {
                        it.updateWeather()
                    }
                }
                // loads warnings
                launch {
                    if (it.alertList.isEmpty()) {
                        it.updateWarning()
                    }
                }
                // loads sunrise and set
                launch {
                    if (it.set == null) {
                        it.updateSunriseAndSunset()
                    }
                }
            }
        }
    }

    fun updateWeather() {
        favourites.forEach {
            viewModelScope.launch {
                it.updateAll()
            }
        }

    }
}