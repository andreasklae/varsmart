package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location

class WarningRepository {


    private val warningDataSource: WarningDataSource= WarningDataSource()

    suspend fun fetchAllWarnings(): Warning {
        return warningDataSource.fetchAllWarnings()
    }


    data class Coordinate2(val lat: Double, val lon: Double)


    fun findClosestCoordinate(loc: Location, coordinates: List<Feature>): Coordinate2? {
        val myCoordinate = Coordinate2(loc.lat, loc.lon)
        println("Looking for alerts near ${loc.lat}, ${loc.lon}...")
        var closestCoordinate: Coordinate2? = null
        var minDistance = Double.MAX_VALUE
        println("Running test")

        // First get all lists of warnings, including clusters
        // Coords is here I.E a size of 45
        coordinates.forEach{it ->
            if(it.geometry.coordinates is ArrayList<*>){
                // If there is only 1 cluster of coordinates
                // I.E [[59, 5][54, 45]]
                if(it.geometry.coordinates.size == 1){
                    // Iterate through each [lat, long] ArrayList to extract coordinates
                    it.geometry.coordinates.forEach{coords ->
                        // Again check if it's the correct type
                        if(coords is ArrayList<*>){
                            coords.forEach{x ->
                                if(x is ArrayList<*>){
                                    // Calling function on first and second element in the ArrayList (lat lng)
                                    // Yes this is an absolute mess but it works
                                    val distance = calculateDistance(myCoordinate, Coordinate2(x[1].toString().toDouble(), x[0].toString().toDouble()))
                                    if (distance < minDistance) {
                                        minDistance = distance
                                        closestCoordinate = Coordinate2(x[1].toString().toDouble(), x[0].toString().toDouble())
                                    }
                                }


                            }

                        }
                    }
                }
                // If there's more than 1 cluster of coordinates (2, nested list)
                // I.E ["[[59, 54][43, 46]]""[[43, 65][34, 65]]"]
                else{
                    it.geometry.coordinates.forEach{it2 ->
                        // it2 will now be the I.E 2 individual lists that contain another list of
                        // [54, 695] lists
                        if(it2 is ArrayList<*>){
                            it2.forEach{it3 ->
                                // it3 will now be [[54, 54][43, 25]] so we need to go deeper again
                                if(it3 is ArrayList<*>){
                                    it3.forEach{coords ->
                                        // We have finally reached the inner [45, 65] ArrayList and
                                        // can repeat the method to compare distance
                                        if(coords is ArrayList<*>){
                                            val distance = calculateDistance(myCoordinate, Coordinate2(coords[1].toString().toDouble(), coords[0].toString().toDouble()))
                                            if (distance < minDistance) {
                                                minDistance = distance
                                                closestCoordinate = Coordinate2(coords[1].toString().toDouble(), coords[0].toString().toDouble())
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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