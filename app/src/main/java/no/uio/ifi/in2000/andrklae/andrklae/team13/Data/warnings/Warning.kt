package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location

data class Warning(
    //val date: DateTime,
    val weatherAlert: AlertResponse
) {

    val properties: List<Feature> = weatherAlert.features




    override fun toString(): String{
        return properties.toString()
    }






}