package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationRepository
import java.io.IOException

data class favouritesUiState(
    val favourites: MutableList<DataHolder>
)

class FavoriteViewModel() : ViewModel() {
    val locationRepository: LocationRepository= LocationRepository()
    var favouritesUiState by mutableStateOf(favouritesUiState(favourites = mutableListOf<DataHolder>()))
    var locationsUiState by  mutableStateOf(listOf<CustomLocation>())

    init{
        println(favouritesUiState.favourites.size)
    }

    fun loadFavourites(location:CustomLocation){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                Add(location)
                println(favouritesUiState.favourites)

            } catch (e: IOException) {

            }

        }
    }

     fun loadSearch(text:String){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                Search(text)
               // println(locationsUiState)
            } catch (e: IOException) {

            }

        }
    }

    //Replace forEach later on
  /*  fun Remove(favourite: DataHolder) {
        favouritesUiState.favourites.forEach {
            if (it == favourite) {
                favouritesUiState.favourites.remove(it)
            }
        }
    }*/

    suspend fun Add(location: CustomLocation){
        println(favouritesUiState.favourites.size)

        val favourite: DataHolder = DataHolder(location)
        println("Legger til: ")
        println(location.name)
        println("Locations now: ")
        favouritesUiState.favourites.add(favourite)
        favouritesUiState.favourites.forEach {it ->
            println(it.location.name)
        }

    }

    //give notice about an already existing favourite

    suspend fun Search(locationName: String){
        locationsUiState = locationRepository.getLocations(locationName)
    }
}