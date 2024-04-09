package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import com.mapbox.geojson.*
import java.lang.Math.pow
import kotlin.math.sqrt

class WarningRepository {

    var warningRange = 40
    private val warningDataSource: WarningDataSource= WarningDataSource()
    // TO-DO
    // Initialize variable with List<Features>
    suspend fun fetchAllWarnings(): Warning {
        return warningDataSource.fetchAllWarnings()
    }


    data class Coordinate2(val lat: Double, val lon: Double)


    // Refactored to use a single function to handle different types of coordinate structures
    private fun processCoordinates(coordinateList: Any?, myCoordinate: Coordinate2, minDistance: Double, currentMinimum: Feature, closestCoordinate: Coordinate2): Pair<Double, Feature?> {
        when (coordinateList) {
            is ArrayList<*> -> {
                coordinateList.forEachIndexed { index, item ->
                    val newCoordinate = parseItemToCoordinate(item) ?: continue
                    val distance = calculateDistance(newCoordinate, myCoordinate)
                    if (index > 0 && distance >= minDistance || index == 0) {
                        return@processCoordinates updateIfNecessary(minDistance, distance, currentMinimum, closestCoordinate, newCoordinate)
                    }
                }
            }
            is String -> {
                val newCoordinate = parseStringToCoordinate(coordinateList) ?: continue
                val distance = calculateDistance(newCoordinate, myCoordinate)
                if (distance < minDistance) {
                    return@processCoordinates updateIfNecessary(minDistance, distance, currentMinimum, closestCoordinate, newCoordinate)
                }
            }
            else -> Unit
        }
        return Pair(minDistance, currentMinimum)
    }

    // Helper functions to parse items into Coordinate2 objects or skip them
    private fun parseItemToCoordinate(item: Any?) : Coordinate2? {
        return try {
            Coordinate2((item as ArrayList<Any>)[1].toString().toDouble(), (item as ArrayList<Any>)[0].toString().toDouble())
        } catch (_: Exception) {
            null
        }
    }

    private fun parseStringToCoordinate(string: String) : Coordinate2? {
        return string.split(",").let { parts ->
            try {
                Coordinate2(parts[1].trim().toDouble(), parts[0].trim().toDouble())
            } catch (_: Exception) {
                null
            }
        }
    }

    // Update the minimum values if necessary
    private fun updateIfNecessary(oldMinDist: Double, dist: Double, oldMinVal: Feature, prevCoord: Coordinate2, coord: Coordinate2): Pair<Double, Feature?> {
        if (dist < oldMinDist) {
            return Pair(dist, Feature("", Geometry.Point(GeoJsonObjectType.POINT, Point(listOf(Value(prevCoord)))))).also {
                prevCoord.lat = coord.lat
                prevCoord.lon = coord.lon
            }
        }
        return Pair(oldMinDist, oldMinVal)
    }

    // Main function refactored using the helper functions
    fun findClosestCoordinate(loc: CustomLocation, coordinates: List<Feature>) : Feature? {
        val myCoordinate = Coordinate2(loc.lat, loc.lon)
        var closestCoordinate: Coordinate2? = null
        var minDistance = Double.MAX_VALUE
        var currentMinimum: Feature = features[0]

        coordinates.forEach { feature ->
            val result = processCoordinates(feature.geometry.coordinates, myCoordinate, minDistance, currentMinimum, closestCoordinate)

            minDistance = result.first
            currentMinimum = result.second ?: currentMinimum
            closestCoordinate?.apply { lat = currentMinimum.geometry.point.value.get()[0].valueAsNumber.doubleValue; lon = currentMinimum.geometry.point.value.get()[1].valueAsNumber.doubleValue }
        }

        return if (calculateKm(myCoordinate.lat, myCoordinate.lon, closestCoordinate!!.lat, closestCoordinate!!.lon) <= warningRange) {
            currentMinimum
        } else {
            null
        }
    }

    // Extension property to access latitude from Value object
    val Value.lat: Double
        get() = valueAsNumber.doubleValue

    val Value.lon: Double
        get() = next().valueAsNumber.doubleValue

    data class Coordinate2(var lat: Double, var lon: Double)

    object UtilityFunctions {
        const val earthRadiusInMeters = 6378137f

        private fun calculateDistance(c1: Coordinate2, c2: Coordinate2): Float {
            val deltaLatitude = Math.toRadians(c2.lat - c1.lat)
            val deltaLongitude = Math.toRadians(c2.lon - c1.lon)
            val cosDeltaLatitude = Math.cos(deltaLatitude / 2)
            val sinDeltaLatitude = Math.sin(deltaLatitude / 2)
            val cosDeltaLongitude = Math.cos(deltaLongitude / 2)
            val sinDeltaLongitude = Math.sin(deltaLongitude / 2)
            val sumSquaredHalfChordLength = pow(cosDeltaLatitude * cosDeltaLongitude, 2.0) + pow(sinDeltaLatitude * sinDeltaLatitude, 2.0) + pow(cosDeltaLongitude * sinDeltaLatitude * sinDeltaLatitude, 2.0)
            val squaredHalfChordLength = 1 - sumSquaredHalfChordLength
            val s = sqrt(squaredHalfChordLength)
            return ((earthRadiusInMeters * (1 - s)).toFloat()).roundToInt()
        }

        private fun calculateKm(startLat: Double, startLon: Double, endLat: Double, endLon: Double): Int {
            val dLat = Math.toRadians(endLat - startLat)
            val dLon = Math.toRadians(endLon - startLon)
            val a = sin(dLat / 2) * sin(dLat / 2) + sin(dLon / 2) * sin(dLon / 2) * cos(Math.toRadians(startLat)) * cos(Math.toRadians(endLat))
            val c = 2 * atan2(sqrt(a), sqrt(1 - a));
            return (UtilityFunctions.earthRadiusInMeters * c).roundToInt()
        }
    }



    fun calculateDistance(coord1: Coordinate2, coord2: Coordinate2): Double {
        val earthRadius = 6371.0 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(coord2.lat - coord1.lat)
        val dLon = Math.toRadians(coord2.lon - coord1.lon)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(coord1.lat)) * Math.cos(Math.toRadians(coord2.lat)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
    }

    fun calculateKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371 // Radius of the earth in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c // Distance in kilometers
    }

}
