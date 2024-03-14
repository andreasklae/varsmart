package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.gson.gson
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location


class WarningDataSource{


    private val client = HttpClient(CIO) {

        defaultRequest {

            url("https://gw-uio.intark.uh-it.no/in2000/")
            header("X-Gravitee-API-Key", "5bf87cd2-0fb4-498a-8dae-9cd509071bf8")
        }

        install(ContentNegotiation) {
            gson()
        }
    }




    suspend fun fetchAllWarnings(): Warning{

    //Coordinates API-call
        val source = "weatherapi/metalerts/2.0/current.json"

        val alerts: AlertResponse= client.get(source).body()

        return Warning( weatherAlert = alerts)

    }


    suspend fun fetchWarningCoordinates(loc: Location): Warning{
        val source = "weatherapi/metalerts/2.0/all.json?lat=${loc.lat}&lon=${loc.lon}"
        val alerts: AlertResponse= client.get(source).body()
        return Warning(weatherAlert = alerts)

    }



}