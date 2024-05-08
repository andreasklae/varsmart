package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast

private val LRepo: LocationRepositoryInterface = LocationRepository()
private val Wrepo: WeatherRepositoryInterface = WeatherRepository()
suspend fun main(){
    println("Fetching location: Sagene")
    println("Searching...")
    val list = LRepo.getLocations("Sagene")
    if(list.isNotEmpty()){
        val location = list.first()
        println("Success. Found: ${location.name} in ${location.postSted}"
        )
        println()
        val year = "2024"
        val month = "04"
        val day = "4"
        val hour = "16"

        val dateTime = DateTime(year, month, day, hour)
        val weatherForecast = Wrepo.getWeather(location)
        val weather = weatherForecast?.let { WeatherTimeForecast(it, dateTime, location) }
        println("Success, Printing info...")
        println()
        println(weather)
    }
    else{
        println("Failed to find location")
    }


}