package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationRepositoryImpl
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseAndSunset
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseDataSource

interface WeatherRepository {
    val wDataSource: WeatherDataSource
    val locRepo: LocationRepository
    val sunrisesunsetData: SunriseDataSource
    suspend fun getLocation(search: String): CustomLocation

    suspend fun getWeather(customLocation: CustomLocation): WeatherForecast?

    suspend fun getRiseAndSet(loc: CustomLocation, time: DateTime): SunriseAndSunset?


}