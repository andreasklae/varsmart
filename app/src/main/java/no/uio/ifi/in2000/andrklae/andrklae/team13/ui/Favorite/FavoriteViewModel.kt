package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationRepository


class FavoriteViewModel() : ViewModel() {
    val locationRepository: LocationRepository = LocationRepository()
    var favourites = DataHolder.Favourites
    init{
        loadData()
    }

    // variables for searching
    private val _showSearch = MutableStateFlow(false)
    val showSearch = _showSearch.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchResults = MutableStateFlow(listOf<CustomLocation>())
    val searchResults = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet = _showBottomSheet.asStateFlow()

    fun toggleBottomSheet(){
        // hides / shows bottom sheet
        _showBottomSheet.value = !showBottomSheet.value
        // resets search bar
        changeSearchText("")
        _isSearching.value = false
    }
    fun toggleSearchDialog(){
        _showSearch.value = !_showSearch.value
    }
    fun toggleSearching(changeTo: Boolean = !_isSearching.value) {
        _isSearching.value = changeTo
    }

    fun changeSearchText(text: String){
        emptySearchresults()
        _searchText.value = text
        // if its not an empty search
        if (text != ""){
            loadSearch(text)
        }
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
    fun loadSearch(search: String){
        viewModelScope.launch(Dispatchers.IO) {
            // fetches from api
            _searchResults.value = locationRepository.getLocations(search)
        }
    }
    fun emptySearchresults(){
        // empties the list
        _searchResults.value = listOf()
    }
}