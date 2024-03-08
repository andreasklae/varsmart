package no.uio.ifi.in2000.andrklae.andrklae.team13

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.KnownLocations
import org.junit.Test

import org.junit.Assert.*

class KnownLocationsTest {
    private val knownLocations = KnownLocations()
    @Test
    fun testKnownLocationsNotEmpty(){
        // arrange
        val name = "Oslo"

        // act
        val searchResult = knownLocations.returnFiltered(name)

        //assert
        assertTrue(searchResult.isNotEmpty())
        println(searchResult)
    }

    @Test
    fun testKnownLocationsNotFullString(){
        // arrange
        val name = "O"

        // act
        val searchResult = knownLocations.returnFiltered(name)

        //assert
        assertTrue(searchResult.isEmpty())
    }
    @Test
    fun testKnownLocationsCaseSensitive(){
        // arrange
        val name = "oSlO"

        // act
        val searchResult = knownLocations.returnFiltered(name)

        //assert
        assertTrue(searchResult.isNotEmpty())
        println(searchResult.first().name)
    }


    @Test
    fun testNoResults() {
        // arrange
        val name = "Barcelona"

        // act
        val searchResult = knownLocations.returnFiltered(name)

        //assert
        assertTrue(searchResult.isEmpty())
    }
}