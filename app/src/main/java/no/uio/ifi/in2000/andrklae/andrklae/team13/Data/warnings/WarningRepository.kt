package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata.CustomLocation
import com.mapbox.geojson.*
import java.lang.Math.pow
import kotlin.math.sqrt

class WarningRepository {

    var warningRange = 40
    private val warningDataSource: WarningDataSource= WarningDataSource()
    // TO-DO
    // Initialize variable with List<Features>
    suspend fun fetchAllWarnings(): Warning {
        return warningDataSource.fetchAllWarnings()
    }


    data class Coordinate(val latitude: Double, val longitude: Double)

    class CustomLocation(var lat: Double, var lon: Double)

    private fun calculateDistance(a: Coordinate, b: Coordinate): Double {
        val earthRadiusInMeters = 6378137f
        val deltaLatitudeRadians = Math.radians(b.latitude - a.latitude).toFloat()
        val deltaLongitudeRadians = Math.radians(b.longitude - a.longitude).toFloat()

        val sinDeltaLatitudeHalf = Math.sin(deltaLatitudeRadians / 2).toFloat()
        val sinDeltaLongitudeHalf = Math.cos(a.latitude * Math.PI / 180).let { cosA ->
            Math.sin((b.latitude - a.latitude) * Math.PI / 180 / 2).times(-1).divideBy(cosA)
        }

        return sqrt(pow(sinDeltaLatitudeHalf, 2) + pow(sinDeltaLongitudeHalf, 2)) * earthRadiusInMeters
    }

    private const val warningRange = 10000.0 // Assuming some default value or configuration

    fun findClosestCoordinate(loc: CustomLocation, coordinates: List<Feature>): Feature? {
        val myCoordinate = Coordinate(loc.lat, loc.lon)
        var closestCoordinate: Coordinate? = null
        var minDistance = Double.POSITIVE_INFINITY

        coordinates.forEachIndexed { index, feature ->
            processGeometry(feature.geometry, myCoordinate)?.also { coordinateList ->
                coordinateList.forEach { coord ->
                    val currentDistance = calculateDistance(myCoordinate, coord)
                    if (currentDistance < minDistance) {
                        minDistance = currentDistance
                        closestCoordinate = coord
                    }
                }
            } ?: Unit
        }

        return if (closestCoordinate != null && calculateKm(myCoordinate.latitude, myCoordinate.longitude, closestCoordinate.latitude, closestCoordinate.longitude) <= warningRange) {
            features[index]
        } else {
            null
        }
    }

    private fun processGeometry(geom: Geometry?, origin: Coordinate): MutableList<Coordinate>? {
        when (geom?.type) {
            "Point" -> {
                geom as Point
                mutableListOf(Coordinate(geom.coordinate.latitude, geom.coordinate.longitude)).apply { addAll(processGeometry(geom.subtype, origin)) }
            }
            "MultiPolygon", "LineString" -> {
                geom as LineString
                mutableListOf(origin).apply { addAll(geom.positions.mapNotNull { pos ->
                    pos.firstOrNull()?.second?.takeIf { it > 0 }?.let { Coordinate(it, pos.lastOrNull() ?: 0.0) }
                }) }
                "Polygon" -> {
                    geom as Polygon
                    mutableListOf(origin).apply { addAll(geom.rings.flatten().filterIsInstance<Position>().asSequence().mapNotNull { pos ->
                        pos.firstOrNull()?.second?.takeIf { it > 0 }?.let { Coordinate(it, pos.lastOrNull() ?: 0.0) }
                    }) }
                }
                else -> null
            }
        }

}
