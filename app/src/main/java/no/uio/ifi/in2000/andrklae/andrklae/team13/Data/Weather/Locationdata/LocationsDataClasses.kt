package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata

import kotlinx.serialization.Serializable



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





@Serializable
data class AddressResponse(
    val metadata: Metadata,
    val adresser: List<Address>
)

@Serializable
data class Metadata(
    val totaltAntallTreff: Int,
    val sokeStreng: String,
    val asciiKompatibel: Boolean,
    val treffPerSide: Int,
    val viserFra: Int,
    val side: Int,
    val viserTil: Int
)


 @Serializable
data class Address(
    val adressenavn: String,
    val adressetekst: String,
    val adressetilleggsnavn: String?,
    val adressekode: Int,
    val nummer: Int,
    val bokstav: String,
    val kommunenummer: String,
    val kommunenavn: String,
    val gardsnummer: Int,
    val bruksnummer: Int,
    val festenummer: Int,
    val undernummer: String?,
    val bruksenhetsnummer: List<String>,
    val objtype: String,
    val poststed: String,
    val postnummer: String,
    val adressetekstutenadressetilleggsnavn: String,
    val stedfestingverifisert: Boolean,
    val representasjonspunkt: Representasjonspunkt,
    val oppdateringsdato: String
)

@Serializable
data class Representasjonspunkt(
    val epsg: String,
    val lat: Double,
    val lon: Double
)

@Serializable
data class ApiResponse(
    val adresser: List<Address>
)

@Serializable
data class Coordinate(
    val lat: Double,
    val lon: Double
)