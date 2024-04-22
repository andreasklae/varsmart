package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata

interface LocationRepositoryInterface {
    suspend fun getLocations(search: String): List<CustomLocation>

    suspend fun coordsToCity(lat: Double, lon: Double): CustomLocation?

}
