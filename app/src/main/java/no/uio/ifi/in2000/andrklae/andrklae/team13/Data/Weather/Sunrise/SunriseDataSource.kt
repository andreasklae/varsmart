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

// Class which fetches the sunrise and sunset times for a given day and timezone
class SunriseDataSource {
    // starts a client
    private val client = HttpClient(CIO) {

        defaultRequest {
            // Establish connection via UiO's Proxy
            url("https://gw-uio.intark.uh-it.no/in2000/")
            header("X-Gravitee-API-Key", "5bf87cd2-0fb4-498a-8dae-9cd509071bf8")
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    // Function that returns a SunriseAndSunset object from MET-API
    suspend fun fetchSunriseandSunset(loc: CustomLocation, dateTime: DateTime): SunriseAndSunset {
        // finds latitude and longitude
        val lat = loc.lat
        val lon = loc.lon



        // sets the url based on location and timezone
        val source = "weatherapi/sunrise/3.0/sun?lat=$lat&lon=$lon&date=${dateTime.year}-${dateTime.month}-${dateTime.day}&offset=+01:00"
        // returns the object
        val sunriseData: SunriseData = client.get(source).body()
        return SunriseAndSunset(sunriseData, loc, dateTime)
    }
}