package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings

import androidx.compose.ui.graphics.Color
import no.uio.ifi.in2000.andrklae.andrklae.team13.R

data class Background(
    val imageId: Int,
    val gradientList: List<Color>
) {
    companion object {
        val images = listOf(
            Background(
                R.drawable.sky,
                listOf(
                    Color.White,
                    Color(0xFF9FD8F2)
                )
            ),
            Background(
                R.drawable.flowers,
                listOf(
                    Color.White,
                    Color(0xFFB9C8A3)
                )
            ),
            Background(
                R.drawable.glitterbackground,
                listOf(
                    Color.White,
                    Color(0xFFFDDEFF)
                )
            ),
            Background(
                R.drawable.neutral,
                listOf(
                    Color.White,
                    Color(0xFFCCDADE)
                )
            )

        )
    }
}