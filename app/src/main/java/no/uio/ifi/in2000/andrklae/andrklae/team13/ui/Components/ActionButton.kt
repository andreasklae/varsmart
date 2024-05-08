package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect

// making buttons look similar
@Composable
fun ActionButton(
    icon: ImageVector,
    onClick: () -> Unit
){
    Box(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .glassEffect()
            .padding(5.dp)
    ) {
        Icon(
            icon,
            "refresh",
            Modifier.size(40.dp)
        )
    }
}