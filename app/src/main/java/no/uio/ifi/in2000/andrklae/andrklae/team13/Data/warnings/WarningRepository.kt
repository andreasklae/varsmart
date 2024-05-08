package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

import com.google.android.gms.maps.model.LatLng
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import kotlin.math.max
import kotlin.math.min

class WarningRepository() : WarningRepositoryInterface {
    companion object {
        private val warningDataSource: WarningDataSource = WarningDataSource()
    }

    // fetches all warnings
    override suspend fun fetchAllWarnings(): Warning? {
        try {
            return warningDataSource.fetchAllWarnings()
        } catch (e: Exception) {
            return null
        }

    }

    // Turns the warnings into a list of Alert objects sorted by the
    // distance to them from a given location
    override suspend fun fetchAlertList(allWarnings: Warning, loc: CustomLocation): List<Alert> {
        val alerts = allWarnings.weatherAlert.features
        val alertList = mutableListOf<Alert>()

        // iterates through all alerts
        alerts.forEach { alert ->
            val distanceToAlert: Double

            // find all polygons
            val polygonList = findAllPolygons(alert.geometry.coordinates)

            // sees if the the location is inside one of the polygons
            var isInAlertArea = false
            polygonList.forEach {
                if (isPointInsidePolygon(it.coordinates, LatLng(loc.lat, loc.lon))) {
                    isInAlertArea = true
                }
            }

            if (isInAlertArea) {
                distanceToAlert = 0.0
            } else {
                // find the closest coordinate of the alert
                val closestCoord = findClosestCoordinateOfAlert(polygonList, loc)
                // finds the distance to the closest coordinate
                distanceToAlert = calculateDistance(closestCoord, LatLng(loc.lat, loc.lon))
            }

            alertList.add(Alert(alert, distanceToAlert, polygonList))
        }
        alertList.sortBy { it.distance }
        return alertList
    }


    // mathematical function for calculating distance between two coordinates on earth
    override fun calculateDistance(coord1: LatLng, coord2: LatLng): Double {
        val earthRadius = 6371.0 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(coord2.latitude - coord1.latitude)
        val dLon = Math.toRadians(coord2.longitude - coord1.longitude)
        val a = Math.sin(
            dLat / 2
        ) * Math.sin(dLat / 2) +
                Math.cos(
                    Math.toRadians(coord1.latitude)
                ) * Math.cos(Math.toRadians(coord2.latitude)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
    }

    // method looking for the closest point of a polygon
    override suspend fun findClosestCoordinateOfAlert(
        list: List<Polygon>,
        loc: CustomLocation
    ): LatLng {
        val myCoord = LatLng(loc.lat, loc.lon)
        var closestCoord: LatLng? = null
        var closestDist: Double = Double.MAX_VALUE // Initialize with the maximum value

        // iterates through each polygon
        list.forEach {
            // iterates through the coordinates of each polygon
            it.coordinates.forEach { coord ->
                // calculates the distance to the coordinate
                val distance = calculateDistance(coord, myCoord)
                // if the coordinate is the closest one thus far
                if (distance < closestDist) {
                    closestDist = distance
                    closestCoord = coord
                }
            }

        }
        return closestCoord!!
    }

    // function looking for all polygons of an alert
    override fun findAllPolygons(list: List<*>): List<Polygon> {
        val polygonList = mutableListOf<Polygon>()
        // Api gives unnecessarily many layers of a nested list
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

    // functions to see if a coordinate is inside a polygon.
    // the functions is not our own, got it from chat gpt.
    // It passed the unittests, so it should work.
    override fun isPointInsidePolygon(polygon: List<LatLng>, point: LatLng): Boolean {
        var intersections = 0
        val rayEndPoint = LatLng(point.latitude, polygon.maxOf { it.longitude } + 1.0)

        polygon.forEachIndexed { index, vertex ->
            val nextVertex = polygon[(index + 1) % polygon.size]

            if (areLinesIntersecting(point, rayEndPoint, vertex, nextVertex)) {
                intersections++
            }
        }

        return intersections % 2 == 1
    }

    fun direction(p: LatLng, q: LatLng, r: LatLng): Double {
        return (q.longitude - p.longitude) *
                (r.latitude - q.latitude) -
                (q.latitude - p.latitude) *
                (r.longitude - q.longitude)
    }

    override fun areLinesIntersecting(p1: LatLng, p2: LatLng, q1: LatLng, q2: LatLng): Boolean {
        // Determine the four orientations needed for the general and special cases
        val d1 = direction(q1, q2, p1)
        val d2 = direction(q1, q2, p2)
        val d3 = direction(p1, p2, q1)
        val d4 = direction(p1, p2, q2)

        // General case
        if (d1 * d2 < 0 && d3 * d4 < 0) return true

        // Special Cases (collinear or touching segments)
        // Collinear points are considered intersecting
        return (d1 == 0.0 && onSegment(q1, p1, q2)) || (d2 == 0.0 && onSegment(q1, p2, q2)) ||
                (d3 == 0.0 && onSegment(p1, q1, p2)) || (d4 == 0.0 && onSegment(p1, q2, p2))
    }

    fun onSegment(p: LatLng, q: LatLng, r: LatLng): Boolean {
        return q.longitude <= max(p.longitude, r.longitude) && q.longitude >= min(p.longitude, r.longitude) &&
                q.latitude <= max(p.latitude, r.latitude) && q.latitude >= min(p.latitude, r.latitude)
    }
}

data class Alert(val alert: Feature, val distance: Double, val polygonList: List<Polygon>)

data class Polygon(val coordinates: List<LatLng>) {
    override fun toString(): String {
        var string = ""
        coordinates.forEach { string += "(${it.latitude} - ${it.longitude})" }
        return string
    }
}