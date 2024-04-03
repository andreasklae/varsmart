package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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


    Box(
        modifier= Modifier,
        contentAlignment = Alignment.TopEnd) {
        LottieAnimation(
            composition = composition,
            iterations = 20,
            modifier = Modifier.size(120.dp)


        )


    }
}










@Composable
fun SpeechBubble(text: String, modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopEnd
    ) {
        Surface(
            modifier = Modifier.padding(top = 40.dp, end = 40.dp),
            shape = SpeechBubblen(),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 16.dp
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}