package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation

/*
* This data class holds weather variables for a given location and time
*
* The following values can be accessed:
* specifiedTime -- Gives time and date in iso format
* airPressure -- A Double containing air pressure as hPa value
* temperature -- given as a Double measured in Celsius
* cloudCoverage -- A Double. is measured as a percentage
* humidity -- A Double. is measured as a percentage
* windSpeed -- A Double. Measured in m/s. wind direction is not specified
*
*/
data class WeatherTimeForecast(
    val weatherForecast: WeatherForecast,
    val time: DateTime,
    val customLocation: CustomLocation
) {
    val specifiedTime = time.isoFormat
    val timeSeries: TimeSeries? = weatherForecast.properties.timeseries.find { it.time.startsWith(specifiedTime) }
    val airPressure: Double? = timeSeries?.data?.instant?.details?.air_pressure_at_sea_level
    val temperature: Double? = timeSeries?.data?.instant?.details?.air_temperature
    val cloudCoverage: Double? = timeSeries?.data?.instant?.details?.cloud_area_fraction
    val humidity: Double? = timeSeries?.data?.instant?.details?.relative_humidity
    val windSpeed: Double? = timeSeries?.data?.instant?.details?.wind_speed

    override fun toString(): String {
        return if (timeSeries != null) {
            "Weather Forecast for ${customLocation.name} at ${time}:\n" +
                    "Air Pressure: $airPressure hPa\n" +
                    "Temperature: $temperature °C\n" +
                    "Cloud Coverage: $cloudCoverage%\n" +
                    "Humidity: $humidity%\n" +
                    "Wind Speed: $windSpeed m/s"
        } else {
            "No weather data available for $specifiedTime"
        }
    }
}