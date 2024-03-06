package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast

//https://ws.geonorge.no/adresser/v1/openapi.json


class LocationDataSource() {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json{ignoreUnknownKeys = true})
        }
    }

    suspend fun fetchAddresses(search:String): List<Location> {

        val path="https://ws.geonorge.no/adresser/v1/sok?sok=$search&fuzzy=false&sokemodus=OR&utkoordsys=4258&treffPerSide=10&side=0&asciiKompatibel=true"
        val response: ApiResponse = client.get(path).body()
        return response.adresser.map { address ->
            Location(
                name = address.adressetekst,
                lat = address.representasjonspunkt.lat,
                lon = address.representasjonspunkt.lon
            )
        }
    }
}

