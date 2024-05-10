package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata

class LocationRepository() : LocationRepositoryInterface {
    // function for finding coordinates based on a location name
    override suspend fun getLocations(search: String): List<CustomLocation> {
        println("Searching for $search")
        val dataSource = LocationDataSource()
        // Replacing space and norwegian letters
        val newString = search
            .lowercase()
            .replace(" ", "%20")
            .replace("æ", "%C3%A6")
            .replace("ø", "%C3%B8")
            .replace("å", "%C3%A5")

        return dataSource.searchLocations(newString)
    }

    // function for finding the name of a location based on coordinates
    override suspend fun coordsToCity(lat: Double, lon: Double): String {
        val dataSource = LocationDataSource()
        val locName = dataSource.reverseGeocoding(lat, lon)

        return locName

    }
}