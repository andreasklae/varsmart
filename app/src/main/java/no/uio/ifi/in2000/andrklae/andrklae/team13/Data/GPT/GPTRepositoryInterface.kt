package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT

import com.aallam.openai.api.BetaOpenAI
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast

interface GPTRepositoryInterface {
    val dummyResponse: String
    val dataSource: GPTDataSource

    @OptIn(BetaOpenAI::class)
    suspend fun fetchCurrent(
        weather: WeatherTimeForecast,
        next24: List<WeatherTimeForecast>,
        age: Int,
        hobbyList: List<String>
    ): String

    suspend fun fetchWeek(
        week: List<WeatherTimeForecast>,
        age: Int,
        hobbyList: List<String>
    ): String

    suspend fun fetch24h(next24h: List<WeatherTimeForecast>, age: Int): String

}
