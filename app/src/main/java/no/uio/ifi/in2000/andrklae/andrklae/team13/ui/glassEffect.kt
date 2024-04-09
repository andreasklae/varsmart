package no.uio.ifi.in2000.andrklae.andrklae.team13.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.glassEffect(): Modifier {
    return this
        .clip(RoundedCornerShape(15.dp))
        .background(Color.White.copy(alpha = 0.3f))
        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(15.dp))
        .border(2.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(15.dp))
        .border(3.dp, Color.White.copy(alpha = 0.02f), RoundedCornerShape(15.dp))
        .border(4.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(15.dp))
}