package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningDataSource
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository


suspend fun main(){
        testWarning()
    }

suspend fun testWarning(){

    val name = "Oslo"
    val type = "By"
    val fylke = "Oslo"
    val lat = 59.91
    val lon = 10.71

    val location = Location(name, lon, lat, type, fylke)

    val warningRepo = WarningRepository()
    val result = warningRepo.findClosestCoordinate(location, warningRepo.fetchAllWarnings().features)
    println("Most nearby alert found at area: ${result.properties.area} Title: ${result.properties.title}")
}
