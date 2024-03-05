package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location

suspend fun fetchWeather(loc: Location, dateTime: DateTime): WeatherTimeForecast {
    // finds latitude and longitude
    val lat = loc.lat
    val lon = loc.lon

    // starts a client
    val client = HttpClient(CIO) {
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

    // sets the url based on location
    val source = "weatherapi/locationforecast/2.0/compact?lat=$lat&lon=$lon"





    // returns WeatherTimeForecast object
    val weatherForecast: WeatherForecast = client.get(source).body()
    return WeatherTimeForecast(weatherForecast, dateTime, loc)
}

