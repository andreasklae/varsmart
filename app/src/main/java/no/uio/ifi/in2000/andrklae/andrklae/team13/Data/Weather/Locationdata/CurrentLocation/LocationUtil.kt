package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CurrentLocation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation

object LocationUtil {
    fun hasLocationPermission(context: Context): Boolean {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return hasFineLocationPermission || hasCoarseLocationPermission
    }

    fun requestPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
            1001
        )
    }
    fun fetchLocation(activity: Activity, context: Context, callback: (CustomLocation?) -> Unit) {
        if (hasLocationPermission(context)) {
            try {
                val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        callback(CustomLocation("Min posisjon", it.latitude, it.longitude, "",""))
                    } ?: run {
                        // Location was null, handle accordingly
                        callback(null)
                    }
                }.addOnFailureListener {
                    // Failed to get location, handle accordingly
                    callback(null)
                }
            } catch (e: SecurityException) {
                // Handle the case where the permission was revoked between checking and usage
                callback(null)
            }
        } else {
            // Permissions not granted, request initiated, handle as needed
            callback(null)
        }
    }
}