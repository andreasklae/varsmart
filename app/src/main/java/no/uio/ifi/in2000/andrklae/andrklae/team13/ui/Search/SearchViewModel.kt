package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepository

class SearchViewModel: ViewModel() {
    val locationRepository: LocationRepository = LocationRepository()
    var favourites = DataHolder.Favourites

    val statusStates = listOf("loading", "success", "failed")
    private val _searchStatus = MutableStateFlow(statusStates[1])
    val searchStatus = _searchStatus.asStateFlow()

    private val _showSearchDialog = MutableStateFlow(false)
    val showSearchDialog = _showSearchDialog.asStateFlow()

    private val _searchResults = MutableStateFlow(listOf<CustomLocation>())
    val searchResults = _searchResults.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    fun toggleSearchDialog(){
        _showSearchDialog.value = !_showSearchDialog.value
    }

    fun emptySearchresults(){
        // empties the list
        _searchResults.value = listOf()
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

    fun toggleSearching(changeTo: Boolean = !_isSearching.value) {
        _isSearching.value = changeTo
        // resets the dialog
        _searchText.value = ""
        emptySearchresults()
    }

}