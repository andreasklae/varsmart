package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import no.uio.ifi.in2000.andrklae.andrklae.team13.R

class Animation {
}

@Composable
fun Chicken() {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.chicken)
    )

    // Test
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.TopEnd) {
        LottieAnimation(
            composition = composition,
            iterations = 1,
            modifier = Modifier.size(120.dp)
        )

    }
}