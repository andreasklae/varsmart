package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseDataSource

suspend fun main() {

    // specifies time and location
    val name = "Oslo"
    val postSted = "Oslo"
    val fylke = "Oslo"
    val lat = 59.91
    val lon = 10.71

    val customLocation = CustomLocation(name, lon, lat, postSted, fylke)

    val year = "2024"
    val month = "03"
    val day = "13"
    val hour = "16"

    val dateTime = DateTime(year, month, day, hour)

    val dataSource = SunriseDataSource()

    // fetches forecast for a specific location and time
    val sunsetAndSunrise = dataSource.fetchSunriseandSunset(customLocation, dateTime) // Replace with an actual call to fetch data

    // Print the weather forecast for the specified time
    println(sunsetAndSunrise)
}