package no.uio.ifi.in2000.andrklae.andrklae.team13

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationUtil
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.PreferenceManager
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.FavoriteViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.MasterUi
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings.SettingsViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning.WarningViewModel

class MainActivity : ComponentActivity() {

    val weatherVM = WeatherViewModel(DataHolder.initLocation, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val favVM = FavoriteViewModel()
        val warningVM = WarningViewModel()
        val settingsVM = SettingsViewModel(
            initAge = PreferenceManager.fetchAge(this),
            initHobbies = PreferenceManager.fetchHobbies(this),
            initBackground = PreferenceManager.fetchBackgroundIndex(this)
        )
        getCurrentLocation()

        favVM.loadFavourites(this)

        // if "Min posisjon" exists in the favourite list
        if (DataHolder.Favourites.any { it.location.name == "Min posisjon" }){
            weatherVM.setLocation(
                DataHolder.Favourites.find { it.location.name == "Min posisjon" }!!
            )
        }

        // if favourite list is not empty
        else if (DataHolder.Favourites.isNotEmpty()){
            weatherVM.setLocation(DataHolder.Favourites.first())
        }
        else weatherVM.updateAll()

        setContent {
            MasterUi(
                activity = this,
                settingsVM = settingsVM,
                favVM = favVM,
                weatherVM = weatherVM,
                warningVM = warningVM
            )
        }
    }

    // function for handling requests (only location services is needed)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // if the request code is for location access
        if (requestCode == LocationUtil.REQUEST_CODE) {
            // Check if the permissions were granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, try to fetch the location
                LocationUtil.fetchLocation(this)
                { customLocation ->
                    // if fetching was sucessfull
                    if (customLocation != null) {
                        val new = DataHolder(customLocation)

                        // i data doesnt exist
                        if (!DataHolder.Favourites.contains(new)) {
                            new.toggleInFavourites()
                            weatherVM.setLocation(new)
                        } else {
                            weatherVM.setLocation(
                                // will never be null because of the if statement above
                                DataHolder.Favourites.find { it == new }!!
                            )
                        }
                    } else {

                    }
                }
            } else {
                // Permission was denied. You can show a message
                // to the user or take appropriate action.
                println("Permission denied by the user")
            }
        }
    }

    // function for fetching the current location of the device
    fun getCurrentLocation() {
        println("Henter nåværende lokasjon")
        if (LocationUtil.hasLocationPermission(this)) {
            // If permission is already granted, attempt to fetch the location immediately
            LocationUtil.fetchLocation(
                this
            ) { customLocation ->
                if (customLocation != null) {
                    val new = DataHolder(customLocation)

                    // i data doesnt exist
                    if (!DataHolder.Favourites.contains(new)) {
                        new.toggleInFavourites()
                        weatherVM.setLocation(new)
                    } else {
                        weatherVM.setLocation(
                            // will never be null because of the if statement above
                            DataHolder.Favourites.find { it == new }!!
                        )
                    }
                    PreferenceManager.saveFavourites(this, DataHolder.Favourites)
                }
            }
        }

        // request permission if not granted
        else {
            LocationUtil.requestPermission(this)
        }
    }

    // function for checking if the user as internet
    fun isOnline(context: Context = this): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
        Log.i("Internet", "No internet connection detected")
        return false
    }
}