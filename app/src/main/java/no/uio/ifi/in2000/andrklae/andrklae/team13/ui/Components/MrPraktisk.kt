package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT.MrPraktiskAnimations
import no.uio.ifi.in2000.andrklae.andrklae.team13.R

@Composable
fun MrPraktisk(generateText: () -> Unit, animation: MrPraktiskAnimations) {
    val clipShape = GenericShape { size, _ ->
        // Start from the top left corner with an offset
        moveTo(size.width * 0.2f, size.height * 0.12f)
        // Draw to the top right corner with an offset
        lineTo(size.width * 0.85f, size.height * 0.12f)
        // Draw to the bottom right corner with an offset
        lineTo(size.width * 0.7f, size.height * 0.95f)
        // Draw to the bottom left corner with an offset
        lineTo(size.width * 0.3f, size.height * 0.95f)
        // Complete the shape
        close()
    }

    val blink by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.blink)
    )
    val speak by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.speak)
    )
    val think by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.blink)
    )

    val composition = {
        when (animation) {
            MrPraktiskAnimations.BLINK -> blink
            MrPraktiskAnimations.SPEAK -> speak
            MrPraktiskAnimations.THINKING -> think
        }
    }
    Box(
        modifier = Modifier
            .clickable {
                // only allow one prompt at a time
                if (
                    animation != MrPraktiskAnimations.SPEAK
                    &&
                    animation != MrPraktiskAnimations.THINKING
                ){
                    generateText()

                }
            }
    ) {
        LottieAnimation(
            composition = composition(),
            iterations = Int.MAX_VALUE,
            modifier = Modifier
                .size(120.dp)
                .clip(clipShape)
        )

    }
}

@Preview
@Composable
fun MrPraktiskPreview(){
    val clipShape = GenericShape { size, _ ->
        // Start from the top left corner with an offset
        moveTo(size.width * 0.2f, size.height * 0.12f)
        // Draw to the top right corner with an offset
        lineTo(size.width * 0.85f, size.height * 0.12f)
        // Draw to the bottom right corner with an offset
        lineTo(size.width * 0.7f, size.height * 0.95f)
        // Draw to the bottom left corner with an offset
        lineTo(size.width * 0.3f, size.height * 0.95f)
        // Complete the shape
        close()
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.blink)
    )
    LottieAnimation(
        composition = composition,
        iterations = Int.MAX_VALUE,
        modifier = Modifier
            .size(120.dp)
            .clip(clipShape)
            .background(Color.Red)
    )


}
