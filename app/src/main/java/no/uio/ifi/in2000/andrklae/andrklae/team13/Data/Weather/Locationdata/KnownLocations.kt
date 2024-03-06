package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata

import okhttp3.internal.threadName

class KnownLocations{
    val locations = listOf(
        Location(
            name = "Oslo",
            lon= 59.91,
            lat= 10.75

        ),
        Location(
            name = "Bergen",
            lon = 60.39,
            lat = 5.32),
        Location(
            name = "Stavanger",
            lon = 58.97,
            lat = 5.73),
        Location(
            name = "Trondheim",
            lon = 63.43,
            lat = 10.39),
        Location(
            name = "Drammen",
            lon = 59.74,
            lat = 10.20),
        Location(
            name = "Fredrikstad",
            lon = 59.22,
            lat = 10.93),
        Location(
            name = "Kristiansand",
            lon = 58.14,
            lat = 7.99),
        Location(
            name = "Sandnes",
            lon = 58.85,
            lat = 5.74),
        Location(
            name = "Tromsø",
            lon = 69.65,
            lat = 18.96),
        Location(
            name = "Sarpsborg",
            lon = 59.28,
            lat = 11.11)

    )

    fun returnFiltered(search: String): List<Location>{
        return locations.filter { it.name.lowercase() == search.lowercase() }
    }
}
