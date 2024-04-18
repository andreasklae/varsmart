package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Search

import android.annotation.SuppressLint
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDialog(
    searchVm: SearchViewModel,
    functionToPerform: (DataHolder) -> Unit
) {
    val searchStatus by searchVm.searchStatus.collectAsState()
    val searchText by searchVm.searchText.collectAsState()
    val searchResults by searchVm.searchResults.collectAsState()

    Dialog(onDismissRequest = {
        searchVm.toggleSearchDialog()
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
                    SearchBox(
                        searchText = searchText,
                        onSearchChange = { text -> searchVm.changeSearchText(text) },
                        toggleSearching = { bool -> searchVm.toggleSearching(bool) }
                    )
                    when (searchStatus){
                        // loading searches
                        searchVm.statusStates[0] -> {
                            Spacer(modifier = Modifier.height(20.dp))
                            CircularProgressIndicator(color = Color.Black)
                        }
                        // found location(s)
                        searchVm.statusStates[1] -> {
                            Box (
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
                                SearchResults(
                                    searchResults = searchResults,
                                    functionToPerform = { data -> functionToPerform(data)},
                                    toggleDialog = { searchVm.toggleSearchDialog() }
                                )
                            }
                        }
                        // found no location
                        searchVm.statusStates[2] -> {
                            Text(text = "Fant ingen resultater")
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .wrapContentWidth()
                    .clickable { searchVm.toggleSearchDialog() }
                    .padding(15.dp)

            ) {
                Icon(Icons.Filled.Close,"fjern fra favoritter")
            }
            Spacer(modifier = Modifier.weight(1f))

        }

    }

}

@Composable
fun SearchResults(
    searchResults: List<CustomLocation>,
    functionToPerform: (DataHolder) -> Unit, // different search dialogs have different functions
    toggleDialog: () -> Unit
    ) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        searchResults.forEach { location ->
            Row (
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
                        toggleDialog()
                    }

            ){
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
                if (DataHolder.Favourites.any{
                        it.location.name == location.name
                                &&
                        it.location.fylke == location.fylke
                    }
                ){
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE1E1E1))
                            .wrapContentWidth()
                            .clickable {
                                DataHolder.Favourites.remove(
                                    DataHolder.Favourites.find {
                                        it.location.name == location.name
                                                &&
                                        it.location.fylke == location.fylke
                                    }
                                )
                            }
                            .padding(10.dp)
                    ) {
                        Icon(Icons.Filled.Bookmark,"fjern fra favoritter")
                    }
                } else{
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE1E1E1))
                            .wrapContentWidth()
                            .clickable {
                                val newLocation = DataHolder(location)
                                newLocation.toggleInFavourites()
                            }
                            .padding(10.dp)
                    ) {
                        Icon(Icons.Filled.BookmarkBorder,"legg til sted i favoritter")
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

    OutlinedTextField(
        value = searchText,
        onValueChange = { onSearchChange(it)},
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear Text",
                    modifier = Modifier.clickable {
                        onSearchChange("")
                        keyboardController?.hide()  // Optionally hide the keyboard when clearing text
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