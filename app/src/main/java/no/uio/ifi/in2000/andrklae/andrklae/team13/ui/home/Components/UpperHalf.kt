package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.HomeViewModel
@Composable
fun UpperHalf(homeVM: HomeViewModel){
    val loc by homeVM.loc.collectAsState()

    val wStatus by homeVM.wStatus.collectAsState()
    val weather by homeVM.currentWeather.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Image(
            painter = painterResource(id = R.drawable.flowers),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        Column() {
            Spacer(modifier = Modifier.padding(20.dp))

            // Header (location name)
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = loc.name, fontSize = 35.sp)
            }

            // contents
            Column {
                Spacer(modifier = Modifier.height(25.dp))

                // Temperature box
                Row {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .width(160.dp)
                            .height(134.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.7f)) // Semi-transparent background
                            .border(1.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(20.dp)),

                        contentAlignment = Alignment.Center

                    ) {
                        when(wStatus){
                            homeVM.statusStates[0] -> {
                                CircularProgressIndicator(
                                    color = Color.Black, // Sets the color of the spinner
                                    strokeWidth = 4.dp // Sets the stroke width of the spinner
                                )
                            }

                            homeVM.statusStates[1] -> {
                                Text(
                                    text = "${weather!!.temperature}°C",
                                    fontSize = 35.sp,
                                )
                            }

                            homeVM.statusStates[2] -> {

                            }
                        }


                    }

                    // Gpt respons
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.7f)) // Semi-transparent background
                            .border(1.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(20.dp)),

                        contentAlignment = Alignment.Center

                    ) {
                        when(wStatus){
                            homeVM.statusStates[0] -> {
                                CircularProgressIndicator(
                                    color = Color.Black, // Sets the color of the spinner
                                    strokeWidth = 4.dp // Sets the stroke width of the spinner
                                )
                            }

                            homeVM.statusStates[1] -> {
                                Text(
                                    text = homeVM.GPTResponse.value,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }

                            homeVM.statusStates[2] -> {

                            }
                        }


                    }
                }


                // mr. Praktisk
                Box(
                    modifier = Modifier
                        .offset(290.dp, 25.dp)
                ) {
                    Chicken()
                }
            }


        }
        if(wStatus == homeVM.statusStates[1]){
            DrawSymbol(
                weather!!.symbolName,
                size = 120.dp,
                modifier = Modifier
                    .offset(x = 80.dp, y = 80.dp)
            )
        }

    }
}

