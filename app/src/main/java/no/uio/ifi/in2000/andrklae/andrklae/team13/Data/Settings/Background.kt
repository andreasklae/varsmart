package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings

import androidx.compose.ui.graphics.Color
import no.uio.ifi.in2000.andrklae.andrklae.team13.R

// background images
data class Background(
    val imageId: Int,
    val gradientList: List<Color>,
    val name: String // for content description for screen readers
) {

    companion object {
        // a list of the background images and their corresponding gradient
        val images = listOf(
            Background(
                R.drawable.sky,
                listOf(
                    Color.White,
                    Color(0xFF9FD8F2)
                ),
                "Himmel"
            ),
            Background(
                R.drawable.flowers,
                listOf(
                    Color.White,
                    Color(0xFFB9C8A3)
                ),
                "Blomster"
            ),
            Background(
                R.drawable.glitterbackground,
                listOf(
                    Color.White,
                    Color(0xFFFDDEFF)
                ),
                "Glitter"
            ),
            Background(
                R.drawable.neutral,
                listOf(
                    Color.White,
                    Color(0xFFCCDADE)
                ),
                "nøytral"
            )

        )
    }
}