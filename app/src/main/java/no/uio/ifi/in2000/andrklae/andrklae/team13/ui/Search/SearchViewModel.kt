package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Status

class SearchViewModel: ViewModel() {
    val locationRepository: LocationRepositoryInterface = LocationRepository()
    var favourites = DataHolder.Favourites

    // status of the api
    private val _searchStatus = MutableStateFlow(Status.SUCCESS)
    val searchStatus = _searchStatus.asStateFlow()

    // whether to show the dialog or not
    private val _showSearchDialog = MutableStateFlow(false)
    val showSearchDialog = _showSearchDialog.asStateFlow()

    // list of the search results
    private val _searchResults = MutableStateFlow(listOf<CustomLocation>())
    val searchResults = _searchResults.asStateFlow()

    // the text in the search field
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // weather the user is currently typing in the search field
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    // shows / hides the search dialog
    fun toggleSearchDialog(){
        changeSearchText("")
        emptySearchresults()
        _showSearchDialog.value = !_showSearchDialog.value
    }

    fun emptySearchresults(){
        // empties the list
        _searchResults.value = listOf()
    }

    // when the search text field is changing as the user types
    fun changeSearchText(text: String){
        // resets the list of results
        emptySearchresults()
        _searchText.value = text
        // if its not an empty string, search on text change
        if (text != ""){
            // loads results of the search
            loadSearch(text)
        }
    }

    // fetches search results from the api
    fun loadSearch(search: String){
        viewModelScope.launch(Dispatchers.IO) {
            _searchStatus.value = Status.LOADING
            val newList = locationRepository.getLocations(search)

            // if the api returns one or more locations
            if (newList.isNotEmpty()){
                _searchResults.value = newList
                _searchStatus.value = Status.SUCCESS
            }

            // if no locations matches the search or the api fails
            else{
                _searchStatus.value = Status.FAILED
            }
        }
    }

    fun toggleSearching(changeTo: Boolean = !_isSearching.value) {
        _isSearching.value = changeTo
    }

}