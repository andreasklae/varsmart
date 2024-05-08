package no.uio.ifi.in2000.andrklae.andrklae.team13

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationUtil
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.PreferenceManager
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Favorite.FavoriteViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.MasterUi
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings.SettingsViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.onboarding.Onboarding
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.onboarding.OnboardingViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning.WarningViewModel

class MainActivity : ComponentActivity() {

    val weatherVM = WeatherViewModel(DataHolder.initLocation, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // creates each viewmodel
        val favVM = FavoriteViewModel()
        val warningVM = WarningViewModel()
        val onboardingVM = OnboardingViewModel(this)
        val settingsVM = SettingsViewModel(
            initAge = PreferenceManager.fetchAge(this),
            initHobbies = PreferenceManager.fetchHobbies(this),
            initBackground = PreferenceManager.fetchBackgroundIndex(this)
        )

        // loads the favourites from long term storage
        favVM.loadFavourites(this)

        // if favourite list is not empty
        if (DataHolder.Favourites.isNotEmpty()){
            // sets location to either the current position of the device
            // or the first favourite
            weatherVM.setLocation(
                DataHolder.Favourites
                    .sortedBy { it.location.name.contains("Min posisjon") }
                    .first())
        } else weatherVM.updateAll()

        setContent {
            // sees if the onboarding is completed
            val onboardingCompleted = onboardingVM.onboardingCompleted.collectAsState()

            // if completed, show the regular ui
            if (onboardingCompleted.value){
                MasterUi(
                    activity = this,
                    settingsVM = settingsVM,
                    favVM = favVM,
                    weatherVM = weatherVM,
                    warningVM = warningVM
                )
            }

            // if not, show the onboarding
            else{
                Onboarding(
                    this,
                    onboardingVM,
                    settingsVM
                )
            }
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

                        // i data doesn't already exist
                        if (!DataHolder.Favourites.contains(new)) {
                            // add to favourites
                            new.toggleInFavourites()
                            // navigate to home-screen with current location
                            weatherVM.setLocation(new)
                        } else {
                            weatherVM.setLocation(
                                // will never be null because of the if statement above
                                DataHolder.Favourites.find { it == new }!!
                            )
                        }

                        // updates favourite list in long term memory
                        PreferenceManager.saveFavourites(this, DataHolder.Favourites)
                    } else {

                    }
                }
            } else {
                // Permission was denied.
                Toast.makeText(
                    this,
                    "Loksjonstilgang ikke gitt",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // function for fetching the current location of the device
    fun getCurrentLocation() {
        println("Henter nåværende lokasjon")
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if location services is enabled on the device
        val locationEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        // if not enabled
        if (!locationEnabled) {
            // Prompt the user to enable location from settings
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Handling kreves, posisjon kunne ikke hentes")
                .setMessage("Venligst slå på posisjon for å hente været for din posisjon.")

                // button for navigating to settings and turning on location services
                .setPositiveButton("Instillinger") { dialog, which ->
                    // Open the device's location settings
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                //  button for dismissing
                .setNegativeButton("Cancel") { dialog, which -> }
                .show()
        }

        // if location services is enabled on the device
        else {
            // If location permission is granted
            if (LocationUtil.hasLocationPermission(this)) {
                // fetch the location
                LocationUtil.fetchLocation(
                    this
                ) { customLocation ->

                    // if location was fetched successfully
                    if (customLocation != null) {
                        val new = DataHolder(customLocation)

                        // i data doesnt exist
                        if (!DataHolder.Favourites.contains(new)) {
                            weatherVM.setLocation(new)
                            new.toggleInFavourites()
                        } else {
                            weatherVM.setLocation(
                                // will never be null because of the if statement above
                                DataHolder.Favourites.find { it == new }!!
                            )
                        }
                        PreferenceManager.saveFavourites(this, DataHolder.Favourites)
                    } else{
                        Toast.makeText(
                            this,
                            "Fant ikke lokasjon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            // request permission if not already granted
            else {
                LocationUtil.requestPermission(this)
            }
        }

    }
}