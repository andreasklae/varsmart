package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT.MrPraktiskAnimations
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.ImageIcon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.MrPraktisk


@Composable
fun GptSpeechBubble(
    content: String,
    function: () -> Unit,
    animation: MrPraktiskAnimations
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .heightIn(min = 70.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(Modifier.padding(15.dp)) {
                    Column(
                        modifier = Modifier
                    ) {
                        Text(
                            text = content,
                            fontSize = 15.sp,
                            lineHeight = 15.sp, // Adjusted for visual consistency
                            modifier = Modifier.fillMaxWidth()
                        )

                    }

                }
            }
            ImageIcon(y = 0, x = 0, symbolId = R.drawable.arrowright, width = 30, height = 60)
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 30.dp)
            ){
            MrPraktisk({ function() }, animation)

        }
    }
}
