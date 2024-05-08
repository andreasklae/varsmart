package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.currentCameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Alert
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature


// Composable that displays a single alert and its polygons
@Composable
fun WeatherScreenMap(
    alert: Alert,
    setPreview: (Feature) -> Unit,
    resetPreview: () -> Unit,
    selected: Boolean
) {
    val polygon = alert.polygonList
    val polygonColor = alert.alert.properties.riskMatrixColor
    val area = alert.alert.properties.area
    val centers = mutableListOf<LatLng>()

    // Used to find the center coordinates for each polygon
    polygon.forEach { poly ->
        centers.add(calculatePolygonCenter(poly.coordinates))
    }

    // Create a mutable state to track whether the polygon is selected
    var isPolygonSelected by remember { mutableStateOf(false) }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = rememberCameraPositionState {
            // Find initialized camera position from the center of all polygons
            position = CameraPosition.fromLatLngZoom(calculatePolygonCenter(centers), 7f)
        }
    ) {
        // Make a polygon animation for each polygon the alert has
        polygon.forEach { it ->
            Polygon(
                // Corners
                points = it.coordinates,
                clickable = true,
                fillColor = if (isPolygonSelected)
                    getColorFromString(polygonColor).copy(alpha = 0.8f)
                else getColorFromString(polygonColor).copy(alpha = 0.5f),
                strokeColor = Color.Black,
                strokeWidth = 5f,
                tag = area,
                onClick = {
                    println(selected)
                    // Toggle display of a warning box if the polygons are selected
                    if (isPolygonSelected) {
                        resetPreview()
                        isPolygonSelected = false
                    } else {
                        setPreview(alert.alert)
                        isPolygonSelected = true
                    }
                }
            )
        }
        // Close warning box if the camera is moved by the user
        if (currentCameraPositionState.isMoving) {
            isPolygonSelected = false
            resetPreview()
        }
    }
}

// Function that returns a single center coordinate from a polygon
fun calculatePolygonCenter(polygon: List<LatLng>): LatLng {
    var totalLat = 0.0
    var totalLng = 0.0
    // Adds all coordinates together
    for (point in polygon) {
        totalLat += point.latitude
        totalLng += point.longitude
    }
    // Divide size by amount of coordinates to find average
    val centerLat = totalLat / polygon.size
    val centerLng = totalLng / polygon.size
    // Return center
    return LatLng(centerLat, centerLng)
}

// Function to find a color based on the color string from MetAlerts API
fun getColorFromString(colorString: String): Color {
    return when (colorString.lowercase()) {
        "yellow" -> Color.Yellow
        "green" -> Color.Green
        "orange" -> Color(0xFFFFA500)
        "red" -> Color.Red
        else -> Color.Black // Default color or any other color you prefer
    }
}