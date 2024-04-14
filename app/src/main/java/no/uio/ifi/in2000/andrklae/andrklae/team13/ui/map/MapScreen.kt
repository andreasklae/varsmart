package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.ktx.model.polygonOptions
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Alert
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Polygon
import java.io.File
import javax.annotation.Nullable

@Composable
fun AllPolygons(list: List<Alert>) {
    val polys = mutableListOf<Polygon>()
    list.forEach {

    }
}

@Composable
fun MapWithPolygon(alert: Alert) {
    val polygon = alert.polygonList
    val polygonColor = alert.alert.properties.riskMatrixColor
    val area = alert.alert.properties.area
    val centers = mutableListOf<LatLng>()
    polygon.forEach { poly ->
        centers.add(calculatePolygonCenter(poly.coordinates))
    }

    // Create a mutable state to track whether the polygon is selected
    var isPolygonSelected by remember { mutableStateOf(false) }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = rememberCameraPositionState {

            position = CameraPosition.fromLatLngZoom(calculatePolygonCenter(centers), 7f)
        }
    ) {

        polygon.forEach { it ->
            Polygon(
                points = it.coordinates,
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
                    state = rememberMarkerState(position = centers[polygon.indexOf(it)]),
                    title = area,
                    snippet = "${alert.alert.properties.eventAwarenessName}: " +
                            "${alert.alert.properties.triggerLevel}",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                    alpha = 0.8f
                )
            }

        }

    }
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