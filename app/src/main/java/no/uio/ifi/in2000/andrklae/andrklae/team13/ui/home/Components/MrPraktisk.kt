package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MrPraktisk(){
    Icon(
        imageVector = Icons.Filled.Face,
        contentDescription = "Play",
        tint = Color.Black,
        modifier = Modifier
            .size(48.dp) // Keep the fixed size for the icon
        // To ensure no space between the icon and the Box, adjustments are made through the Row and Box.
    )
}