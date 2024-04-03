package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun DrawSymbol(symbol: String?, size: Dp, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory())
                .data("https://raw.githubusercontent.com/metno/weathericons/89e3173756248b4696b9b10677b66c4ef435db53/weather/svg/$symbol.svg")
                // Coil's .size() method is used to define a target size for image decoding, not the display size in Compose.
                // Since we're using Box's size to control the image size, this is not strictly necessary unless the SVG has a very large default size.
                .build(),
            // ContentScale.Fit will ensure the image scales properly within the bounds you've set without cropping.
            contentScale = ContentScale.Fit
        )
        Image(
            painter = painter,
            contentDescription = null,
            // Fill the max size of the parent Box, and adjust the image scaling with contentScale.
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Fit // This makes the image fit within the given dimensions, maintaining aspect ratio.
        )

    }
}