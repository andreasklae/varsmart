package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherDataSource
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherRepository

val repo = WeatherRepository()
// specifies time and location
val name = "Oslo"
val type = "By"
val fylke = "Oslo"
val lat = 59.91
val lon = 10.71

val location = CustomLocation(name, lon, lat, type, fylke)

val year = "2024"
val month = "03"
val day = "16"
val hour = "16"

val dateTime = DateTime(year, month, day, hour)
suspend fun main() {
    println("Fetching Weather for the next 24 hours...")
    test24H()
    println()

    println("Fetching weather for the week...")
    testWeekWeather()
    println()

    println("Finding sunrise and sunset time for today...")
    testSunriseSunset()
}

suspend fun test24H(){



    val weather = repo.getWeather24h(dateTime, location)
    weather.forEach {

        println(
            "day: ${it.time.dayOfWeek} Hour: ${it.time.hour} Temp: ${it.temperature}"
        )
    }
}
suspend fun testSunriseSunset(){

    val sunrisesunset = repo.getRiseAndSet(location, dateTime)

    println("For the day: ${dateTime.dayOfWeek} (${dateTime.day}.${dateTime.month}), the sunrise at: ${sunrisesunset.sunriseTime}, and sunset at: ${sunrisesunset.sunsetTime}.")
}

suspend fun testWeekWeather(){
    val week = repo.getWeatherWeek( dateTime, location)

    week.forEach {
        println(
            "Temp for ${it.time.dayOfWeek}: ${it.temperature}"
        )
    }
}