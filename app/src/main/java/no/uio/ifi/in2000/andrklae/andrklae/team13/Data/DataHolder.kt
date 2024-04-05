package no.uio.ifi.in2000.andrklae.andrklae.team13.Data

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository
import java.time.LocalDateTime

data class DataHolder(
    val location: CustomLocation
){

    var current = LocalDateTime.now()
    var currentYear = current.year.toString()
    var currentMonth = current.monthValue.toString()
    var currentDay = current.dayOfMonth.toString()
    var currentHour = current.hour.toString()
    var dt = DateTime(
        currentYear,
        currentMonth,
        currentDay,
        currentHour
    )
    var lastUpdate = dt

    var currentWeather: WeatherTimeForecast? = null
    var next24h: List<WeatherTimeForecast> = listOf()
    var week: List<WeatherTimeForecast> = listOf()
    var rise: String? = null
    var set: String? = null
    var warning: Feature? = null
    companion object {
        val Favourites = mutableListOf<DataHolder>()
        val wRepo = WeatherRepository()
        val aRepo = WarningRepository()
    }

    init {
        Favourites.add(this)
        println("Creating data for ${location.name} and adding to favourites")
        println("Favorites: ")
        Favourites.forEach{
            println("\t ${it.location.name}")
        }

    }
    fun removeFromFavorites(){
        Favourites.remove(this)
    }

    suspend fun updateCurrentWeather() {
        // if there is more than 1 hour since last update
        if(lastUpdate > getCurrentTime() || currentWeather == null){
            lastUpdate = getCurrentTime()
            currentWeather = wRepo.getCurrentWeather(dt, location)
        }
    }

    suspend fun updateNext24h(){
        if(lastUpdate > getCurrentTime() || next24h.isEmpty()){
            lastUpdate = getCurrentTime()
            next24h = wRepo.getWeather24h(dt, location)
        }
    }
    suspend fun updateWeek(){
        if(lastUpdate > getCurrentTime() || week.isEmpty()){
            lastUpdate = getCurrentTime()
            week = wRepo.getWeatherWeek(dt, location)
        }
    }

    suspend fun updateSunriseAndSunset(){
        if(lastUpdate > getCurrentTime() || rise == null || set == null){
            lastUpdate = getCurrentTime()
            val sun = wRepo.getRiseAndSet(location, dt)
            rise = sun.sunriseTime
            set = sun.sunsetTime
        }
    }

    suspend fun updateWarning(){
        val warnings = aRepo.fetchAllWarnings().features
        warning = aRepo.findClosestCoordinate(location, warnings)
    }

    fun getCurrentTime(): DateTime{
        var current = LocalDateTime.now()
        var currentYear = current.year.toString()
        var currentMonth = current.monthValue.toString()
        var currentDay = current.dayOfMonth.toString()
        var currentHour = current.hour.toString()
        var dt = DateTime(
            currentYear,
            currentMonth,
            currentDay,
            currentHour
        )
        return dt
    }
}
