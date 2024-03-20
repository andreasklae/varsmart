package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherDataSource


// function to test api
suspend fun main() {

    // specifies time and location
    val name = "Oslo"
    val type = "By"
    val fylke = "Oslo"
    val lat = 59.91
    val lon = 10.71

    val customLocation = CustomLocation(name, lat, lon, type, fylke)

    val year = "2024"
    val month = "03"
    val day = "20"
    val hour = "16"

    val dateTime = DateTime(year, month, day, hour)

    val dataSource = WeatherDataSource()

    // fetches forecast for a specific location and time
    val weatherTimeForecast = dataSource.fetchWeather(customLocation, dateTime) // Replace with an actual call to fetch data

    // Print the weather forecast for the specified time
    println(weatherTimeForecast)
}