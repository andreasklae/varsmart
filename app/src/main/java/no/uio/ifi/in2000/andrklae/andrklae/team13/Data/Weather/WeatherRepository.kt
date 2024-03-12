package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location

class WeatherRepository {
    val dataSource = WeatherDataSource()
    suspend fun getWeather(time: DateTime, location: Location): WeatherTimeForecast{
        println("Fetching Weather for ${location.name}")
        val weather = dataSource.fetchWeather(location, time)
        return weather
    }




}