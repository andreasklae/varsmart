package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT.MrPraktiskAnimations
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.PreferenceManager
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings.Background
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Weather.DateTime
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.DrawSymbol
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.ImageIcon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.MrPraktisk
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect
import java.time.LocalDateTime

// the top components of the app
@Composable
fun UpperHalf(
    data: DataHolder,
    background: Background,
    updateAll: () -> Unit,
    age: Int,
    gptText: String,
    hobbies: List<String>,
    updateMainGpt: (Int, List<String>) -> Unit,
    animation: MrPraktiskAnimations
) {
    val loc = data.location.name
    val context = LocalContext.current
    // box to put the image behind the contents
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
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .glassEffect()
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                ) {
                    // location name
                    Column(Modifier.weight(20f)) {
                        Text(
                            text = loc,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Left
                        )
                        // finds the current time of the device
                        var current = LocalDateTime.now()
                        var currentYear = current.year.toString()
                        var currentMonth = current.monthValue.toString()
                        var currentDay = current.dayOfMonth.toString()
                        var currentHour = current.hour.toString()
                        var currentMinute = current.minute.toString()
                        var dt = DateTime(
                            currentYear,
                            currentMonth,
                            currentDay,
                            currentHour,
                            currentMinute
                        )
                        // sets the correct description of the interval since last update
                        val interval = data.lastUpdate.getInterval(dt)

                        // time since last update
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Filled.Restore, contentDescription = "sist oppdatert")
                            Text(text = interval)
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // bookmark icon
                    // checks if the data is saved in favourites and gives correct icon
                    val iconId = {
                        if (DataHolder.Favourites.contains(data)) {
                            Icons.Filled.Bookmark
                        }
                        // current location of device will not be true on the if check above
                        // because reverse geocoding a running async, making the date somewhat
                        // different by the time the ui loads. the code below makes an exception
                        // for when the location is the devices location
                        else if (
                            data.location.name.contains("Min posisjon")
                            &&
                            DataHolder.Favourites
                                .filter { it.location.name.contains("Min posisjon") }
                                .isNotEmpty()
                        ) {
                            Icons.Filled.Bookmark
                        } else Icons.Filled.BookmarkBorder
                    }
                    // checks if the data is saved in favourites and gives correct toast message
                    val toastString = {
                        if (DataHolder.Favourites.contains(data)) {
                            "${data.location.name} fjernet fra favoritter"
                        } else "${data.location.name} lagt til i favoritter"
                    }

                    // button from refreshing the page
                    IconButton(
                        onClick = {
                            updateAll()
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Oppdater været",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))

                    // Button for adding or removing from favourites
                    IconButton(
                        onClick = {
                            Toast.makeText(context, toastString(), Toast.LENGTH_SHORT)
                                .show()
                            data.toggleInFavourites()
                            PreferenceManager.saveFavourites(context, DataHolder.Favourites)
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = iconId(),
                            contentDescription = "lagre i favoritter",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                }
                // basic info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {

                    // temp and weather
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = data.currentWeather!!.temperature.toString() + "°C",
                            textAlign = TextAlign.Start,
                            fontSize = 35.sp
                        )

                        // highest and lowest temp of the day
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // highest
                            Icon(
                                imageVector = Icons.Filled.ArrowUpward,
                                contentDescription = "Høyest",
                                tint = Color(0xFFFC2600)
                            )
                            Text(text = data.highest.toString() + "°C")
                            Spacer(modifier = Modifier.width(5.dp))

                            // lowest
                            Icon(
                                imageVector = Icons.Filled.ArrowDownward,
                                contentDescription = "Lavest",
                                tint = Color(0xFF093CF7)
                            )
                            Text(data.lowest.toString() + "°C")
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    // weather symbol
                    DrawSymbol(symbol = data.currentWeather!!.symbolName, size = 100.dp)

                }
            }


            Spacer(modifier = Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(10.dp))
            GPTBox(gptText, { updateMainGpt(age, hobbies) }, animation)
        }
    }
}

// Mr. Praktisk and a speech bubble
@Composable
fun GPTBox(
    content: String,
    function: () -> Unit,
    animation: MrPraktiskAnimations

) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // speech bubble
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(10.dp)
        ) {
            Text(
                text = content,
                fontSize = 15.sp,
                lineHeight = 15.sp,
            )
        }

        // arrow pointing the bubble to Mr. Praktisk
        ImageIcon(
            y = 0,
            x = -20,
            symbolId = R.drawable.arrowdown,
            width = 60, height = 25,
            "snakkeboble"
        )
        MrPraktisk({ function() }, animation)
    }
}



