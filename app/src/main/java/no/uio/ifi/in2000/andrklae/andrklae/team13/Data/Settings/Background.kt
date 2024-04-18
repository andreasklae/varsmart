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
                R.drawable.space,
                listOf(
                    Color.White,
                    Color(0xFF9FD8F2)
                )
            ),
            Background(
                R.drawable.flowers,
                listOf(
                    Color.White,
                    Color(0xFF90EE90)
                )
            )

        )
    }
}