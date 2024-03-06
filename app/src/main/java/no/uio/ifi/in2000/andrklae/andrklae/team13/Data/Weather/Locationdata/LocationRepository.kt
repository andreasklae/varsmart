package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata

fun getLocations(search: String): List<Location>{

    var CityList: List<Location> = listOf()

    val knownCities = getKnownLocations().filter{ it.name.lowercase().startsWith(search.lowercase())}

    if (!knownCities.isEmpty()){
        return knownCities
    }
    else{
        //api kall
    }

    // if api kall and known cities er tomme
    return emptyList()


}