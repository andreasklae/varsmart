package no.uio.ifi.in2000.andrklae.andrklae.team13.Data

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.time.delay
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
import kotlin.time.Duration

data class DataHolder(
    val location: CustomLocation
){
    val statusStates: List<String> = listOf("Loading", "Success", "Failed")

    // variables for weather
    var weather: WeatherForecast? = null
    val weatherStatus = mutableStateOf(statusStates[0])
    var currentWeather: WeatherTimeForecast? = null

    val mainGptStatus = mutableStateOf(statusStates[0])
    var mainGpt = ""

    var next24h: List<WeatherTimeForecast> = listOf()
    var week: List<WeatherTimeForecast> = listOf()
    var weekGpt = ""

    // variables for sunrise and sunset
    var rise: String? = null
    var set: String? = null
    val sunStatus = mutableStateOf(statusStates[0])

    // variables for alerts
    var allWarnings: Warning? = null
    var alertList = listOf<Alert>()
    val alertStatus = mutableStateOf(statusStates[0])


    var current = LocalDateTime.now()
    var currentYear = current.year.toString()
    var currentMonth = current.monthValue.toString()
    var currentDay = current.dayOfMonth.toString()
    var currentHour = current.hour.toString()
    var currentMinute = current.minute.toString()
    var dt = DateTime(
        currentYear,
        currentMonth,
        currentDay,
        currentHour,
        currentMinute
    )
    var lastUpdate = dt



    @SuppressLint("MutableCollectionMutableState")
    companion object {
        val Favourites = mutableStateListOf<DataHolder>()
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
        updateWeather()
        updateWarning()
        updateSunriseAndSunset()
        updateGPTCurrent()
    }

    suspend fun updateWeather() {
        // sets status to loading
        weatherStatus.value = statusStates[0]
        // fetches weather data
        val newWeather = wRepo.getWeather(location)

        // if api call is successfull
        if (newWeather != null){
            // sets last update to now
            lastUpdate = getCurrentTime()
            weather = newWeather
            currentWeather = WeatherTimeForecast(newWeather, dt, location)
            updateNext24h(newWeather)
            updateWeek(newWeather)
            // sets status to success
            weatherStatus.value = statusStates[1]
            // updates Gpt advice
            updateGPTCurrent()

        }
        else{
            weatherStatus.value = statusStates[2]
        }
    }

    suspend fun updateGPTCurrent(){
        // sets status to loading
        mainGptStatus.value = statusStates[0]

        // fetches from api
        val newResponse = gptRepo.fetchCurrent(currentWeather!!, next24h)
        if (newResponse != null){
            mainGpt = newResponse
            mainGptStatus.value = statusStates[1]
        }
        else{
            mainGptStatus.value = statusStates[2]
        }
    }

    suspend fun updateNext24h(newWeather: WeatherForecast){
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
            } else{
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
            newList.add(WeatherTimeForecast(newWeather, it, location))
        }
        next24h = newList

    }

    suspend fun updateWeek(newWeather: WeatherForecast) {
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
            newList.add(WeatherTimeForecast(newWeather, it, location))
        }
        week = newList

    }

    suspend fun updateSunriseAndSunset(){
        // sets status to loading
        sunStatus.value = statusStates[0]
        // fetches from api
        val newSun = wRepo.getRiseAndSet(location, dt)

        // if api call is successful
        if(newSun != null){
            rise = newSun.sunriseTime
            set = newSun.sunsetTime

            // sets status to success
            sunStatus.value = statusStates[1]
        }
        // if api call fails
        else{
            sunStatus.value = statusStates[2]

        }
    }

    suspend fun updateWarning(){
        // starts by setting the status to loading
        alertStatus.value = statusStates[0]
        // fetches from api
        val newWarnings = aRepo.fetchAllWarnings()
        // if api call is successful
        if (newWarnings != null){
            alertList = aRepo.fetchAlertList(newWarnings, location)
            allWarnings = newWarnings

            // sets status to success
            alertStatus.value = statusStates[1]
        }
        // if api call fails
        else{
            alertStatus.value = statusStates[1]
        }
    }

    fun getCurrentTime(): DateTime{
        var current = LocalDateTime.now()
        var currentYear = current.year.toString()
        var currentMonth = current.monthValue.toString()
        var currentDay = current.dayOfMonth.toString()
        var currentHour = current.hour.toString()
        var currentMinute = current.minute.toString()
        var dt = DateTime(
            currentYear,
            currentMonth,
            currentDay,
            currentHour,
            currentMinute
        )
        return dt
    }

    suspend fun updateGPTWeek() {
        weekGpt = "gptRepo.fetchWeek(next24h)"
    }
}
