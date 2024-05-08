package no.uio.ifi.in2000.andrklae.andrklae.team13

import com.google.android.gms.maps.model.LatLng
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Polygon
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.WarningRepository
import org.junit.Test
import kotlin.math.roundToInt

class WarningUnitTest{
    val repo = WarningRepository()

    // sees if distance is 0 when the coordinates are the same
    @Test
    fun testDistanceIs0(){
        val coords1 = LatLng(10.0, 10.0)
        val coords2 = LatLng(10.0, 10.0)

        val distance = repo.calculateDistance(coords1, coords2)

        assert(distance == 0.0)

    }

    // calculates distance between two coordinates far away
    @Test
    fun testDistanceFar(){
        val coords1 = LatLng(30.5, 10.3)
        val coords2 = LatLng(20.3, 4.7)

        val distance = repo.calculateDistance(coords1, coords2)
        val rounded = (distance * 10).roundToInt().toDouble() / 10

        // the distance should be 1265.4 km
        assert(rounded == 1265.4)

    }
    // calculates distance between two close coordinates
    @Test
    fun testDistanceClose(){
        val coords1 = LatLng(23.5, 7.4)
        val coords2 = LatLng(23.6, 7.3)

        val distance = repo.calculateDistance(coords1, coords2)
        val rounded = (distance * 10).roundToInt().toDouble() / 10

        println(distance)

        // the distance should be 15.1 km
        assert(rounded == 15.1)

    }

    // checks if a coordinate is inside a polygon
    @Test
    fun isInPolygon(){

        // coordinate of the Royal castle
        val castle = LatLng(59.9167, 10.7270)

        // polygon of Oslo
        val oslo = listOf(
            LatLng(59.9714, 10.8359),  // North-West point
            LatLng(59.9390, 10.5906),  // West point near Bygdøy
            LatLng(59.8782, 10.7108),  // South-West near Lysaker
            LatLng(59.8102, 10.8148),  // Southern point near Nordstrand
            LatLng(59.8928, 10.9390),  // South-East near Ellingsrud
            LatLng(59.9505, 10.9181),  // North-East near Grorud
            LatLng(59.9714, 10.8359)   // Closing the polygon to North-West point
        )

        val castleIsInOslo = repo.isPointInsidePolygon(oslo, castle)

        // The royal castle should be in oslo
        assert(castleIsInOslo)
    }

    // checks if a coordinate is outside a polygon
    @Test
    fun isNotInPolygon(){

        // coordinate of Galdhøpiggen
        val galdhopiggen = LatLng(61.636475, 8.312439)

        // polygon of Trondheim
        val trondheim = listOf(
            LatLng(63.446827, 10.421906),  // North-West near Trolla
            LatLng(63.430485, 10.525690),  // North-East near Lade
            LatLng(63.395500, 10.470500),  // East near Ranheim
            LatLng(63.360992, 10.358083),  // South-East near Bratsberg
            LatLng(63.375519, 10.258020),  // South-West near Byåsen
            LatLng(63.408789, 10.333414)   // West near Heimdal
        )

        val galdInTrond = repo.isPointInsidePolygon(trondheim, galdhopiggen)

        // Galdhøpiggen should not be in Trondheim
        assert(!galdInTrond)

    }
}
