package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata
class LocationRepository{
    suspend fun getLocations(search: String): List<CustomLocation>{
        val knownLocations = KnownLocations()

        println("Searching known cities")
        val knownCities = knownLocations.returnFiltered(search)

        // Returns a list of known cities if it is not empty
        return knownCities.ifEmpty {
            println("Didn't find any known cities, searching APIs")
            // API-call when known cities is empty

            val dataSource = LocationDataSource()
            // Replacing empty space with %20 for dataSource URL
            val newString = search
                .lowercase()
                .replace(" ", "%20")
                .replace("æ","%C3%A6")
                .replace("ø","%C3%B8")
                .replace("å","%C3%A5")

            dataSource.fetchAddresses(newString)
        }

    }
}
