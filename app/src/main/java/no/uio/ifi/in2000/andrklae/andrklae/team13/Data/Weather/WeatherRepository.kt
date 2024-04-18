package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseAndSunset
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseDataSource

class WeatherRepository {
    val wDataSource = WeatherDataSource()
    val locRepo = LocationRepository()
    val sunrisesunsetData = SunriseDataSource()

    suspend fun getLocation(search: String): CustomLocation {
        return locRepo.getLocations(search).first()
    }

    suspend fun getWeather(customLocation: CustomLocation): WeatherForecast? {
        try {
            val weather = wDataSource.fetchWeather(customLocation)
            return weather
        } catch (exception: Exception) {
            return null
        }

    }

    suspend fun getRiseAndSet(loc: CustomLocation, time: DateTime): SunriseAndSunset? {
        try {
            return sunrisesunsetData.fetchSunriseandSunset(loc, time)
        } catch (exception: Exception) {
            return null
        }

    }


}