package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationDataSource
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationRepository

private val locationRepo = LocationRepository()
suspend fun main(){
    println("Testing location Repository")
    println()

    println("Testing searchKnownCities")
    searchKnownCities()
    println()

    println("Testing Api-call")
    testAPI()
    println()


}

suspend fun testAPI(){


    val list = locationRepo.getLocations("St. hanshaugen")
    if(list.isNotEmpty()){
        println("Success. Found: ${list.size} locations. First is: \n" +
                "${list.first()}")
    }
    else{
        println("Failed")
    }
}
suspend fun searchKnownCities(){
    val list = locationRepo.getLocations("Oslo")
    println("Found: ${list.first()}")
}