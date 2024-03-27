package no.uio.ifi.in2000.andrklae.andrklae.team13

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CurrentLocation.LocationUtil
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.MVP
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.HomeScreen
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.HomeViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.MapViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.Team13Theme
class MainActivity : ComponentActivity() {
    private val mapViewModel = MapViewModel()
    val homeVM = HomeViewModel()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            // Check if the permissions were granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, try to fetch the location
                LocationUtil.fetchLocation(this, this) { customLocation ->
                    if (customLocation != null) {
                        homeVM.setLocation(customLocation)
                        homeVM.update()
                        println("Location fetched after permission granted")
                    } else {
                        // Handle the case where location is still null
                        println("Failed to fetch location even after permission was granted")
                    }
                }
            } else {
                // Permission was denied. You can show a message to the user or take appropriate action.
                println("Permission denied by the user")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            setContent {

            Team13Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //MVP(homeVM, this)
                    HomeScreen()
                }
            }
        }
    }

    fun getCurrentLocation(){
        if (LocationUtil.hasLocationPermission(this)) {
            // If permission is already granted, attempt to fetch the location immediately
            LocationUtil.fetchLocation(this,this) { customLocation ->
                if (customLocation != null) {
                    homeVM.setLocation(customLocation)
                    homeVM.update()
                    println("hentet lokasjon")
                }
            }
        }

        else{
            LocationUtil.requestPermission(this)
        }
    }
}