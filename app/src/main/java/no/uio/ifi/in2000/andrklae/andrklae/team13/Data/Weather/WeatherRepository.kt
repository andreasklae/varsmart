package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseAndSunset
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseDataSource

class WeatherRepository() : WeatherRepositoryInterface {
    override val wDataSource = WeatherDataSource()
    override val locRepo: LocationRepositoryInterface = LocationRepository()
    override val sunrisesunsetData = SunriseDataSource()

    override suspend fun getLocation(search: String): CustomLocation {
        return locRepo.getLocations(search).first()
    }

    override suspend fun getWeather(customLocation: CustomLocation): WeatherForecast? {
        try {
            val weather = wDataSource.fetchWeather(customLocation)
            return weather
        } catch (exception: Exception) {
            return null
        }

    }

    override suspend fun getRiseAndSet(loc: CustomLocation, time: DateTime): SunriseAndSunset? {
        try {
            return sunrisesunsetData.fetchSunriseandSunset(loc, time)
        } catch (exception: Exception) {
            return null
        }

    }


}