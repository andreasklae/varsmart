package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT

import com.aallam.openai.api.BetaOpenAI
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast

class GPTRepo {
    val dataSource = GPTDataSource()
    @OptIn(BetaOpenAI::class)
    suspend fun fetchCurrent(weather: WeatherTimeForecast, next24: List<WeatherTimeForecast>): String? {
        var prompt = (
                "oppsumer været og gi relevante råd,maks 30 ord,ett avsnitt. " +
                        "Du er kortfattet,jovial,uformell,enkel å forstå. " +
                        "Skriv som om jeg er 9 år" +
                        "Værdata: " +

                        "sted: ${weather.customLocation.name} " +
                        "tid: nå{" +
                        "temp: ${weather.temperature}C " +
                        "skydekke: ${weather.cloudCoverage} " +
                        "regn: ${weather.precipitation}mm " +
                        "vind: ${weather.windSpeed}m/s }"
                )
        next24.take(10).forEach {
            prompt += ("tid: ${it.time.isoFormat}{" +
                    "temp: ${it.temperature}C " +
                    "skydekke: ${it.cloudCoverage} " +
                    "regn: ${it.precipitation}mm " +
                    "vind: ${it.windSpeed}m/s }")
        }
        try {
            return dataSource.getGPTResponse(prompt)
        }
        catch (e: Exception) {
            return null
        }
    }

    suspend fun fetchWeek(next24h: List<WeatherTimeForecast>): String {
        var prompt = "på maks 20 ord, fortell hvordan været utvikler seg i løpet av døgnet." +
                "Du er jovial,uformell,enkel å forstå."+
                "Skriv som om jeg er 9 år. " +
                "Værdata: "
        next24h.forEach {
            prompt += ("tid: ${it.time.isoFormat}{" +
                    "temp: ${it.temperature}C " +
                    "skydekke: ${it.cloudCoverage} " +
                    "regn: ${it.precipitation}mm " +
                    "vind: ${it.windSpeed}m/s }")
        }
        return dataSource.getGPTResponse(prompt)
    }

}
