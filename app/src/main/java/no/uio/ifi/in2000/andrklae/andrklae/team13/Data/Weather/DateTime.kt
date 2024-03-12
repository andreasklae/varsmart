package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

data class DateTime(
    val year: String,
    val month: String,
    val day: String,
    val hour: String,
    val isoFormat: String = "$year-$month-${day}T${hour}:00:00Z",

    val date: String = "$day/$month/$year",
    val time: String = "$hour:00"
)
{
    override fun toString(): String {
        return date + " " + time
    }
}