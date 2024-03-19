package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.LocationRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseAndSunset
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseDataSource

class WeatherRepository {
    val wDataSource = WeatherDataSource()
    val locRepo = LocationRepository()
    val sunrisesunsetData = SunriseDataSource()

    suspend fun getLocation(search: String): CustomLocation{
        return locRepo.getLocations(search).first()
    }
    suspend fun getCurrentWeather(time: DateTime, customLocation: CustomLocation): WeatherTimeForecast? {
        try {
            return wDataSource.fetchWeather(customLocation, time)
        }
        catch (exception: Exception){
            return  null
        }
        
    }

    suspend fun getWeather24h(time: DateTime, customLocation: CustomLocation): List<WeatherTimeForecast>{
        val hour = time.hour

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
        
        try {
            // creates a list of weather objects for the next 24h
            val weather = mutableListOf<WeatherTimeForecast>()
            hours.forEach {
                weather.add(wDataSource.fetchWeather(customLocation, it))
            }

            return weather
        }
        catch (exception: Exception){
            println("Error fetching Weather")
            return emptyList()
        }
        
    }

    suspend fun getWeatherWeek(time: DateTime, customLocation: CustomLocation): List<WeatherTimeForecast>{
        
        // Creates a list of days for the week
        val week = mutableListOf<DateTime>()
        var dayIterator = time
        for (i in 0..6) {
            val nextDay = time.getNextDay(dayIterator)
            week.add(nextDay)
            dayIterator = nextDay
        }
        
        // creates a list of weather objects for the next week
        val weather = mutableListOf<WeatherTimeForecast>()
        week.forEach {
            weather.add(wDataSource.fetchWeather(customLocation, it))
        }

        return weather
        
        
    }

    suspend fun getRiseAndSet(loc: CustomLocation, time: DateTime): SunriseAndSunset {
        return sunrisesunsetData.fetchSunriseandSunset(loc, time)
    }




}