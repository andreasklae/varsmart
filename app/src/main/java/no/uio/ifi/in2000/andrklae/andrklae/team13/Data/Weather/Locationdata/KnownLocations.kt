package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata

class KnownLocations{
    val customLocations = listOf(
        CustomLocation(
            name = "Oslo",
            lat = 59.91,
            lon = 10.75,
            fylke = "Oslo",
            type = "By"
        )
    )

    fun returnFiltered(search: String): List<CustomLocation>{
        return customLocations.filter { it.name.lowercase() == search.lowercase() }
    }
}
