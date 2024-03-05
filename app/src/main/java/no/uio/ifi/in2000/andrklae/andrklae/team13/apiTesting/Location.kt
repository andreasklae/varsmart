package no.uio.ifi.in2000.andrklae.andrklae.team13.apiTesting

// a data class describing a location.
data class Location (
    val name: String,
    val lon: Double,
    val lat: Double
){
    override fun toString(): String {
        return name
    }
}