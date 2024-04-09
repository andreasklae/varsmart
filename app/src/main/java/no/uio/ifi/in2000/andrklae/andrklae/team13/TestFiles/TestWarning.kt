package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository


suspend fun main(){
    testWarning()
}

suspend fun testWarning(){

    val name = "Custom location"
    val type = "By"
    val fylke = ""
    val lat = 72.5
    val lon = 0.0

    val customLocation = CustomLocation(name, lat, lon, type, fylke)

    val warningRepo = WarningRepository()
    val result = warningRepo
        .findClosestCoordinate(
            customLocation,
            warningRepo.fetchAllWarnings().features
        )

    if (result != null){
        println(
            "Most nearby alert found at area: ${result.properties.area} \n" +
                "Title: ${result.properties.title}"
        )
    }
    else{
        println("No nearby alerts")
    }
}
