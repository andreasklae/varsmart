package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository
import okhttp3.internal.wait
import java.lang.Thread.sleep

class MasterRepository {
    val weatherRepository = WeatherRepository()
    val warningRepository = WarningRepository()

    val favorites: List<CustomLocation> = emptyList()

    var currentLocation: CustomLocation? = null

    fun printLocation(){
        //sleep(2000)
        if(currentLocation != null){
            println("Location success: ${currentLocation!!.lat}, ${currentLocation!!.lon}")
        }
        else{
            println("nope")
        }
    }

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
                        currentLocation = CustomLocation("Your position", task.result.longitude, task.result.latitude, "", "")
                        println("Inne i funksjonen")
                        printLocation()
                    if (locationResult.result != null){
                        println("User Coords: ${locationResult.result.latitude}, ${locationResult.result.longitude}")
                    }
                }
            }
        } catch (e: SecurityException) {
            // Show error or something
        }
    }




}