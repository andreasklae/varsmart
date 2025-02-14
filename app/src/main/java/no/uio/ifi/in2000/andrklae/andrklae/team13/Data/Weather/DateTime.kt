package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class DateTime(
    val year: String,
    val initMonth: String,
    val initDay: String,
    val initHour: String,
    val initMinute: String = "00"
) {


    // Ensure each value are two digits
    val day = if (initDay.length == 1) "0$initDay" else initDay
    val hour = if (initHour.length == 1) "0$initHour" else initHour
    val month = if (initMonth.length == 1) "0$initMonth" else initMonth
    val minute = if (initMinute.length == 1) "0$initMinute" else initMinute

    // Use formattedDay for isoFormat and date
    val isoFormat: String = "$year-$month-${day}T${hour}:00:00Z"
    val date: String = "$day/$month/$year"
    val time: String = "$hour:00"

    val DateInt = LocalDate.of(year.toInt(), month.toInt(), initDay.toInt())
    val dayOfWeek = dayToNorwegian(DateInt)

    private fun dayToNorwegian(dateInt: LocalDate): String {
        val day = dateInt.dayOfWeek
            .toString().lowercase()
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
        val current = LocalDate.parse(
            "${dateTime.year}-${dateTime.month}-${dateTime.day}",
            DateTimeFormatter.ISO_DATE
        )
        val nextDay = current.plusDays(1)

        return DateTime(
            year = nextDay.year.toString(),
            initMonth = nextDay.monthValue.toString(),
            initDay = nextDay.dayOfMonth.toString(),
            initHour = "12"
        )
    }

    // function for getting relatively accurate description of a time interval
    fun getInterval(dt: DateTime): String{

        // if its a year or more
        if (this.year != dt.year) {
            val interval = this.year.toInt() - dt.year.toInt()
            return interval.toString() + " år siden"
        }

        // if its less than a year
        if (this.month != dt.month) {
            val interval = this.month.toInt() - dt.month.toInt()

            if (interval > 1) return interval.toString() + " måneder siden"
            else return "Forrige måned"
        }

        // if its less than a month
        if (this.day.toInt() - dt.day.toInt() > 7){
            val weeks = ((this.day.toInt() - dt.day.toInt()).toFloat() / 7.0).toInt()
            if (weeks > 1) return "$weeks uker siden"
            else return "En uke siden"
        }

        // if its less than a week
        if (this.day != dt.day){
            return this.dayOfWeek
        }
        // if its less than a day
        else {
            return this.hour + ":" + this.minute
        }

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
        if (this.minute != dt.minute) {
            return this.minute.toInt() - dt.minute.toInt()
        }
        return 0
    }
}