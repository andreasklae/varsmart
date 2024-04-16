package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
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

    val statusStates = listOf("loading", "success", "failed")
    private val _searchStatus = MutableStateFlow(statusStates[1])
    val searchStatus = _searchStatus.asStateFlow()


    // variables for bottom sheet
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
        // if its not an empty string, search on text change
        if (text != ""){
            loadSearch(text)
        }
    }

    fun loadSearch(search: String){
        viewModelScope.launch(Dispatchers.IO) {
            _searchStatus.value = statusStates[0]
            val newList = locationRepository.getLocations(search)
            if (newList.isNotEmpty()){
                _searchResults.value = newList
                _searchStatus.value = statusStates[1]
            }
            else{
                _searchStatus.value = statusStates[2]
            }
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

    private val _lastUpdate = MutableStateFlow(
        DataHolder.Favourites.sortedWith(
            compareBy<DataHolder> {it.lastUpdate.hour.toInt() }.thenBy { it.lastUpdate.minute.toInt()}
        ).first().lastUpdate
    )

    fun updateWeather(){
        favourites.forEach{
            viewModelScope.launch {
                it.updateAll()
            }
        }

    }

    fun emptySearchresults(){
        // empties the list
        _searchResults.value = listOf()
    }
}