package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.Locationdata

import kotlinx.serialization.Serializable



data class CustomLocation (
    val name: String,
    val lat: Double,
    val lon: Double,
    val type: String, // By / Bydel / fylke osv.
    val fylke: String
) {
    override fun toString(): String {
        return "Location = $name \n" +
                "Lat = $lat \n" +
                "Lon = $lon \n" +
                "Type = $type \n" +
                "Fylke = $fylke"
    }


}





@Serializable
data class ApiResponse(
    val metadata: Metadata,
    val navn: List<Navn>
)

@Serializable
data class Metadata(
    val side: Int,
    val sokeStreng: String,
    val totaltAntallTreff: Int,
    val treffPerSide: Int,
    val viserFra: Int,
    val viserTil: Int
)

@Serializable
data class Navn(
    val fylker: List<Fylke>,
    val kommuner: List<Kommune>,
    val navneobjekttype: String,
    val navnestatus: String,
    val representasjonspunkt: Representasjonspunkt,
    val skrivemåte: String,
    val skrivemåtestatus: String,
    val språk: String,
    val stedsnummer: Long,
    val stedstatus: String
)

@Serializable
data class Fylke(
    val fylkesnavn: String,
    val fylkesnummer: String
)

@Serializable
data class Kommune(
    val kommunenavn: String,
    val kommunenummer: String
)

@Serializable
data class Representasjonspunkt(
    val koordsys: Int,
    val nord: Double,
    val øst: Double
)