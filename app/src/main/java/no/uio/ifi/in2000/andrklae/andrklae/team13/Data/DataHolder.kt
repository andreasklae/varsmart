package no.uio.ifi.in2000.andrklae.andrklae.team13.Data

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise.SunriseAndSunset
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature

data class DataHolder(
    var lastUpdate: DateTime,
    var currentWeather: WeatherTimeForecast?,
    var week: List<WeatherTimeForecast>,
    var day: List<WeatherTimeForecast>,
    var warningFeature: Feature?,
    var rise: String,
    var set: String

){
    companion object { val favourites = mutableListOf<DataHolder>() }

    init {
        favourites.add(this)
    }

    fun removeFromFavorites(){
        favourites.remove(this)
    }
}
