package no.uio.ifi.in2000.andrklae.andrklae.team13

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.getLocations
import org.junit.Test

import org.junit.Assert.*

class LocationsTest {

    @Test
    fun testKnownLocationsNotEmpty(){
        // arrange
        val name = "Oslo"

        // act
        val list = getLocations(name)

        //assert
        assertTrue(list.isNotEmpty())
        println(list)
    }

    @Test
    fun testKnownLocationsNotFullString(){
        // arrange
        val name = "O"

        // act
        val list = getLocations(name)

        //assert
        assertTrue(list.first().name == "Oslo")
        println(list)
    }
    @Test
    fun testKnownLocationsCaseSensitive(){
        // arrange
        val name = "oSlO"

        // act
        val list = getLocations(name)

        //assert
        assertTrue(list.first().name == "Oslo")
        println(list)
    }
    @Test
    fun testKnownLocationsMultipleResults(){
        // arrange
        val name = "s"

        // act
        val list = getLocations(name)

        //assert
        assertTrue(list.size > 1)
        println(list)
    }

    @Test
    fun testNoResults() {
        // arrange
        val name = "Barcelona"

        // act
        val list = getLocations(name)

        //assert
        assertTrue(list.isEmpty())
    }
}