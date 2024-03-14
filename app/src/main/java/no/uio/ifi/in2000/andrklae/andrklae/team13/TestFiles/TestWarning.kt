package no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningDataSource
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository


suspend fun main(){
        testWarning()

    }

    suspend fun testWarning(){
        val warningData= WarningDataSource()

       val lon= 14.3706
       val lat= 67.2832


       val warning: Warning = warningData.fetchAllWarnings()

       val warningRepo: WarningRepository= WarningRepository()

       val myCoordinate: WarningRepository.Coordinate2 = WarningRepository.Coordinate2(lat = lat, lon = lon)

       for()
        

        warning.features.forEach { it ->

            val coordinates: List<Any> =  it.geometry.coordinates

            warningRepo.findClosestCoordinate(myCoordinate = myCoordinate, coordinates = coordinates)


            }

          println(it.geometry.coordinates)
          println(it::class.java.typeName)
          println(it.properties.description)



 }


fun containsNumber(list: List<Any>, number: Int): Boolean {
    for (element in list) {
        when (element) {
            is Int -> {
                if (element == number) {
                    return true
                }
            }
            is List<*> -> {
                if (containsNumber(element as List<Any>, number)) {

                }