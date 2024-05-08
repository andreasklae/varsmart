package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Alert

interface GPTRepositoryInterface {
    val dummyResponse: String
    val dataSource: GPTDataSource

    // function for generating gpt text based on the current weather
    suspend fun fetchCurrent(
        weather: WeatherTimeForecast,
        next24: List<WeatherTimeForecast>,
        age: Int,
        hobbyList: List<String>,
        alerts: List<Alert>
    ): String

    // function for creating gpt text summarizing the weather the next week
    suspend fun fetchWeek(
        week: List<WeatherTimeForecast>,
        age: Int,
        hobbyList: List<String>
    ): String

    // function for creating gpt text summarizing the weather the next 24 hours
    suspend fun fetch24h(next24h: List<WeatherTimeForecast>, age: Int): String

}
