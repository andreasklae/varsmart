package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT

import com.aallam.openai.api.BetaOpenAI
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast

class GPTRepoImpl() : GPTRepo {
    override val dummyResponse = "dummy response: Lorem ipsum dolor sit amet " +
            "consectetur adipisicing elit. Maxime mollitia, " +
            "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum "
    override val dataSource = GPTDataSource()

    @OptIn(BetaOpenAI::class)
    override suspend fun fetchCurrent(
        weather: WeatherTimeForecast,
        next24: List<WeatherTimeForecast>,
        age: Int,
        hobbyList: List<String>
    ): String {
        var prompt = (
                "gi relevante råd basert på været,maks 40 ord,ett avsnitt. " +
                        "Du er kortfattet,jovial,uformell,enkel å forstå. " +
                        "ta hensyn til hobbyene mine dersom det er relevant" +
                        "Skriv som om jeg er $age år" +
                        "hobbyer: ${hobbyList.toString()}" +
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
        return dataSource.getGPTResponse(prompt)
        //delay(2000)
        //return dummyResponse

    }

    override suspend fun fetch24h(next24h: List<WeatherTimeForecast>, age: Int): String {
        var prompt = "på maks 30 ord, fortell hvordan været utvikler seg i løpet av døgnet." +
                "Du er jovial,uformell,enkel å forstå." +
                "kun utf-8 karakterer" +
                "Skriv som om jeg er $age år. " +
                "Værdata: "
        next24h.forEach {
            prompt += ("tid: ${it.time.isoFormat}{" +
                    "temp: ${it.temperature}C " +
                    "skydekke: ${it.cloudCoverage} " +
                    "regn: ${it.precipitation}mm " +
                    "vind: ${it.windSpeed}m/s }")
        }
        return dataSource.getGPTResponse(prompt)
        //delay(2000)
        //return dummyResponse
    }
}