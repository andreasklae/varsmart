package no.uio.ifi.in2000.andrklae.andrklae.team13

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.Location
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val location = Location("",0.0,0.1)

        assertTrue(location.name == "")

    }

}