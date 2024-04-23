package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata

class LocationRepository() : LocationRepositoryInterface {
    override suspend fun getLocations(search: String): List<CustomLocation> {
        println("Searching for $search")
        // API-call when known cities is empty
        val dataSource = LocationDataSource()
        // Replacing empty space with %20 for dataSource URL
        val newString = search
            .lowercase()
            .replace(" ", "%20")
            .replace("æ", "%C3%A6")
            .replace("ø", "%C3%B8")
            .replace("å", "%C3%A5")

        return dataSource.fetchAddresses(newString)
    }

    override suspend fun coordsToCity(lat: Double, lon: Double): CustomLocation? {
        val dataSource = LocationDataSource()
        val data = dataSource.reverseGeocoding(lat, lon)

        if (!(data == null)) {
            return data
        } else {
            return CustomLocation("Min posisjon", lat, lon, "", "")
        }
    }
}