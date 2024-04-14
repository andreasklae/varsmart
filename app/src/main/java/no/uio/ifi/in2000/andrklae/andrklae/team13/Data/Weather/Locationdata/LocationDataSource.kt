package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

//https://ws.geonorge.no/adresser/v1/openapi.json


class LocationDataSource() {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json{ignoreUnknownKeys = true})
        }
    }

    suspend fun fetchAddresses(search:String): List<CustomLocation> {

        val path="https://api.kartverket.no/stedsnavn/v1/navn?sok=$search&utkoordsys=4258&treffPerSide=10&side=1"

        try {
            val response: ApiResponse = client.get(path).body()
            return response.navn.map { navnItem ->
                CustomLocation(
                    name = navnItem.skrivemåte,
                    lon = navnItem.representasjonspunkt.øst,
                    lat = navnItem.representasjonspunkt.nord,
                    type = navnItem.navneobjekttype,
                    fylke = navnItem.fylker.joinToString(separator = ", ") { it.fylkesnavn } // Assumes there can be more than one fylke; adjust as needed
                )
            }
        }
        catch (e: Exception){
            return emptyList()
        }

    }
}

