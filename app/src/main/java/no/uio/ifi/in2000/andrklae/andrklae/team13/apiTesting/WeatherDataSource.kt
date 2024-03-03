import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.andrklae.andrklae.team13.apiTesting.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.apiTesting.Location
import no.uio.ifi.in2000.andrklae.andrklae.team13.apiTesting.WeatherTimeForecast

suspend fun fetchWeather(loc: Location, dateTime: DateTime): WeatherTimeForecast {
    // finds latitude and longitude
    val lat = loc.lat
    val lon = loc.lon

    // starts a client
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    // sets the url based on location
    val url = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=$lat&lon=$lon"

    // returns WeatherTimeForecast object
    val weatherForecast: WeatherForecast = client.get(url).body()
    return WeatherTimeForecast(weatherForecast, dateTime, loc)
}

