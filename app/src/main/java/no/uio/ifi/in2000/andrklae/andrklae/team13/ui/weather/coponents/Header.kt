package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect

// template for headers
@Composable
fun Header(header: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .glassEffect()
                .padding(10.dp)
        ) {
            Text(
                text = header,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }

}