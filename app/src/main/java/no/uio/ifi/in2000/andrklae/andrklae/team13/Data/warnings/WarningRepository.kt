package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation

class WarningRepository {
    companion object{
        val warningDataSource: WarningDataSource = WarningDataSource()
    }

    // fetches all warnings
    suspend fun fetchAllWarnings(): Warning{
        return warningDataSource.fetchAllWarnings()
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
                if (isPointInsidePolygon(it.coordinates, Coordinate(loc.lat, loc.lon))){
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
                distanceToAlert = calculateDistance(closestCoord, Coordinate(loc.lat, loc.lon))
            }

            alertList.add(Alert(alert, distanceToAlert, polygonList))
            alertList.sortBy { it.distance }
        }
        return alertList
    }


    // mathematical function for calculating distance between two coodinates on earth
    suspend fun calculateDistance(coord1: Coordinate, coord2: Coordinate): Double {
        val earthRadius = 6371.0 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(coord2.lat - coord1.lat)
        val dLon = Math.toRadians(coord2.lon - coord1.lon)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(coord1.lat)) * Math.cos(Math.toRadians(coord2.lat)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
    }

    // method looking for the closest point of a polygon
    suspend fun findClosestCoordinateOfAlert(list: List<Polygon>, loc: CustomLocation): Coordinate {
        val myCoord = Coordinate(loc.lat, loc.lon)
        var closestCoord: Coordinate? = null
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
            val coords = mutableListOf<Coordinate>()

            // flattens the nested list
            val flattenedList = flatten((it as? List<*>)!!)
            // in the flattened list, every other item will shift between lat and lon
            for (i in flattenedList.indices) {
                if (i % 2 != 0) {
                    val lat = flattenedList[i].toString().toDouble()
                    val lon = flattenedList[i - 1].toString().toDouble()

                    val coordinate = Coordinate(lat, lon)
                    coords.add(coordinate)
                }
            }
            val polygon = Polygon(coords)
            polygonList.add(polygon)
        }
        return polygonList
    }

    fun isPointInsidePolygon(polygon: List<Coordinate>, point: Coordinate): Boolean {
        // Counter for the number of times a ray starting from the point intersects with polygon edges.
        var intersections = 0

        // Construct a ray from the point to the 'east' direction (increasing longitude)
        val rayEndPoint = Coordinate(point.lat, Double.MAX_VALUE)

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

    fun areLinesIntersecting(p1: Coordinate, p2: Coordinate, q1: Coordinate, q2: Coordinate): Boolean {
        // Quick bounding box test to rule out lines that are too far apart to intersect
        if (maxOf(p1.lon, p2.lon) < minOf(q1.lon, q2.lon)) {
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

    fun direction(pi: Coordinate, pj: Coordinate, pk: Coordinate): Double {
        // The cross product of vectors (pi, pj) and (pi, pk) determines the relative orientation
        // of the point pk to the line segment formed by pi and pj
        return (pk.lon - pi.lon) * (pj.lat - pi.lat) - (pj.lon - pi.lon) * (pk.lat - pi.lat)
    }
}
fun flatten(list: List<*>): List<*> =
    list.flatMap {
        if (it is List<*>) flatten(it) else listOf(it)
    }

data class Coordinate(val lat: Double, val lon: Double)
data class Alert(val alert: Feature, val distance: Double, val polygonList: List<Polygon>)

data class Polygon(val coordinates: List<Coordinate>){
    override fun toString(): String {
        var string = ""
        coordinates.forEach { string += "(${it.lat} - ${it.lon})" }
        return string
    }
}

