package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

object LocationUtil {
    val REQUEST_CODE = 1001
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

    fun requestPermission(
        activity: Activity
    ) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_CODE
        )
    }

    // function for fetching the user location
    fun fetchLocation(
        activity: Activity,
        callback: (CustomLocation?) -> Unit
    ) {
        if (hasLocationPermission(activity)) {
            try {
                val fusedLocationProviderClient: FusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(activity)
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            callback(
                                CustomLocation(
                                    "Min posisjon",
                                    it.latitude,
                                    it.longitude,
                                    "",
                                    ""
                                )
                            )
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