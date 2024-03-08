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

        val path="https://api.kartverket.no/stedsnavn/v1/navn?sok=$search&utkoordsys=4258&treffPerSide=10&side=1"
        val response: ApiResponse = client.get(path).body()
        return response.navn.map { navnItem ->
            Location(
                name = navnItem.skrivemåte,
                lon = navnItem.representasjonspunkt.øst,
                lat = navnItem.representasjonspunkt.nord,
                type = navnItem.navneobjekttype,
                fylke = navnItem.fylker.joinToString(separator = ", ") { it.fylkesnavn } // Assumes there can be more than one fylke; adjust as needed
            )
        }
    }
}

