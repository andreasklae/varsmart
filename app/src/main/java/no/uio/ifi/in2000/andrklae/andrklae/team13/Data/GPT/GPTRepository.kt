package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT

import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.WeatherTimeForecast
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Alert

class GPTRepository() : GPTRepositoryInterface {
    // dummy response
    override val dummyResponse = "dummy response: Lorem ipsum dolor sit amet " +
            "consectetur adipisicing elit. Maxime mollitia, " +
            "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum "

    override val dataSource = GPTDataSource()

    // fetches gpt text for the current weather

    override suspend fun fetchCurrent(
        weather: WeatherTimeForecast,
        next24: List<WeatherTimeForecast>,
        age: Int,
        hobbyList: List<String>,
        alerts: List<Alert>
    ): String {
        var prompt = (
                "gi relevante tips basert på været, ca. 40 ord, maks 50 ord, 1 avsnitt, utf-8. " +
                        "Du er kortfattet, " +
                        "jovial, uformell, " +
                        "enkel å forstå, " +
                        "unngår komplisert data i ditt svar. " +

                        "Skriv som om jeg er $age år" +

                        "Værdata: " +

                        "sted: ${weather.customLocation.name} " +
                        "tid: nå{" +
                        "temp: ${weather.temperature}C " +
                        "skydekke: ${weather.cloudCoverage} " +
                        "regn: ${weather.precipitation}mm " +
                        "vind: ${weather.windSpeed}m/s }"
                )
        // adds forecast for the next 10 hours to the prompt
        next24.take(10).forEach {
            prompt += ("tid: ${it.time.isoFormat}{" +
                    "temp: ${it.temperature}C " +
                    "skydekke: ${it.cloudCoverage} " +
                    "regn: ${it.precipitation}mm " +
                    "vind: ${it.windSpeed}m/s }")
        }

        // adds alerts to the prompt
        if (alerts.isNotEmpty()){
            prompt += "farevarsler: "
            alerts.forEach {
                prompt += it.alert.properties.area + ": " + it.distance + "km unna. {" +
                        it.alert.properties.awarenessSeriousness + ". " +
                        it.alert.properties.eventAwarenessName + ". " +
                        it.alert.properties.consequences + ". " +
                        it.alert.properties.description + ". " +
                        it.alert.properties.instruction +
                        "}"
            }
        }

        // adds the users hobbies to the prompt
        if (hobbyList.isNotEmpty()){
            prompt += "ta hensyn til hobbyene mine, men kun om det er relevant" +
                    "ta hensyn til hvilken årstid det er f.eks. ikke foreslå en skitur på sommeren"+
                    "hobbyer: ${hobbyList.toString()}"
        }

        return dataSource.getGPTResponse(prompt)
        //delay(2000)
        //return dummyResponse
    }

    // fetches gpt text summarizing the weather the next 24 hours
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

    // fetches gpt text summarizing the weather the next week
    override suspend fun fetchWeek(
        week: List<WeatherTimeForecast>,
        age: Int,
        hobbyList: List<String>
    ): String {
        var prompt = "På maks 50 ord, oppsumer været den kommende uken" +
                "Du er jovial,uformell,enkel å forstå" +
                "Kun utf-8" +
                "skriv som om jeg er $age år"

        // adds the users hobbies to the prompt
        if (hobbyList.isNotEmpty()){
            prompt += "gi tips ang. hobby(ene) mine. hvilke(n) dag(er) det passer best å drive med dem?" +
                    "ta hensyn til hvilken årstid det er, f.eks. ikke foreslå en skitur i juni"+
                    "hobbyer: ${hobbyList.toString()}"
        }

        prompt += "vær:"
        week.forEach {
            prompt += ("${it.time.dayOfWeek}{" +
                    "temp: ${it.temperature}C " +
                    "skydekke: ${it.cloudCoverage} " +
                    "regn: ${it.precipitation}mm " +
                    "vind: ${it.windSpeed}m/s }")
        }
        return dataSource.getGPTResponse(prompt)
    }
}