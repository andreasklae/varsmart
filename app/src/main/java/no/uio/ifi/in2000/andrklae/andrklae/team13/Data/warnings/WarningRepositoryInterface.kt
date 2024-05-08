package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

import com.google.android.gms.maps.model.LatLng
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation

interface WarningRepositoryInterface {

    // fetches all warnings
    suspend fun fetchAllWarnings(): Warning?

    // Turns the warnings into a list of Alert objects sorted by the distance to them
    suspend fun fetchAlertList(allWarnings: Warning, loc: CustomLocation): List<Alert>


    // mathematical function for calculating distance between two coodinates on earth
    fun calculateDistance(coord1: LatLng, coord2: LatLng): Double

    // method looking for the closest point of a polygon
    suspend fun findClosestCoordinateOfAlert(list: List<Polygon>, loc: CustomLocation): LatLng

    // function looking for all polygons of an alert
    fun findAllPolygons(list: List<*>): List<Polygon>

    fun isPointInsidePolygon(polygon: List<LatLng>, point: LatLng): Boolean

    fun areLinesIntersecting(p1: LatLng, p2: LatLng, q1: LatLng, q2: LatLng): Boolean
}

fun flatten(list: List<*>): List<*> =
    list.flatMap {
        if (it is List<*>) flatten(it) else listOf(it)
    }



