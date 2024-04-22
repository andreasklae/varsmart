package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.PreferenceManager


class FavoriteViewModel() : ViewModel() {
    val locationRepository: LocationRepositoryInterface = LocationRepository()
    var favourites = DataHolder.Favourites

    // variables for searching

    // variables for bottom sheet
    val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet = _showBottomSheet.asStateFlow()

    fun toggleBottomSheet() {
        // hides / shows bottom sheet
        _showBottomSheet.value = !showBottomSheet.value
    }

    fun loadFavourites(context: Context){
        viewModelScope.launch{
            DataHolder.setFavourites(PreferenceManager.fetchFavourites(context))
        }
    }
    fun updateFavouritesList(context: Context){
        PreferenceManager.saveFavourites(context, favourites)
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