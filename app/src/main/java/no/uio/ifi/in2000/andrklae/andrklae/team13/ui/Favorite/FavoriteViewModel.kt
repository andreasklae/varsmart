package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationRepository


class FavoriteViewModel() : ViewModel() {
    val locationRepository: LocationRepository = LocationRepository()
    var favourites = DataHolder.Favourites
    init{
        loadData()
    }

    fun loadData(){
        viewModelScope.launch {
            favourites.forEach{
                launch {
                    it.updateWeather()
                }
                launch {
                    it.updateWarning()
                }
                launch {
                    it.updateSunriseAndSunset()
                }
            }
        }
    }

    fun updateFavouriteList(){
        favourites = DataHolder.Favourites
    }
}