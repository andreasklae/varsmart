package no.uio.ifi.in2000.andrklae.andrklae.team13

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import org.junit.Test

class DateTimeUnitTest {


    // 5 unit tests that compares date time objects to see if on is before the other
    @Test
    fun compareYear(){
        val date1 = DateTime(
            year = "2024",
            initMonth = "3",
            initDay = "3",
            initHour = "12",
            initMinute = "00"
        )
        val date2 = DateTime(
            year = "2025",
            initMonth = "3",
            initDay = "3",
            initHour = "12",
            initMinute = "00"
        )

        assert(date2 > date1)
    }
    @Test
    fun compareMonth(){
        val date1 = DateTime(
            year = "2024",
            initMonth = "3",
            initDay = "3",
            initHour = "12",
            initMinute = "00"
        )
        val date2 = DateTime(
            year = "2024",
            initMonth = "4",
            initDay = "3",
            initHour = "12",
            initMinute = "00"
        )

        assert(date2 > date1)
    }

    @Test
    fun compareDay(){
        val date1 = DateTime(
            year = "2024",
            initMonth = "3",
            initDay = "3",
            initHour = "12",
            initMinute = "00"
        )
        val date2 = DateTime(
            year = "2024",
            initMonth = "3",
            initDay = "5",
            initHour = "12",
            initMinute = "00"
        )

        assert(date2 > date1)
    }
    @Test
    fun compareHour(){
        val date1 = DateTime(
            year = "2024",
            initMonth = "3",
            initDay = "3",
            initHour = "12",
            initMinute = "00"
        )
        val date2 = DateTime(
            year = "2024",
            initMonth = "3",
            initDay = "3",
            initHour = "14",
            initMinute = "00"
        )

        assert(date2 > date1)
    }
    @Test
    fun compareMinute(){
        val date1 = DateTime(
            year = "2024",
            initMonth = "3",
            initDay = "3",
            initHour = "12",
            initMinute = "00"
        )
        val date2 = DateTime(
            year = "2024",
            initMonth = "3",
            initDay = "3",
            initHour = "12",
            initMinute = "15"
        )

        assert(date2 > date1)
    }

    @Test
    fun compareAll(){
        val date1 = DateTime(
            year = "2024",
            initMonth = "3",
            initDay = "3",
            initHour = "12",
            initMinute = "00"
        )
        val date2 = DateTime(
            year = "2025",
            initMonth = "4",
            initDay = "4",
            initHour = "13",
            initMinute = "15"
        )

        assert(date2 > date1)
    }
}