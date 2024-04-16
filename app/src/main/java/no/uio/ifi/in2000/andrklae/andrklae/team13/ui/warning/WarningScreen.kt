package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning.WarningViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.getColorFromString

@Composable
fun WarningScreen(warningViewModel: WarningViewModel = WarningViewModel()) {
    val warningStatus by warningViewModel.loadingStatus.collectAsState()

    when (warningStatus) {
        warningViewModel.statusStates[0] -> {

        }

        warningViewModel.statusStates[1] -> {

        }

        warningViewModel.statusStates[2] -> {

        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        DisplayAllWarning()
    }
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
                            position = LatLng(
                                polygon.coordinates[0].latitude,
                                polygon.coordinates[0].longitude
                            )
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