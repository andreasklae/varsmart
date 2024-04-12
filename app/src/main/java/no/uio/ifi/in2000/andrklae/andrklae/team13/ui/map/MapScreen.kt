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
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Polygon
import java.io.File

@Composable
fun bilde(){
    Box(modifier = Modifier.fillMaxSize()){
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                //.data("file:///android_asset/symbols/fog.svg")
                .data(File("file:///android_asset/NSA.JPG"))
                .crossfade(true)
                .build()
        )
        Image(
            painter = painter,
            contentDescription = "test"
        )
    }
}


@Composable
fun ComposeMapDemoMarkers(mapViewModel: MapViewModel) {
    val singapore = LatLng(1.3554117053046808, 103.86454252780209)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {

        Marker(
            state = rememberMarkerState(position = singapore),
            title = "Marker1",
            snippet = "Marker in Singapore",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
        )
        //Polygon(points = mapViewModel.polygonPoints)

    }
}
@Composable
fun MapWithPolygon(polygon: Polygon) {
    val polygonPoints = listOf(
        LatLng(37.7749, -122.4194),
        LatLng(37.8049, -122.4400),
        LatLng(37.7949, -122.4100)
    )

    // Create a mutable state to track whether the polygon is selected
    var isPolygonSelected by remember { mutableStateOf(false) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = rememberCameraPositionState {
            // position = CameraPosition.fromLatLngZoom(LatLng(37.7749, -122.4194), 13f)
            position = CameraPosition.fromLatLngZoom(calculatePolygonCenter(polygon.coordinates), 30f)
        }
    ) {
        Polygon(
            points = polygon.coordinates,
            clickable = true,
            fillColor = if (isPolygonSelected) Color.Red else Color.Gray,
            strokeColor = Color.Blue,
            strokeWidth = 5f,
            tag = "San Francisco",
            onClick = { polygon ->
                // Handle polygon click event
                isPolygonSelected = true
            }
        )
    }
    // Add a button to reset the selection
    Box(contentAlignment = Alignment.BottomCenter) {
        Button(
            onClick = {
                isPolygonSelected = false
            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text("Reset Selection")
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