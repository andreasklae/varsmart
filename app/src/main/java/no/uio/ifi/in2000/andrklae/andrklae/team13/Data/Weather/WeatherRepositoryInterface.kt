package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepositoryInterface
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseAndSunset
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseDataSource

interface WeatherRepositoryInterface {
    val wDataSource: WeatherDataSource
    val locRepo: LocationRepositoryInterface
    val sunrisesunsetData: SunriseDataSource
    suspend fun getLocation(search: String): CustomLocation

    suspend fun getWeather(customLocation: CustomLocation): WeatherForecast?

    suspend fun getRiseAndSet(loc: CustomLocation, time: DateTime): SunriseAndSunset?


}