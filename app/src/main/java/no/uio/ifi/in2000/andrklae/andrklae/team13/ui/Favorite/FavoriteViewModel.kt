package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationRepository
import java.io.IOException


class FavoriteViewModel() : ViewModel() {
    val locationRepository: LocationRepository = LocationRepository()
    var favourites = DataHolder.Favourites
    var locationsUiState by mutableStateOf(listOf<CustomLocation>())
    init{
        loadData()
    }

    fun loadData(){
        viewModelScope.launch {
            favourites.forEach{
                launch {
                    if (it.weather == null){
                        it.updateWeather()
                    }
                }
                launch {
                    if (it.alertList.isEmpty()){
                        it.updateWarning()
                    }
                }
                launch {
                    if (it.set == null){
                        it.updateSunriseAndSunset()
                    }
                }
            }
        }
    }

    fun updateFavouriteList(){
        favourites = DataHolder.Favourites
    }
    fun loadSearch(text: String){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                Search(text)
            }
            catch (e: IOException){

            }
        }
    }

    suspend fun Search(locationName: String){
        locationsUiState = locationRepository.getLocations(locationName)
    }
}