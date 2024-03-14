package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import io.ktor.util.reflect.instanceOf
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningDataSource
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository


suspend fun main(){
        testWarning()

    }

    suspend fun testWarning(){
        val warningData= WarningDataSource()

        val name = "Oslo"
        val type = "By"
        val fylke = "Oslo"
        val lat = 59.91
        val lon = 10.71

        val location = Location(name, lon, lat, type, fylke)


       val warning: Warning = warningData.fetchAllWarnings()

       val warningRepo = WarningRepository()

    println("Most nearby alert found at: ${warningRepo.findClosestCoordinate(location, warning.features)}")
 }
