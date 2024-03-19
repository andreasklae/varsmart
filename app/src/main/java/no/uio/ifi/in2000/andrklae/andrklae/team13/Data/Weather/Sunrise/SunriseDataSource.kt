package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Sunrise

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation

/* Dette er KUN for å teste at selve API-kallet fungerer og må flyttes til en egen klasse etter
merge til main
suspend fun main() {

    // specifies time and location
    val name = "Oslo"
    val type = "By"
    val fylke = "Oslo"
    val lat = 59.91
    val lon = 10.71

    val location = Location(name, lon, lat, type, fylke)

    val year = "2024"
    val month = "03"
    val day = "13"
    val hour = "16"

    val dateTime = DateTime(year, month, day, hour)

    val dataSource = SunriseDataSource()

    // fetches forecast for a specific location and time
    val sunsetAndSunrise = dataSource.fetchSunriseandSunset(location, dateTime) // Replace with an actual call to fetch data

    // Print the weather forecast for the specified time
    println(sunsetAndSunrise)
}

 */
class SunriseDataSource {
    // starts a client
    private val client = HttpClient(CIO) {

        defaultRequest {

            url("https://gw-uio.intark.uh-it.no/in2000/")
            header("X-Gravitee-API-Key", "5bf87cd2-0fb4-498a-8dae-9cd509071bf8")
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun fetchSunriseandSunset(loc: CustomLocation, dateTime: DateTime): SunriseAndSunset {
        // finds latitude and longitude
        val lat = loc.lat
        val lon = loc.lon



        // sets the url based on location
        val source = "weatherapi/sunrise/3.0/sun?lat=$lat&lon=$lon&date=${dateTime.year}-${dateTime.month}-${dateTime.day}&offset=+01:00"

        // returns WeatherTimeForecast object
        val sunriseData: SunriseData = client.get(source).body()
        return SunriseAndSunset(sunriseData, loc, dateTime)
    }
}