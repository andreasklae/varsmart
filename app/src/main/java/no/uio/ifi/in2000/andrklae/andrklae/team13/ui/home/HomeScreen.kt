package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.Team13Theme
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.infoShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Team13Theme {

        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(

            ) { innerPadding ->

                MainComponent(innerPadding = innerPadding)

            }
        }


    }
}

@Preview
@Composable
fun prevHomeScreen(){
    HomeScreen()
}


@Composable
fun TopAppBar(){
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
       verticalAlignment = Alignment.CenterVertically
    ) {
        favorittButton()
        mapButton()
        Spacer(modifier = Modifier.weight(1f))
        settingsButton()
    }
}

@Composable
fun UpperHalf(){
    Box(modifier = Modifier
        .fillMaxSize()

    ){
        Image(
            painter = painterResource(id = R.drawable.flowers),
            contentDescription = "",
            contentScale= ContentScale.FillBounds,
            modifier=Modifier.matchParentSize()

        )

        Column() {
            TopAppBar()
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = "OSLO", fontSize = 30.sp)

            }
            Spacer(modifier = Modifier.height(25.dp))
            mainForecastBox()

            Spacer(modifier = Modifier.height(50.dp))
        }

        WeatherIcon()


    }
}

@Composable
fun MainComponent(innerPadding: PaddingValues){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(innerPadding)

    ) {
        item {
            UpperHalf()
            Box(modifier= Modifier
                .background(Color.Blue)
                .height(100.dp)
                .fillMaxWidth()){

            }

        }
        item{

        }

    }

}

@Composable
fun WeatherIcon(){
    Box(modifier = Modifier
        .offset(y = (85).dp, x=115.dp)
        // adjust the value to suit your design
    ) {
        Image(
            painter = painterResource(id = R.drawable.sunclouds),
            contentDescription = "suncloud",
            modifier = Modifier
                .width(104.dp)
                .height(100.dp)
        )
    }
}

@Composable
fun favorittButton(){
    IconButton(
        onClick = { /* handle button click */ },
        modifier = Modifier
            .size(50.dp)
            .padding(5.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.favoritt),
            contentDescription = "Image Description",

            modifier = Modifier
                .size(40.dp)

        )
    }
}

@Composable
fun settingsButton(){
    IconButton(
        onClick = { /* handle button click */ },
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = Color.Black
        )
    }
}

@Composable
fun mapButton(){
    IconButton(
        onClick = { /* handle button click */ },
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Settings",
            tint = Color.Black
        )
    }
}


@Composable
fun mainForecastBox(){

    Box(modifier = Modifier
        .padding(16.dp)
        .width(160.dp)
        .height(134.dp)
        .clip(RoundedCornerShape(40.dp))
        .background(Color.White.copy(alpha = 0.8f))
        .drawWithContent {
            drawContent()
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.1f), // Set the opacity using the alpha value
                size = Size(160.dp.toPx(), 134.dp.toPx()),
                cornerRadius = CornerRadius(40f, 30f),
                style = Stroke(2f)
            )
        },
        contentAlignment = Alignment.Center

    ){

        Text(
            text = "25°",
            fontSize = 35.sp,

        )



    }
}



