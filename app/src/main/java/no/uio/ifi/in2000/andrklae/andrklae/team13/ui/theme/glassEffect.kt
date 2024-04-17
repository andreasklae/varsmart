package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.glassEffect(color: Color = Color.White): Modifier {
    return this
        .clip(RoundedCornerShape(15.dp))
        .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(15.dp))
        .border(2.dp, color.copy(alpha = 0.05f), RoundedCornerShape(15.dp))
        .border(3.dp, color.copy(alpha = 0.02f), RoundedCornerShape(15.dp))
        .border(4.dp, color.copy(alpha = 0.03f), RoundedCornerShape(15.dp))
        .background(brush = Brush.verticalGradient(
                colors = listOf(
                    color.copy(alpha = 0.7f),
                    color.copy(alpha = 0.4f),
                    color.copy(alpha = 0.7f)
                ),
            )
        )
        .background(brush = Brush.horizontalGradient(
                colors = listOf(
                    color.copy(alpha = 0.7f),
                    color.copy(alpha = 0.4f),
                    color.copy(alpha = 0.7f)
                ),
            )
        )


}

fun Modifier.coloredGlassEffect(color: Color = Color.White): Modifier {
    return this
        .clip(RoundedCornerShape(15.dp))
        .border(1.dp, Color.Black.copy(alpha = 0.3f), RoundedCornerShape(15.dp))
        .border(2.dp, Color.Black.copy(alpha = 0.05f), RoundedCornerShape(15.dp))
        .border(3.dp, Color.Black.copy(alpha = 0.02f), RoundedCornerShape(15.dp))
        .border(4.dp, Color.Black.copy(alpha = 0.03f), RoundedCornerShape(15.dp))
        .background(brush = Brush.verticalGradient(
                colors = listOf(
                    color.copy(alpha = 0.7f),
                    color.copy(alpha = 0.4f),
                    color.copy(alpha = 0.7f)
                ),
            )
        )
        .background(brush = Brush.horizontalGradient(
                colors = listOf(
                    color.copy(alpha = 0.7f),
                    color.copy(alpha = 0.4f),
                    color.copy(alpha = 0.7f)
                ),
            )
        )


}