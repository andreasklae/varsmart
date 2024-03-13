package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

//val homeVM = HomeViewModel()
@Preview(showSystemUi = true)
@Composable
fun HomeScreen(){
    Column {
        Text(text = "Oslo")
        HomeWidget()
    }

}
@Composable
fun HomeWidget() {
    Box(
        modifier = Modifier
            .background(color = Color(android.graphics.Color.GRAY), shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Text(text = "Test")
    }
}