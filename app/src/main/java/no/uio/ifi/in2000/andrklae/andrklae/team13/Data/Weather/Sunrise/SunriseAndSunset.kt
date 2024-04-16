package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation

// class for a SunriseAndSunset object
class SunriseAndSunset(
    val sunriseData: SunriseData,
    val customLocation: CustomLocation,
    val time: DateTime
    ) {
    // Variables that collects the sunrise and sunset times as strings
    val sunriseTime: String = sunriseData.properties.sunrise.time
    val sunsetTime: String = sunriseData.properties.sunset.time

    // toString method to test if the class contains information
    override fun toString(): String {
        return "Sunset and sunrise for ${customLocation.name} on ${time.day}.${time.month}.${time.year}:\n" +
                "Time for sunrise: $sunriseTime\n" +
                "Time for sunset: $sunsetTime"
    }
}
