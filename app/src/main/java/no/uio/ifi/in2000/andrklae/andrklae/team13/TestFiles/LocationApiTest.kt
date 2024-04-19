package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationDataSource
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepository

private val locationRepo = LocationRepository()
suspend fun main() {
    println("Testing location Repository")
    println()

    println("Testing Api-call")
    testAPI()
    println()

    reverseGeocode()

}

suspend fun testAPI() {
    val list = locationRepo.getLocations("sagtomtveien")
    if (list.isNotEmpty()) {
        println("Success. Found: ${list.size} locations.")
        list.forEach { println(); println(it) }
    } else {
        println("Failed")
    }
}

suspend fun searchKnownCities() {
    val list = locationRepo.getLocations("Oslo")
    println("Found: ${list.first()}")
}

suspend fun reverseGeocode() {
    val lat = 63.9592162
    val lon = 15.9
    println(locationRepo.coordsToCity(lat, lon)?.name)
}