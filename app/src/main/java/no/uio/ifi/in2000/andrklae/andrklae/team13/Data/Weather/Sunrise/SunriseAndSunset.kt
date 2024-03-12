package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location


class SunriseAndSunset(
    val sunriseData: SunriseData,
    val location: Location,
    val time: DateTime
    ) {
    val sunriseTime: String = sunriseData.properties.sunrise.time
    val sunsetTime: String = sunriseData.properties.sunset.time

    override fun toString(): String {
        return "Sunset and sunrise for ${location.name} on ${time.day}.${time.month}.${time.year}:\n" +
                "Time for sunrise: $sunriseTime\n" +
                "Time for sunset: $sunsetTime"
    }
}
