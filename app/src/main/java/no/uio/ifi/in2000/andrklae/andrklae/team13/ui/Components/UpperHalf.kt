package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings.Background
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Status
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.ActionButton
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.WeatherViewModel

@Composable
fun UpperHalf(
    data: DataHolder,
    background: Background,
    updateAll: () -> Unit,
    age: Int,
    gptText: String,
    hobbies: List<String>,
    updateMainGpt: (Int, List<String>) -> Unit,
) {
    val loc = data.location.name
    Box {
        // background image
        Image(
            painter = painterResource(background.imageId),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(570.dp),
            contentScale = ContentScale.Crop
        )
        // main components of the screen
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            Spacer(modifier = Modifier.height(40.dp))
            Column (
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .glassEffect()
                    .padding(10.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        //.padding(horizontal = 20.dp)
                        //.glassEffect()
                        //.padding(10.dp)
                ){
                    Spacer(modifier = Modifier.width(16.dp))

                    Column(Modifier.weight(20f)) {
                        Text(
                            text = loc,
                            fontSize = 40.sp,
                            textAlign = TextAlign.Left
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // if the location is in the favourites
                    val iconId = {
                        if (DataHolder.Favourites.contains(data)) {
                            Icons.Filled.Bookmark
                        } else Icons.Filled.BookmarkBorder
                    }

                    // button from refreshing the page
                    IconButton(
                        onClick = { updateAll() },
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "lagre i favoritter",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    // Button for adding or removing from favourites
                    IconButton(
                        onClick = { data.toggleInFavourites() },
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            imageVector = iconId(),
                            contentDescription = "lagre i favoritter",
                            modifier = Modifier.size(50.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                }
                //Spacer(modifier = Modifier.height(20.dp))
                var rise = ""
                var set = ""

                // if sunrise api is successful
                if (data.sunStatus.value == Status.SUCCESS) {
                    rise = data.rise!!.substringAfter("T").substringBefore("+")
                    set = data.set!!.substringAfter("T").substringBefore("+")
                }
                // basic info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        //.padding(horizontal = 20.dp)
                        //.glassEffect()
                        //.padding(horizontal = 10.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    // temp and weather
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = "Nå: " + data.currentWeather!!.temperature.toString() + "°C"
                            ,
                            textAlign = TextAlign.Center,
                            fontSize = 35.sp
                        )
                        Row {
                            Icon(
                                imageVector = Icons.Filled.ArrowUpward,
                                contentDescription = "Høyest",
                                tint = Color(0xffc94242)
                            )
                            Text(text = data.findHighestAndLowestTemp()[1] + "°C")
                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(
                                imageVector = Icons.Filled.ArrowDownward,
                                contentDescription = "Lavest",
                                tint = Color.Blue
                            )
                            Text(data.findHighestAndLowestTemp()[0] + "°C")
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    DrawSymbol(symbol = data.currentWeather!!.symbolName, size = 100.dp)

                }
            }


            Spacer(modifier = Modifier.height(10.dp))

            Spacer(modifier = Modifier.height(10.dp))
            GPTBox(gptText, { updateMainGpt(age, hobbies) })
        }
    }
}

@Composable
fun GPTBox(content: String, function: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(10.dp)
        ) {
            Text(
                text = content,
                fontSize = 12.sp
            )
        }
        ImageIcon(y = 0, x = -20, symbolId = R.drawable.arrowdown, width = 60, height = 25)
        MrPraktiskBlink({ function() })
    }
}

@Composable
fun ActionRow(
    updateAll: () -> Unit,
    isInFavourites: Boolean,
    toggleInFavourites: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // if the location is in the favourites
        val iconId = {
            if (isInFavourites) {
                Icons.Filled.Bookmark
            } else Icons.Filled.BookmarkBorder
        }
        // Button for adding or removing from favourites
        ActionButton(
            icon = iconId(),
            onClick = {toggleInFavourites()}
        )

        Spacer(modifier = Modifier.width(5.dp))

        // button from refreshing the page
        ActionButton(
            icon = Icons.Filled.Refresh,
            onClick = { updateAll() }
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}



