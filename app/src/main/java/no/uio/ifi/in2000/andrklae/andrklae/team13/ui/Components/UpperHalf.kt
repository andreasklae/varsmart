package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings.Background
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
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
    updateMainGpt: (Int, List<String>) -> Unit
){
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // row for actions
            ActionRow(
                updateAll = { updateAll() },
                addToFavourites = { data.addToFavourites() },
                removeFromFavourites = { data.removeFromFavorites() },
                isInFavourites = DataHolder.Favourites.contains(data)
            )
            Spacer(modifier = Modifier.height(10.dp))


            var rise = ""
            var set = ""

            // if sunrise api is successful
            if (data.sunStatus.value == data.statusStates[1]){
                rise = data.rise!!.substringAfter("T").substringBefore("+")
                set = data.set!!.substringAfter("T").substringBefore("+")
            }
            // Row containing basic info like location and current weather
            BasicInfo(
                symbolName = data.currentWeather!!.symbolName!!,
                temperature = data.currentWeather!!.temperature!!,
                loc,
            )
            Spacer(modifier = Modifier.height(10.dp))
            GPTBox(gptText, { updateMainGpt(age, hobbies) } )
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
        ImageIcon(y = 0, x = -20 , symbolId =R.drawable.arrowdown , width =60 , height =25 )
        MrPraktiskBlink({ function()})
    }
}

@Composable
fun ActionRow(
    updateAll: () -> Unit,
    isInFavourites: Boolean,
    addToFavourites: () -> Unit,
    removeFromFavourites: () -> Unit

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // Button for adding to favourites
        // if the location is in the favourites
        if(isInFavourites){
            Box(
                modifier = Modifier
                    .clickable {
                        removeFromFavourites()
                    }
                    .glassEffect()
                    .padding(5.dp)
            ) {
                Icon(
                    Icons.Filled.Bookmark,
                    "refresh",
                    Modifier.size(50.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .clickable {
                        addToFavourites()
                    }
                    .glassEffect()
                    .padding(5.dp)
            ) {
                Icon(
                    Icons.Filled.BookmarkBorder,
                    "refresh",
                    Modifier.size(50.dp)
                )

            }
        }
        Spacer(modifier = Modifier.width(5.dp))

        // button from refreshing the page
        Box(
            modifier = Modifier
                .clickable {
                    updateAll()
                }
                .glassEffect()
                .padding(5.dp)
        ) {
            Icon(
                Icons.Filled.Refresh,
                "refresh",
                Modifier.size(50.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
    }
}

@Composable
fun BasicInfo(
    symbolName: String,
    temperature: Double,
    loc: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(IntrinsicSize.Min)
    ) {
        // Location
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .glassEffect()
                .padding(10.dp)

        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(text = loc, fontSize = 35.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.width(10.dp))

        // temp and weather
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(0.5f)
                .glassEffect()
                .padding(bottom = 10.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            DrawSymbol(symbol = symbolName, size = 85.dp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = temperature.toString() + "°C")
        }

    }
}


@Composable
fun WeatherBox(weatherVM: WeatherViewModel, data: DataHolder){
    val sunStatus = data.sunStatus
    val set = data.set
    val rise = data.rise
    val fontSize = 20
    val iconHeight = weatherVM.spToDp(fontSize.toFloat())
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = 20.dp)
            .glassEffect()
            .padding(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {

            Spacer(modifier = Modifier.weight(1f))
            DrawSymbol(
                data.currentWeather!!.symbolName,
                size = 120.dp,
            )
        }
        Column(
            modifier = Modifier.fillMaxHeight()
        ){
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${data.currentWeather!!.temperature}°C",
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            val highestAndLowest = data.findHighestAndLowestTemp()
            Text(
                text = "H: ${highestAndLowest[1]}°C",
                fontSize = fontSize.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "L: ${highestAndLowest[0]}°C",
                fontSize = fontSize.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }

}



