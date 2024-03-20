package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather

enum class WeatherSymbol {
    clearsky,
    cloudy,
    fair,
    fog,
    heavyrain,
    heavyrainandthunder,
    heavyrainshowers,
    heavyrainshowersandthunder,
    heavysleet,
    heavysleetandthunder,
    heavysleetshowers,
    heavysleetshowersandthunder,
    heavysnow,
    heavysnowandthunder,
    heavysnowshowers,
    heavysnowshowersandthunder,
    lightrain,
    lightrainandthunder,
    lightrainshowers,
    lightrainshowersandthunder,
    lightsleet,
    lightsleetandthunder,
    lightsleetshowers,
    lightsnow,
    lightsnowandthunder,
    lightsnowshowers,
    lightssleetshowersandthunder,
    lightssnowshowersandthunder,
    partlycloudy,
    rain,
    rainandthunder,
    rainshowers,
    rainshowersandthunder,
    sleet,
    sleetandthunder,
    sleetshowers,
    sleetshowersandthunder,
    snow,
    snowandthunder,
    snowshowers,
    snowshowersandthunder;

    override fun toString(): String {
        return name
    }
}
