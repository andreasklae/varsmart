package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata

import okhttp3.internal.threadName

class KnownLocations{
    val locations = listOf(
        CustomLocation(
            name = "Oslo",
            lon= 59.91,
            lat= 10.75,
            fylke = "Oslo",
            type = "By"
        )


    )

    fun returnFiltered(search: String): List<CustomLocation>{
        return locations.filter { it.name.lowercase() == search.lowercase() }
    }
}
