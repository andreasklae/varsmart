package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

import kotlin.reflect.jvm.internal.impl.util.EmptyArrayMap


data class Geometry(
    val coordinates: List<ArrayList<*>>,
    val type: String
)

data class Properties(
    val altitude_above_sea_level: Int,
    val area: String, //
    val awarenessResponse: String,
    val awarenessSeriousness: String,
    val awareness_level: String,
    val awareness_type: String,
    val ceiling_above_sea_level: Int,
    val certainty: String,
    val consequences: String,
    val contact: String,
    val county: List<String?>,
    val description: String, //
    val event: String,
    val eventAwarenessName: String,
    val eventEndingTime: String? = null,
    val geographicDomain: String,
    val id: String,
    val instruction: String, //
    val municipality: List<String>,
    val resources: List<Resource>,
    val riskMatrixColor: String, //
    val severity: String, //
    val status: String,
    val title: String, //
    val triggerLevel: String,
    val type: String,
    val web: String
) {
    // Function that can collect the area name as ocean-type to give the user more context
    // as to what areas like "A5" represent.
    fun thing(string: String): String {
        if (string.length == 2 && (string[1].isDigit() || string[0].isDigit())) {
            return "Havområde: " + string
        } else {
            return string
        }
    }
}

data class Resource(
    val description: String,
    val mimeType: String,
    val uri: String
)


data class When(
    val interval: List<String>
)


data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String,
    val `when`: When
)


data class AlertResponse(
    val features: List<Feature>,
    val lang: String,
    val lastChange: String,
    val type: String
)