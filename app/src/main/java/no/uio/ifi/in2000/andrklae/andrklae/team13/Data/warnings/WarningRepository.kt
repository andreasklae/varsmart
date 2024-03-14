package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

class WarningRepository {


    private val warningDataSource: WarningDataSource= WarningDataSource()

    suspend fun fetchAllWarnings(): Warning {
        return warningDataSource.fetchAllWarnings()
    }


    data class Coordinate2(val lat: Double, val lon: Double)


    fun findClosestCoordinate(myCoordinate: Coordinate2, coordinates: List<Coordinate2>): Coordinate2? {
        var closestCoordinate: Coordinate2? = null
        var minDistance = Double.MAX_VALUE

        for (coordinate in coordinates) {
            val distance = calculateDistance(myCoordinate, coordinate)
            if (distance < minDistance) {
                minDistance = distance
                closestCoordinate = coordinate
            }
        }

        return closestCoordinate
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



}