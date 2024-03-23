package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings

// Warning data class which takes in an alert response containing all weather warnings from MET
data class Warning(
    val weatherAlert: AlertResponse
) {
    // Variable which contains a list of all warnings
    val features: List<Feature> = weatherAlert.features

    // Printable toString method
    override fun toString(): String{
        return features.toString()
    }
}