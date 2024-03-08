package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationDataSource
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherDataSource
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherRepository

private val LRepo = LocationRepository()
private val Wrepo = WeatherRepository()
suspend fun main(){
    println("Fetching location: Sagene")
    println("Searching...")
    val list = LRepo.getLocations("Sagene")
    if(list.isNotEmpty()){
        val location = list.first()
        println("Success. Found: ${location.name} in ${location.fylke}"
        )
        println()
        val year = "2024"
        val month = "03"
        val day = "10"
        val hour = "16"

        val dateTime = DateTime(year, month, day, hour)
        val weather = Wrepo.getWeather(dateTime, location)
        println("Success, Printing info...")
        println()
        println(weather)
    }
    else{
        println("Failed to find location")
    }


}