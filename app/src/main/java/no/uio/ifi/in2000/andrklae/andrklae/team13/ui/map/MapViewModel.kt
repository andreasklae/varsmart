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
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature

class MapViewModel(location: CustomLocation, warning: Feature): ViewModel() {
    val location = location
    val warning = warning

    val polygonPoints = listOf(
        LatLng(40.7128, -74.0060), // New York
        LatLng(34.0522, -118.2437), // Los Angeles
        LatLng(41.8781, -87.6298), // Chicago
        LatLng(37.7749, -122.4194) // San Francisco
    )
    val state: MutableState<MapState> = mutableStateOf(MapState(location, warning))
    /*fun getPolygons(): List<ZoneClusterItem> {
        val list: List<ZoneClusterItem> = emptyList()
        warning.geometry.coordinates.forEach { it ->
            if (warning.geometry.coordinates.size > 1) {

            } else {

            }


            /*

           list.add(ZoneClusterItem(warning.properties.id, location.name, warning.properties.description, ))

           */

            return list
        }

    }*/
    fun getLocation(): LatLng {
        return LatLng(location.lat, location.lon)
    }
}

