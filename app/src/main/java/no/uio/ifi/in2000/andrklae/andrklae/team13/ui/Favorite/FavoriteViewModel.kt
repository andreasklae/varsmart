package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationRepository


class FavoriteViewModel() : ViewModel() {
    val locationRepository: LocationRepository = LocationRepository()
    var favourites = DataHolder.Favourites
    val statusStates = listOf<String>("loading", "success", "failed")
    init{
        loadData()
    }

    fun loadData(){
        viewModelScope.launch {
            favourites.forEach{
                if (it.weather == null){
                    it.status.value = statusStates[0]
                    it.updateAll()
                    if (it.weather == null){
                        it.status.value = statusStates[2]
                    } else{
                        it.status.value = statusStates[1]
                    }
                }
                else{
                    it.status.value = statusStates[1]
                }
            }

        }

    }

    fun updateFavouriteList(){
        favourites = DataHolder.Favourites
    }
}