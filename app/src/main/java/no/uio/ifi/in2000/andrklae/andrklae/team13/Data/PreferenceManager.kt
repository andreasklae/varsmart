package no.uio.ifi.in2000.andrklae.andrklae.team13.Data

import android.content.Context
import androidx.compose.material3.SliderPositions
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings.SettingsViewModel

object PreferenceManager {

    // function to initialize onboarding
    fun startOnboarding(context: Context){
        context.getSharedPreferences("AppNamePrefs", Context.MODE_PRIVATE).edit().apply {
            putBoolean("onboardingCompleted", false)
            apply()
        }
    }

    // function to complete onboarding
    fun completeOnboarding(
        context: Context
    ){
        context.getSharedPreferences("AppNamePrefs", Context.MODE_PRIVATE).edit().apply {
            putBoolean("onboardingCompleted", true)
            apply()
        }
    }

    // function for fetching the status of the onboarding
    fun fetchOnboardingStatus(context: Context): Boolean{
        val status = context.getSharedPreferences(
            "AppNamePrefs",
            Context.MODE_PRIVATE
        )
            .getBoolean("onboardingCompleted", false)
        return status
    }

    // function to save the list of favourites
    fun saveFavourites (context: Context, favourites: List<DataHolder>){
        println("saving favourites")
        val formatedList = mutableListOf<String>()
        favourites.forEach {
            val location = it.location
            val name = location.name.toString()
            val fylke = location.fylke.toString()
            val postSted = location.postSted.toString()
            val lat = location.lat.toString()
            val lon = location.lon.toString()

            val locationString = "$name;$lat;$lon;$postSted;$fylke"
            formatedList.add(locationString)
        }
        val joinedString = formatedList.joinToString(",")
        context.getSharedPreferences("AppNamePrefs", Context.MODE_PRIVATE).edit().apply {
            putString("favourites", joinedString)
            apply()
        }
    }

    // function for fetching favourites
    fun fetchFavourites(context: Context): List<DataHolder>{
        println("fetching favourites")

        val favourites = mutableListOf<DataHolder>()
        val data = context.getSharedPreferences(
            "AppNamePrefs",
            Context.MODE_PRIVATE
        )
            .getString("favourites", "")

        val formatedList = data?.split(",") ?: emptyList()
        formatedList.forEach {
            val location = it.split(";")
            if (location.size > 1){
                favourites.add(
                    DataHolder(
                        CustomLocation(
                            location[0], // name
                            location[1].toDouble(), // lat
                            location[2].toDouble(), // lon
                            location[3], // postSted
                            location[4] // Fylke
                        )
                    )

                )
            }
        }
        return favourites
    }

    // function for saving age
    fun saveAge(context: Context, age: Int){
        println("Saving age")

        context.getSharedPreferences("AppNamePrefs", Context.MODE_PRIVATE).edit().apply {
            putInt("age", age)
            apply()
        }
    }

    // function for fetching age
    fun fetchAge(context: Context): Int {
        println("fetching Age")
        val age = context.getSharedPreferences(
            "AppNamePrefs",
            Context.MODE_PRIVATE
        )
            .getInt("age", 13)
        return age
    }

    // function for saving hobbies
    fun saveHobbies (context: Context, hobbies: List<String>){
        println("saving hobbies")

        val joinedString = hobbies.joinToString(",")
        context.getSharedPreferences("AppNamePrefs", Context.MODE_PRIVATE).edit().apply {
            putString("hobbies", joinedString)
            apply()
        }
    }

    // function for fetching hobbies
    fun fetchHobbies (context: Context): List<String>{
        println("fetching hobbies")

        val hobbies = context.getSharedPreferences(
            "AppNamePrefs",
            Context.MODE_PRIVATE
        )
            .getString("hobbies", null)

        return hobbies?.split(",") ?: emptyList()
    }

    // function for saving age
    fun saveBackgroundIndex(context: Context, index: Int){
        println("Saving background")

        context.getSharedPreferences("AppNamePrefs", Context.MODE_PRIVATE).edit().apply {
            putInt("bIndex", index)
            apply()
        }
    }

    // function for fetching age
    fun fetchBackgroundIndex(context: Context): Int {
        println("fetching background")
        val index = context.getSharedPreferences(
            "AppNamePrefs",
            Context.MODE_PRIVATE
        )
            .getInt("bIndex", 0)
        return index
    }
}