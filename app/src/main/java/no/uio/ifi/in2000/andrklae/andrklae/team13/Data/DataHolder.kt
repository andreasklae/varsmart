package no.uio.ifi.in2000.andrklae.andrklae.team13.Data

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT.GPTRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT.GPTRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Alert
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository
import java.time.LocalDateTime

// holds on to data for a given location.
// puts weather, gpt messages, location, sunrise/set, warnings and time in one object
data class DataHolder(
    var location: CustomLocation
) {
    // variables for weather
    var weather: WeatherForecast? = null
    val weatherStatus = mutableStateOf(Status.LOADING)
    var currentWeather: WeatherTimeForecast? = null
    var next24h: List<WeatherTimeForecast> = listOf()
    var week: List<WeatherTimeForecast> = listOf()
    var highest: Double? = null
    var lowest: Double? = null


    // variables for gpt
    val gptCurrent = mutableStateOf("")
    val gpt24h = mutableStateOf("")
    val gptWeek = mutableStateOf("")

    // variables for sunrise and sunset
    var rise: String? = null
    var set: String? = null
    val sunStatus = mutableStateOf(Status.LOADING)

    // variables for alerts
    var allWarnings: Warning? = null
    var alertList = listOf<Alert>()
    val alertStatus = mutableStateOf(Status.LOADING)


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

        val wRepo: WeatherRepositoryInterface = WeatherRepository()
        val aRepo: WarningRepositoryInterface = WarningRepository()
        val gptRepo: GPTRepositoryInterface = GPTRepository()
        val locRepo: LocationRepositoryInterface = LocationRepository()

        // location to show on homeScreen if there are no favourites
        val initLocation = DataHolder(
            CustomLocation(
                "Ålesund",
                62.47,
                6.15,
                "Ålesund",
                "Møre og Romsdal"
            )
        )
        val Favourites = mutableStateListOf<DataHolder>()
        suspend fun setFavourites(favorites: List<DataHolder>) {
            favorites.forEach {
                if (it.location.name.uppercase().equals("MIN POSISJON")) {
                    it.location = locRepo.coordsToCity(it.location.lat, it.location.lon)
                        ?.let { customLocation ->
                            customLocation
                        } ?: it.location
                    Favourites.add(it)
                } else {
                    Favourites.add(it)
                }
            }
        }

    }

    // function to either add or remove object from favourite list
    fun toggleInFavourites() {
        //if the location is in the list
        if (!Favourites.contains(this)) {
            Favourites.add(this)
        } else {
            Favourites.remove(this)
        }
    }

    suspend fun updateAll() {
        updateWeather()
        updateWarning()
        updateSunriseAndSunset()
    }

    fun findHighestAndLowestTemp(current: WeatherTimeForecast) {
        val templist = mutableListOf<Double>(current.temperature!!.toDouble())
        next24h.forEach {
            // makes sure that it only fetches the temperatures for this day and not tomorrow
            // note: the api doesn't fetch data more than 2 hours in the past,
            // so the highest and lowest will not include past temperatures
            if (it.time.day.toInt() == currentDay.toInt()) {
                templist.add(it.temperature!!.toDouble())
            }
        }

        highest = templist.max()
        lowest = templist.min()
    }

    suspend fun updateWeather() {
        // sets status to loading
        weatherStatus.value = Status.LOADING
        // fetches weather data
        val newWeather = wRepo.getWeather(location)

        // if api call is successfull
        if (newWeather != null) {
            // sets last update to now
            lastUpdate = getCurrentTime()
            weather = newWeather
            currentWeather = WeatherTimeForecast(newWeather, dt, location)
            updateNext24h(newWeather)
            findHighestAndLowestTemp(currentWeather!!)
            updateWeek(newWeather)
            // sets status to success
            weatherStatus.value = Status.SUCCESS
        } else {
            weatherStatus.value = Status.FAILED
        }
    }

    suspend fun updateGPTCurrent(age: Int, hobbies: List<String>) {
        gptCurrent.value = ""
        // fetches from api
        gptCurrent.value = gptRepo.fetchCurrent(currentWeather!!, next24h, age, hobbies, alertList)
    }

    suspend fun updateGPT24h(age: Int) {
        gpt24h.value = ""
        gpt24h.value = gptRepo.fetch24h(next24h, age)
    }

    suspend fun updateGPTWeek(age: Int, hobbies: List<String>) {
        gptWeek.value = ""
        // fetches from api
        gptWeek.value = gptRepo.fetchWeek(week, age, hobbies)
    }

    suspend fun updateNext24h(newWeather: WeatherForecast) {
        // new list for the next 24 hours
        val newList = mutableListOf<WeatherTimeForecast>()
        // a list of datetimes from the api
        val timelist = newWeather.properties.timeseries
        // finds the index of the current time in the datetime list
        val currentTime = timelist.first{
            // matches the hour
            it.time.split("T")[1].split(":")[0] == dt.hour
        }
        val startIndex = newWeather.properties.timeseries.indexOf(currentTime)

        // for loop that runs 23 times and finds the weather
        for (i in 1..24) {
            // creates a datetime object
            val time = newWeather.properties.timeseries[i + startIndex].time
            val date = time.split("T")[0].split("-")
            val year = date[0]
            val month = date[1]
            val day = date[2]
            val hour = time.split("T")[1].split(":")[0]
            val dateTime = DateTime(
                year = year,
                initMonth = month,
                initDay = day,
                initHour = hour
            )

            // creates a weatherTimeForecast object
            val forecast = WeatherTimeForecast(
                newWeather,
                dateTime,
                location
            )

            // adds it to the list
            newList.add(forecast)
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
        weekDays.forEach {
            newList.add(WeatherTimeForecast(newWeather, it, location))
        }
        week = newList

    }

    suspend fun updateSunriseAndSunset() {
        // sets status to loading
        sunStatus.value = Status.LOADING
        // fetches from api
        val newSun = wRepo.getRiseAndSet(location, dt)

        // if api call is successful
        if (newSun != null) {
            rise = newSun.sunriseTime
            set = newSun.sunsetTime

            // sets status to success
            sunStatus.value = Status.SUCCESS
        }
        // if api call fails
        else {
            sunStatus.value = Status.FAILED

        }
    }

    suspend fun updateWarning() {
        // starts by setting the status to loading
        alertStatus.value = Status.LOADING
        // fetches from api
        val newWarnings = aRepo.fetchAllWarnings()
        // if api call is successful
        if (newWarnings != null) {
            alertList = aRepo.fetchAlertList(newWarnings, location)
            allWarnings = newWarnings

            // sets status to success
            alertStatus.value = Status.SUCCESS
        }
        // if api call fails
        else {
            alertStatus.value = Status.FAILED
        }
    }

    // function to get the current time
    fun getCurrentTime(): DateTime {
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
}
