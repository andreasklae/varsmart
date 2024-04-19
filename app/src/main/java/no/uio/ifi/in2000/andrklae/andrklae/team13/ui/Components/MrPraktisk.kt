package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import no.uio.ifi.in2000.andrklae.andrklae.team13.R


@Composable
fun Animation(generateText: () -> Unit, animationType: Int) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(animationType)
    )
    Box(
        modifier = Modifier
            .clickable { generateText() }
            .clip(CircleShape),
        contentAlignment = Alignment.TopEnd
    ) {
        LottieAnimation(
            composition = composition,
            iterations = Int.MAX_VALUE,
            modifier = Modifier.size(120.dp)
        )

    }
}

@Composable
fun Blink(generateText: () -> Unit) {
    Animation(generateText = { generateText() }, animationType = R.raw.blink)
}

@Composable
fun Speak(generateText: () -> Unit) {
    Animation(generateText = { generateText() }, animationType = R.raw.speak)
}
