package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

import com.google.android.gms.maps.model.LatLng
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation

class WarningRepository {
    companion object{
        val warningDataSource: WarningDataSource = WarningDataSource()
    }

    // fetches all warnings
    suspend fun fetchAllWarnings(): Warning?{
        try {
            return warningDataSource.fetchAllWarnings()
        }catch (e: Exception){
            return null
        }

    }

    // Turns the warnings into a list of Alert objects sorted by the distance to them
    suspend fun fetchAlertList(allWarnings: Warning, loc: CustomLocation): List<Alert>{
        val alerts = allWarnings.weatherAlert.features
        val alertList = mutableListOf<Alert>()

        // iterates through all alerts
        alerts.forEach{ alert ->
            val distanceToAlert: Double

            // find all polygons
            val polygonList = findAllPolygons(alert.geometry.coordinates)

            // sees if the the location is inside one of the polygons
            var isInAlertArea = false
            polygonList.forEach {
                if (isPointInsidePolygon(it.coordinates, LatLng(loc.lat, loc.lon))){
                    isInAlertArea = true
                }
            }

            if (isInAlertArea){
                distanceToAlert = 0.0
            }

            else{
                // find the closest coordinate of the alert
                val closestCoord = findClosestCoordinateOfAlert(polygonList, loc)
                // finds the distance to the alert
                distanceToAlert = calculateDistance(closestCoord, LatLng(loc.lat, loc.lon))
            }

            alertList.add(Alert(alert, distanceToAlert, polygonList))
            alertList.sortBy { it.distance }
        }
        return alertList
    }


    // mathematical function for calculating distance between two coodinates on earth
    suspend fun calculateDistance(coord1: LatLng, coord2: LatLng): Double {
        val earthRadius = 6371.0 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(coord2.latitude - coord1.latitude)
        val dLon = Math.toRadians(coord2.longitude - coord1.longitude)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(coord1.latitude)) * Math.cos(Math.toRadians(coord2.latitude)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
    }

    // method looking for the closest point of a polygon
    suspend fun findClosestCoordinateOfAlert(list: List<Polygon>, loc: CustomLocation): LatLng {
        val myCoord = LatLng(loc.lat, loc.lon)
        var closestCoord: LatLng? = null
        var closestDist: Double = Double.MAX_VALUE // Initialize with the maximum value

        // iterates through each polygon
        list.forEach {
            // iterates through the coordinates of each polygon
            it.coordinates.forEach { coord ->
                // calculates the distance to the coordinate
                val distance = calculateDistance(coord, myCoord)
                // if the coordinate is closer than all other thus far
                if (distance < closestDist){
                    closestDist = distance
                    closestCoord = coord
                }
            }

        }
        return closestCoord!!
    }

    // function looking for all polygons of an alert
    suspend fun findAllPolygons(list: List<*>): List<Polygon> {
        val polygonList = mutableListOf<Polygon>()
        // nested list
        list.forEach {
            val coords = mutableListOf<LatLng>()

            // flattens the nested list
            val flattenedList = flatten((it as? List<*>)!!)
            // in the flattened list, every other item will shift between lat and lon
            for (i in flattenedList.indices) {
                if (i % 2 != 0) {
                    val lat = flattenedList[i].toString().toDouble()
                    val lon = flattenedList[i - 1].toString().toDouble()

                    val latLng = LatLng(lat, lon)
                    coords.add(latLng)
                }
            }
            val polygon = Polygon(coords)
            polygonList.add(polygon)
        }
        return polygonList
    }

    fun isPointInsidePolygon(polygon: List<LatLng>, point: LatLng): Boolean {
        // Counter for the number of times a ray starting from the point intersects with polygon edges.
        var intersections = 0

        // Construct a ray from the point to the 'east' direction (increasing longitude)
        val rayEndPoint = LatLng(point.latitude, Double.MAX_VALUE)

        polygon.forEachIndexed { index, vertex ->
            val nextVertex = polygon[(index + 1) % polygon.size] // Ensures a closed loop

            // Check if the ray intersects with the edge between the current vertex and the next vertex
            if (areLinesIntersecting(point, rayEndPoint, vertex, nextVertex)) {
                intersections++
            }
        }

        // If the number of intersections is odd, the point is inside the polygon
        return intersections % 2 == 1
    }

    fun areLinesIntersecting(p1: LatLng, p2: LatLng, q1: LatLng, q2: LatLng): Boolean {
        // Quick bounding box test to rule out lines that are too far apart to intersect
        if (maxOf(p1.longitude, p2.longitude) < minOf(q1.longitude, q2.longitude)) {
            return false
        }

        // Calculate the direction of points to determine if line segments intersect
        val d1 = direction(q1, q2, p1)
        val d2 = direction(q1, q2, p2)
        val d3 = direction(p1, p2, q1)
        val d4 = direction(p1, p2, q2)

        // Line segments intersect if they 'straddle' each other
        return d1 * d2 < 0.0 && d3 * d4 < 0.0
    }

    fun direction(pi: LatLng, pj: LatLng, pk: LatLng): Double {
        // The cross product of vectors (pi, pj) and (pi, pk) determines the relative orientation
        // of the point pk to the line segment formed by pi and pj
        return (pk.latitude - pi.longitude) * (pj.latitude - pi.latitude) - (pj.longitude - pi.longitude) * (pk.latitude - pi.latitude)
    }
}
fun flatten(list: List<*>): List<*> =
    list.flatMap {
        if (it is List<*>) flatten(it) else listOf(it)
    }

data class Alert(val alert: Feature, val distance: Double, val polygonList: List<Polygon>)

data class Polygon(val coordinates: List<LatLng>){
    override fun toString(): String {
        var string = ""
        coordinates.forEach { string += "(${it.latitude} - ${it.longitude})" }
        return string
    }
}

