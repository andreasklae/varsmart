package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise

import kotlinx.serialization.Serializable

// Define the data classes
@Serializable
data class Geometry(
    val type: String,
    val coordinates: List<Double>
)

@Serializable
data class When(
    val interval: List<String>
)

@Serializable
data class Sunrise(
    val time: String,
    val azimuth: Double
)

@Serializable
data class Sunset(
    val time: String,
    val azimuth: Double
)

@Serializable
data class Solarnoon(
    val time: String,
    val disc_centre_elevation: Double,
    val visible: Boolean
)

@Serializable
data class Solarmidnight(
    val time: String,
    val disc_centre_elevation: Double,
    val visible: Boolean
)

@Serializable
data class Properties(
    val body: String,
    val sunrise: Sunrise,
    val sunset: Sunset,
    val solarnoon: Solarnoon,
    val solarmidnight: Solarmidnight
)

@Serializable
data class SunriseData(
    val copyright: String,
    val licenseURL: String,
    val type: String,
    val geometry: Geometry,
    val `when`: When,
    val properties: Properties
)