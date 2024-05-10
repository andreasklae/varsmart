package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata

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
            json(Json { ignoreUnknownKeys = true })
        }
    }

    // function for searching for a location based on a text
    suspend fun searchLocations(search: String): List<CustomLocation> {
        val APIKey = "AIzaSyBofr2wZtjab3DBuYh46BDxeUWUit5l-sw"
        val path =
            "https://maps.googleapis.com/maps/api/geocode/json?components=route:" +
                    "$search|country:NO&language=no&key=$APIKey"

        try {
            val response: Root = client.get(path).body()
            return response.results.map { result ->
                // creates a location object based on the api response.
                // Api response is not flawless in terms of the names it provides,
                //so it needs a bit of formatting
                CustomLocation(
                    name = result.formatted_address.split(", ").first(),
                    lon = result.geometry.location.lng,
                    lat = result.geometry.location.lat,
                    postSted = result.formatted_address
                        .split(", ")
                        [result.formatted_address.split(", ").size - 2]
                        .replace("[0-9\\s]+".toRegex(), ""),
                    fylke = result.address_components.find {
                        it.types.first() == "administrative_area_level_1"
                    }!!.short_name
                )
            }
        }
        // if the api fails
        catch (e: Exception) {
            return emptyList()
        }
    }

    // function for finding the name of a location based on coordinates
    suspend fun reverseGeocoding(lat: Double, lon: Double): String {
        val APIKey = "AIzaSyBofr2wZtjab3DBuYh46BDxeUWUit5l-sw"
        val path =
            "https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lon}\n" +
                    "&location_type=ROOFTOP&result_type=street_address&key=${APIKey}\n"
        try {
            val response: GeocodingResponse = client.get(path).body()

            return extractName(response)

        } catch (e: Exception) {
            println("fant ikke response")
            return ""
        }
    }

    fun extractName(geocodingResponse: GeocodingResponse): String {

        if (!(geocodingResponse.status.equals("ZERO_RESULTS"))) {
            geocodingResponse.results[0].address_components.forEach {
                if (it.types.contains("administrative_area_level_1")) {
                    return it.long_name
                }
            }
        } else {
            println("forsøker å returnere country backup")
            val words = geocodingResponse.plus_code.compound_code.split(" ")
            if (words.size <= 1) {
                return " på havet"
            } else {
                return words.subList(1, words.size - 1).joinToString(" ").replace(",", "")
            }
        }
        return ""
    }
}

