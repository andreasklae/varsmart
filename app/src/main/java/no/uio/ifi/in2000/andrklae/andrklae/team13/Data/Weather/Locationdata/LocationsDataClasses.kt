package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata

import kotlinx.serialization.Serializable



data class CustomLocation (
    val name: String,
    val lat: Double,
    val lon: Double,
    val postSted: String,
    val fylke: String
) {
    override fun toString(): String {
        return "Location = $name \n" +
                "Lat = $lat \n" +
                "Lon = $lon \n" +
                "PostSted = $postSted\n"+
                "fylke = $fylke"

    }


}

@Serializable
data class Root(
    val results: List<Result>,
    val status: String
)

@Serializable
data class Result(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String,
    val types: List<String>
)

@Serializable
data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)

@Serializable
data class Geometry(
    val location: Location,
)

@Serializable
data class Bounds(
    val northeast: Location,
    val southwest: Location
)

@Serializable
data class Location(
    val lat: Double,
    val lng: Double
)
