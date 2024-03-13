package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class DateTime(
    val year: String,
    val initMonth: String,
    val intiDay: String,
    val intiHour: String
) {
    // Ensure day is two digits
    val day = if (intiDay.length == 1) "0$intiDay" else intiDay
    val hour = if (intiHour.length == 1) "0$intiHour" else intiHour
    val month = if (initMonth.length == 1) "0$initMonth" else initMonth

    // Use formattedDay for isoFormat and date
    val isoFormat: String = "$year-$month-${day}T${hour}:00:00Z"
    val date: String = "$day/$month/$year"
    val time: String = "$hour:00"

    val DateInt = LocalDate.of(year.toInt(), month.toInt(), intiDay.toInt())
    val dayOfWeek = DateInt.dayOfWeek.toString().lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }




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
}