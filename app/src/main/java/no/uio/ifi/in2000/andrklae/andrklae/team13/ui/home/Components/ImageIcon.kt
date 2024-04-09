package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ImageIcon(y: Int, x: Int, symbolId: Int, width: Int, height: Int){
    Box(
        modifier = Modifier
            .offset(y = (y).dp, x = x.dp)
        // adjust the value to suit your design
    ) {
        Image(
            painter = painterResource(id = symbolId),
            contentDescription = "Symbol",
            modifier = Modifier
                .width(width.dp)
                .height(height.dp)
        )
    }
}

