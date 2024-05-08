package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Search

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.PreferenceManager
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Status

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    searchVm: SearchViewModel,
    toggleDialog: () -> Unit,
    functionToPerform: (DataHolder) -> Unit,
    navigateToHome: (Int) -> Unit
) {
    val searchStatus by searchVm.searchStatus.collectAsState()
    val searchText by searchVm.searchText.collectAsState()
    val searchResults by searchVm.searchResults.collectAsState()

    // search dialog pop up
    Dialog(onDismissRequest = {
        toggleDialog()
        searchVm.emptySearchresults()
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.8f)
                    .clip(RoundedCornerShape(15.dp))
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .padding(top = 20.dp, bottom = 0.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                )
                {
                    // search box
                    SearchBox(
                        searchText = searchText,
                        onSearchChange = { text -> searchVm.changeSearchText(text) },
                        toggleSearching = { bool -> searchVm.toggleSearching(bool) }
                    )
                    when (searchStatus) {
                        // loading searches
                        Status.LOADING -> {
                            Spacer(modifier = Modifier.height(20.dp))
                            CircularProgressIndicator(color = Color.Black)
                        }
                        // found location(s)
                        Status.SUCCESS -> {
                            Box(
                                modifier = Modifier
                                    // creates a fade on the top and bottom of the scrolling column
                                    .drawWithContent {
                                        drawContent()
                                        drawRect(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.White,
                                                    Color.White.copy(alpha = 0.3f),
                                                    Color.Transparent,
                                                    Color.Transparent,
                                                    Color.Transparent,
                                                    Color.Transparent,
                                                    Color.Transparent,
                                                    Color.Transparent,
                                                    Color.Transparent,
                                                    Color.Transparent,
                                                    Color.Transparent,
                                                    Color.Transparent,
                                                    Color.White.copy(alpha = 0.5f),
                                                    Color.White
                                                ),
                                                startY = 0f,
                                                endY = size.height
                                            ),
                                            alpha = 1f
                                        )
                                    }
                            ) {
                                // shows list of search results
                                SearchResults(
                                    searchResults = searchResults,
                                    functionToPerform = { data -> functionToPerform(data) },
                                    navigateToHome = { page: Int -> navigateToHome(page) },
                                    toggleDialog = { searchVm.toggleSearchDialog() }
                                )
                            }
                        }
                        // found no location
                        Status.FAILED -> {
                            Text(text = "Fant ingen resultater")
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(20.dp))
            // dismiss button
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .wrapContentWidth()
                    .clickable { searchVm.toggleSearchDialog() }
                    .padding(15.dp)

            ) {
                Icon(Icons.Filled.Close, "fjern fra favoritter")
            }
            Spacer(modifier = Modifier.weight(1f))

        }

    }

}

// scrollable column of search results
@Composable
fun SearchResults(
    searchResults: List<CustomLocation>,
    functionToPerform: (DataHolder) -> Unit,
    navigateToHome: (Int) -> Unit,
    toggleDialog: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        // for each location matching the search
        searchResults.forEach { location ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(15.dp)
                    .clickable {
                        val data = DataHolder(location)
                        functionToPerform(data)
                        navigateToHome(0)
                        toggleDialog()
                    }

            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "${location.name}",
                        fontSize = 20.sp,
                    )
                    Text(
                        text = location.postSted + ", " + location.fylke,
                        fontSize = 10.sp,
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                // if location allready exists
                if (DataHolder.Favourites.any { it.location == location }) {
                    val toastString = "${location.name} fjernet fra favoritter"
                    // button for toggeling the location in favourites
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE1E1E1))
                            .wrapContentWidth()
                            .clickable {
                                DataHolder.Favourites.remove(
                                    DataHolder.Favourites.find { it.location == location }
                                )
                                PreferenceManager.saveFavourites(context, DataHolder.Favourites)
                                Toast
                                    .makeText(context, toastString, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            .padding(10.dp)
                    ) {
                        Icon(Icons.Filled.Bookmark, "fjern fra favoritter")
                    }
                } else {
                    val toastString = "${location.name} lagt til i favoritter"
                    // button for toggeling the location in favourites
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE1E1E1))
                            .wrapContentWidth()
                            .clickable {
                                val newLocation = DataHolder(location)
                                newLocation.toggleInFavourites()
                                PreferenceManager.saveFavourites(context, DataHolder.Favourites)
                                Toast
                                    .makeText(context, toastString, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            .padding(10.dp)
                    ) {
                        Icon(
                            Icons.Filled.BookmarkBorder,
                            "legg til sted i favoritter"
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(10.dp))

        }
        Spacer(modifier = Modifier.height(50.dp))

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(
    searchText: String,
    onSearchChange: (String) -> Unit,
    toggleSearching: (Boolean) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        value = searchText,
        onValueChange = { onSearchChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear Text",
                    modifier = Modifier.clickable {
                        onSearchChange("")
                        keyboardController?.hide()// Optionally hide the keyboard when clearing text
                    }
                )
            }
        },
        placeholder = { Text("Søk...") },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            toggleSearching(false)
            keyboardController?.hide()  // Hide the keyboard when the user confirms the search
        })
    )
}