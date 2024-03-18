package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.model.polygonOptions
import kotlinx.coroutines.flow.MutableStateFlow

class MapViewModel( /*Give repos or data*/ ): ViewModel() {
    val state: MutableState<MapState> = mutableStateOf(
        MapState(
            lastKnownLocation = null,
            // Warning areas for later
            /*clusterItems = listOf(
                ZoneClusterItem(
                    id = "zone-1",
                    title = "Zone 1",
                    snippet = "This is Zone 1.",
                    polygonOptions = polygonOptions {
                        add(LatLng(49.105, -122.524))
                        add(LatLng(49.101, -122.529))
                        add(LatLng(49.092, -122.501))
                        add(LatLng(49.1, -122.506))
                        fillColor(some color goes here)
                    }
                )
            )

             */
        )
    )

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state.value = state.value.copy(
                        lastKnownLocation = task.result
                    )
                    if (locationResult.result.latitude != null && locationResult.result.longitude != null){
                        println("User Coords: ${locationResult.result.latitude}, ${locationResult.result.longitude}")
                    }
                }
            }
        } catch (e: SecurityException) {
            // Show error or something
        }
    }
}