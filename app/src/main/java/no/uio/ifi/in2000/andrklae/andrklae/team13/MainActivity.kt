package no.uio.ifi.in2000.andrklae.andrklae.team13

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CurrentLocation.LocationUtil
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.HomeScreen
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.HomeViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.Team13Theme

import com.android.volley.Request
import com.android.volley.RetryPolicy
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    //private val mapViewModel = MapViewModel()

    val alesund = CustomLocation("Ålesund", 62.47, 6.13, "By", "")
    val alesundData = DataHolder(alesund)
    val homeVM = HomeViewModel(0)
    val test = 0

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            // Check if the permissions were granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, try to fetch the location
                LocationUtil.fetchLocation(this, this) { customLocation ->
                    if (customLocation != null) {
                        DataHolder(customLocation)
                        val index = DataHolder.Favourites.indexOf(
                            DataHolder.Favourites.find { it.location == customLocation }
                        )
                        homeVM.setLocation(index)
                        homeVM.updateAll()
                    } else {

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
                    HomeScreen(homeVM)
                }
            }
        }
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun getCurrentLocation(){
        if (LocationUtil.hasLocationPermission(this)) {
            // If permission is already granted, attempt to fetch the location immediately
            LocationUtil.fetchLocation(this,this) { customLocation ->
                if (customLocation != null) {

                    // fix later
                    // checks if the user has moved
                    val notMoved = DataHolder.Favourites.any {
                        it.location.lat == customLocation.lat
                                &&
                                it.location.lon == customLocation.lon
                    }
                    if (notMoved){
                        val index = DataHolder.Favourites.indexOf(
                            DataHolder.Favourites.find { it.location == customLocation }
                        )
                        homeVM.setLocation(index)
                    }
                    else{
                        DataHolder.Favourites.remove(DataHolder.Favourites.find { it.location.name == "My location" })
                        val newLocation = DataHolder(customLocation)
                        homeVM.setLocation(DataHolder.Favourites.lastIndex)
                    }
                }
            }
        }

        else{
            LocationUtil.requestPermission(this)
        }
    }
}