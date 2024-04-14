package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Alert
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository
import kotlin.math.roundToInt


suspend fun main(){
    testWarning()
}

suspend fun testWarning(){

    val name = "Custom location"
    val lat = 58.0377
    val lon = 7.1592

    val customLocation = CustomLocation(name, lat, lon, "","")

    val warningRepo = WarningRepository()
    val allWarnings = warningRepo.fetchAllWarnings()
    var alerts = listOf<Alert>()
    if (allWarnings != null){
        alerts = warningRepo.fetchAlertList(allWarnings, customLocation)

    }

    if (alerts.isNotEmpty()){
        alerts.forEach {
            println(
                "${it.alert.properties.area}, " +
                        "${it.alert.properties.eventAwarenessName}: " +
                        "${it.distance.roundToInt()}km unna"
            )
        }
    }
    else{
        println("No nearby alerts")
    }
}
