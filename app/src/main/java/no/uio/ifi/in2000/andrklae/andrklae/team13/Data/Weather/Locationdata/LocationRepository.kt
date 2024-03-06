package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata
class LocationRepository{
    suspend fun getLocations(search: String): List<Location>{
        val knownLocations = KnownLocations()

        val knownCities = knownLocations.returnFiltered(search)
        // Returns a list of known cities if it is not empty
        println("Searching known cities")
        return knownCities.ifEmpty {
            // API-call when known cities is empty

            val dataSource = LocationDataSource()
            // Replacing empty space with %20 for dataSource URL
            val newString = search.replace(" ", "%20")
            println("Didn't find any known cities, searching APIs")
            dataSource.fetchAddresses(newString)

        }

    }
}
