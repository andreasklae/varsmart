package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.MainActivity
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.BottomSheet
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.FavoriteBox
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.FavoriteViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.FunctionRow
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.SearchDialog
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning.WarningViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.getColorFromString
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

@Composable
fun WarningScreen(warningViewModel: WarningViewModel) {
    val warningStatus by warningViewModel.loadingStatus.collectAsState()
    var test by remember { mutableIntStateOf(0) }
    val data by warningViewModel.data.collectAsState()

    when (warningStatus) {
        warningViewModel.statusStates[0] -> {
            Text(text = "Loading")
            CircularProgressIndicator(color = Color.Black)
        }

        warningViewModel.statusStates[1] -> {
            data[0]?.let { LoadWarningScreen(it) }

        }

        warningViewModel.statusStates[2] -> {
            Text(text = "Failed")
            println("Testerr33")
        }
    }

    //DisplayAllWarning()

}

@Composable
fun DisplayAllWarning() {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = MapUiSettings(zoomControlsEnabled = true),
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(59.9, 10.7), 5f)
        }
    ) {
        val alerts = DataHolder.Favourites[0].alertList
        alerts.forEach {
            var isPolygonSelected by remember { mutableStateOf(false) }
            val polygonColor = it.alert.properties.riskMatrixColor
            val polygons = it.polygonList
            val area = it.alert.properties.area
            polygons.forEach { polygon ->
                Polygon(
                    points = polygon.coordinates,
                    clickable = true,
                    fillColor = if (isPolygonSelected)
                        getColorFromString(polygonColor).copy(alpha = 0.8f)
                    else getColorFromString(polygonColor).copy(alpha = 0.5f),
                    strokeColor = Color.Black,
                    strokeWidth = 5f,
                    tag = area,
                    onClick = {
                        // Handle polygon click event
                        if (isPolygonSelected) {
                            isPolygonSelected = false
                        } else {
                            isPolygonSelected = true
                        }
                    }
                )
                if (isPolygonSelected) {
                    Marker(
                        state =
                        rememberMarkerState(
                            position = calculatePolygonCenter(polygon.coordinates)

                        ),
                        title = area,
                        snippet = "${it.alert.properties.eventAwarenessName}: " +
                                "${it.alert.properties.triggerLevel}",
                        icon = BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED),
                        alpha = 0.8f
                    )

                }

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoadWarningScreen(
    warning: Warning
) {
    var showMap by remember { mutableStateOf(false) }
    // sorts favorite list to put current location at the top of the list

    // column of all favourites
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )

            )

    ) {
        // spacer
        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
        // Row for refreshing and editing list
        item {
            Icon(
                Icons.Filled.LocationOn,
                "show all in map",
                modifier = Modifier
                    .clickable {
                        showMap = true
                    }
            )
            Spacer(modifier = Modifier.height(40.dp))
        }

        // boxes for each favourite
        warning.features.forEach {
            item {
                WarningBox(it)
            }
        }

        item {
            Spacer(modifier = Modifier.fillMaxHeight())
        }

    }
    if (showMap) {
        Dialog(onDismissRequest = { showMap = false }) {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.9f)
            ) {
                DisplayAllWarning()
            }

        }

    }
}

@Composable
fun WarningBox(feature: Feature) {
    Text(text = feature.properties.area)
    Spacer(modifier = Modifier.height(30.dp))
}

fun calculatePolygonCenter(polygon: List<LatLng>): LatLng {
    var totalLat = 0.0
    var totalLng = 0.0

    for (point in polygon) {
        totalLat += point.latitude
        totalLng += point.longitude
    }

    val centerLat = totalLat / polygon.size
    val centerLng = totalLng / polygon.size

    return LatLng(centerLat, centerLng)
}

fun getColorFromString(colorString: String): Color {
    return when (colorString.lowercase()) {
        "yellow" -> Color.Yellow
        "green" -> Color.Green
        "orange" -> Color(0xFFFFA500)
        "red" -> Color.Red
        // Add more cases as needed
        else -> Color.Black // Default color or any other color you prefer
    }
}