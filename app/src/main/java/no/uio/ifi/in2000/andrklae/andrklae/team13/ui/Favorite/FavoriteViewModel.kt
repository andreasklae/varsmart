package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationRepository
import java.io.IOException

data class favouritesUiState(
    val favourites: MutableList<DataHolder>
)

class FavoriteViewModel() : ViewModel() {
    val locationRepository: LocationRepository= LocationRepository()
    var favouritesUiState by mutableStateOf(favouritesUiState(favourites = mutableListOf()))


    init{
    }

    private fun loadFavourites(){
        viewModelScope.launch(Dispatchers.IO) {
            try{

            } catch (e: IOException) {

            }

        }
    }

    //Replace forEach later on
    fun Remove(favourite: DataHolder) {
        favouritesUiState.favourites.forEach {
            if (it == favourite) {
                favouritesUiState.favourites.remove(it)
            }
        }
    }

    suspend fun Add(locationName: String){
        val location = locationRepository.getLocations(locationName).first()
        val favourite: DataHolder = DataHolder(location)
        favouritesUiState.favourites.add(favourite)
    }

}