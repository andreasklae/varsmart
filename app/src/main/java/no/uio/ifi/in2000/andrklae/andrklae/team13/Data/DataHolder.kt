package no.uio.ifi.in2000.andrklae.andrklae.team13.Data

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT.GPTRepo
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Alert
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
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

    var weather: WeatherForecast? = null
    var currentWeather: WeatherTimeForecast? = null
    var GPTCurrent = ""

    var next24h: List<WeatherTimeForecast> = listOf()
    var week: List<WeatherTimeForecast> = listOf()
    var gptWeek = ""

    var rise: String? = null
    var set: String? = null

    var allWarnings: Warning? = null
    var alertList = listOf<Alert>()
    companion object {
        val Favourites = mutableListOf<DataHolder>()
        val wRepo = WeatherRepository()
        val aRepo = WarningRepository()
        val gptRepo = GPTRepo()
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

    suspend fun updateAll() {
        // if there is more than 1 hour since last update
        if(lastUpdate > getCurrentTime() || weather == null){
            lastUpdate = getCurrentTime()
            weather = wRepo.getWeather(location)
            updateCurrentWeather()
            updateNext24h()
            updateWeek()
            updateWarning()
            updateSunriseAndSunset()
        }
    }

    suspend fun updateCurrentWeather() {
        if (weather != null){
            currentWeather = WeatherTimeForecast(weather!!, dt, location)
        }
    }

    suspend fun updateGPTCurrent(){
        if (weather != null){
            GPTCurrent = "gptRepo.fetchCurrent(currentWeather!!, next24h)"
        }
    }

    suspend fun updateNext24h(){
        if (weather != null){
            val time = dt
            val hour = dt.hour

            // Creates DateTime objects for the next 24 Hours
            val hours = mutableListOf<DateTime>()
            for (i in 1..24) {

                // makes sure that it is the correct day
                if (hour.toInt() + i < 24){
                    hours.add(
                        DateTime(
                            time.year,
                            time.month,
                            time.day,
                            (time.hour.toInt() + i).toString()
                        )
                    )
                }
                else{
                    hours.add(
                        DateTime(
                            time.year,
                            time.month,
                            (time.day.toInt() + 1).toString(),
                            (time.hour.toInt() + i - 24).toString()
                        )
                    )
                }
            }

            val newList = mutableListOf<WeatherTimeForecast>()
            // add a weather object for each hour in a day
            hours.forEach{
                newList.add(WeatherTimeForecast(weather!!, it, location))
            }
            next24h = newList
        }
    }
    suspend fun updateWeek(){
        if (weather != null){
            // Creates a list of days for the week
            var weekDays = mutableListOf<DateTime>()
            var dayIterator = dt
            for (i in 0..6) {
                val nextDay = dt.getNextDay(dayIterator)
                weekDays.add(nextDay)
                dayIterator = nextDay
            }

            val newList = mutableListOf<WeatherTimeForecast>()
            // add a weather object for each hour in a day
            weekDays.forEach{
                newList.add(WeatherTimeForecast(weather!!, it, location))
            }
            week = newList
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
        allWarnings = aRepo.fetchAllWarnings()
        alertList = aRepo.fetchAlertList(allWarnings!!, location)
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

    suspend fun updateGPTWeek() {
        gptWeek = "gptRepo.fetchWeek(next24h)"
    }
}
