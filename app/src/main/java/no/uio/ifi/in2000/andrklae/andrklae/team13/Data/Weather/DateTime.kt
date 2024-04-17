package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class DateTime(
    val year: String,
    val initMonth: String,
    val intiDay: String,
    val intiHour: String,
    val initMinute: String = "00"
) {


    // Ensure each value are two digits
    val day = if (intiDay.length == 1) "0$intiDay" else intiDay
    val hour = if (intiHour.length == 1) "0$intiHour" else intiHour
    val month = if (initMonth.length == 1) "0$initMonth" else initMonth
    val minute = if (initMinute.length == 1) "0$initMinute" else initMinute

    // Use formattedDay for isoFormat and date
    val isoFormat: String = "$year-$month-${day}T${hour}:00:00Z"
    val date: String = "$day/$month/$year"
    val time: String = "$hour:00"

    val DateInt = LocalDate.of(year.toInt(), month.toInt(), intiDay.toInt())
    val dayOfWeek = dayToNorwegian(DateInt)

    private fun dayToNorwegian(dateInt: LocalDate?): String {
        val day = DateInt.dayOfWeek
            .toString().
            lowercase()
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()

            }
        return when (day) {
            "Monday" -> "Mandag"
            "Tuesday" -> "Tirsdag"
            "Wednesday" -> "Onsdag"
            "Thursday" -> "Torsdag"
            "Friday" -> "Fredag"
            "Saturday" -> "Lørdag"
            "Sunday" -> "Søndag"
            else -> "Invalid day"
        }
    }


    override fun toString(): String {
        return date + " " + time
    }

    fun getNextDay(dateTime: DateTime): DateTime {
        val current = LocalDate.parse("${dateTime.year}-${dateTime.month}-${dateTime.day}", DateTimeFormatter.ISO_DATE)
        val nextDay = current.plusDays(1)

        return DateTime(
            year = nextDay.year.toString(),
            initMonth = nextDay.monthValue.toString(),
            intiDay = nextDay.dayOfMonth.toString(),
            intiHour = "12"
        )
    }

    operator fun compareTo(dt: DateTime): Int {
        if (this.year != dt.year) {
            return this.year.toInt() - dt.year.toInt()
        }
        if (this.month != dt.month) {
            return this.month.toInt() - dt.month.toInt()
        }
        if (this.day != dt.day) {
            return this.day.toInt() - dt.day.toInt()
        }
        if (this.hour != dt.hour) {
            return this.hour.toInt() - dt.hour.toInt()
        }
        return 0
    }
}