package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

interface LocationRepository {
    suspend fun getLocations(search: String): List<CustomLocation>

    suspend fun coordsToCity(lat: Double, lon: Double): CustomLocation?

}
