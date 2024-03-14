package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles



    data class Coordinate(val lat: Double, val lon: Double)


    fun findClosestCoordinate(myCoordinate: Coordinate, coordinates: List<Coordinate>): Coordinate? {
        var closestCoordinate: Coordinate? = null
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

    fun calculateDistance(coord1: Coordinate, coord2: Coordinate): Double {
        val earthRadius = 6371.0 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(coord2.lat - coord1.lat)
        val dLon = Math.toRadians(coord2.lon - coord1.lon)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(coord1.lat)) * Math.cos(Math.toRadians(coord2.lat)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
    }







fun main() {
    val myCoordinate = Coordinate(59.915857, 10.75265) // New York City coordinates
    val coordinates = listOf(
        Coordinate(59.9440422284473, 10.72092291352452), // Forskningsparken
        Coordinate(41.8781, -87.6298),  // Chicago coordinates
        Coordinate(51.5074, -0.1278)    // London coordinates
    )

    val closestCoordinate = findClosestCoordinate(myCoordinate, coordinates)
    println("Closest coordinate: $closestCoordinate")
}
