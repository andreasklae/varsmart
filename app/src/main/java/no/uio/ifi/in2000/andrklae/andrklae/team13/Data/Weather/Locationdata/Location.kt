package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata

// a data class describing a location.
data class Location (
    val name: String,
    val lon: Double,
    val lat: Double
){
    override fun toString(): String {
        return "\n" +
                "City = $name \n" +
                "Lat = $lat \n" +
                "Lon = $lon"
    }
}