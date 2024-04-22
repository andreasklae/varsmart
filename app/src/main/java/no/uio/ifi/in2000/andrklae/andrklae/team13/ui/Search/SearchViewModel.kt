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
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepositoryImpl
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Status

class SearchViewModel: ViewModel() {
    val locationRepository: LocationRepository = LocationRepositoryImpl()
    var favourites = DataHolder.Favourites

    val statusStates = listOf("loading", "success", "failed")
    private val _searchStatus = MutableStateFlow(Status.SUCCESS)
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
        // resets the dialog and hides it
        changeSearchText("")
        emptySearchresults()
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
            _searchStatus.value = Status.LOADING
            val newList = locationRepository.getLocations(search)
            if (newList.isNotEmpty()){
                _searchResults.value = newList
                _searchStatus.value = Status.SUCCESS
            }
            else{
                _searchStatus.value = Status.FAILED
            }
        }
    }

    fun toggleSearching(changeTo: Boolean = !_isSearching.value) {
        _isSearching.value = changeTo
    }

}