package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningDataSource




    suspend fun main(){
        testWarning()

    }

    suspend fun testWarning(){
        val warningData= WarningDataSource()

       val lon= 14.3706
       val lat= 67.2832

       val location: Location= Location(name = "Oslo", lon=lon, lat=lat, type = "By", fylke = "Oslo")

       val warning: Warning = warningData.fetchWarnings()

        println(warning.properties.size)



    }



