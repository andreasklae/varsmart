import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.andrklae.andrklae.team13.apiTesting.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.apiTesting.Location
import no.uio.ifi.in2000.andrklae.andrklae.team13.apiTesting.WeatherTimeForecast

// function to test api
suspend fun main() {

    // specifies time and location
    val name = "Oslo"
    val lat = 59.91
    val lon = 10.71

    val location = Location(name, lon, lat)

    val year = "2024"
    val month = "03"
    val day = "03"
    val hour = "19"

    val dateTime = DateTime(year, month, day, hour)

    // fetches forecast for a specific location and time
    val weatherTimeForecast = fetchWeather(location, dateTime) // Replace with an actual call to fetch data

    // Print the weather forecast for the specified time
    println(weatherTimeForecast)
}