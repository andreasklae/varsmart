package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata

interface LocationRepositoryInterface {
    // function for finding coordinates based on a location name
    suspend fun getLocations(search: String): List<CustomLocation>

    // function for finding the name of a location based on coordinates
    suspend fun coordsToCity(lat: Double, lon: Double): String

}
