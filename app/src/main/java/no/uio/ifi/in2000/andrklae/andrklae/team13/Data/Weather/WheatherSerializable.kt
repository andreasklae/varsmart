package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

import kotlinx.serialization.Serializable

// Serializable class to hold information from the JSON file
@Serializable
data class WeatherForecast(
    val properties: Properties
) {
    override fun toString(): String {
        return properties.toString()
    }
}

@Serializable
data class Properties(
    val timeseries: List<TimeSeries>
) {
    override fun toString(): String {
        return timeseries.joinToString(separator = "\n", prefix = "Forecast:\n", limit = 5, truncated = "...") { it.toString() }
    }
}

@Serializable
data class TimeSeries(
    val time: String,
    val data: Data
) {
    override fun toString(): String {
        return "Time: $time, Data: $data"
    }
}

@Serializable
data class Data(
    val instant: InstantDetails
    // Add more fields for next_1_hours, next_6_hours, etc., if needed
) {
    override fun toString(): String {
        return instant.toString()
    }
}

@Serializable
data class InstantDetails(
    val details: InstantWeatherDetails
) {
    override fun toString(): String {
        return details.toString()
    }
}

@Serializable
data class InstantWeatherDetails(
    val air_pressure_at_sea_level: Double,
    val air_temperature: Double,
    val cloud_area_fraction: Double,
    val relative_humidity: Double,
    val wind_speed: Double
    // Add other relevant fields
) {
    override fun toString(): String {
        return "Pressure: $air_pressure_at_sea_level hPa, Temperature: $air_temperature °C, Cloud Cover: $cloud_area_fraction%, Humidity: $relative_humidity%, Wind Speed: $wind_speed m/s"
    }
}
