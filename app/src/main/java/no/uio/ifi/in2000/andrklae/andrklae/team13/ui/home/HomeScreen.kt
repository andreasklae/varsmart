package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.text.font.FontWeight
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
            Box (
                modifier = Modifier
                    .offset(290.dp,25.dp)
            ){
                Chicken()
            }


        }

        ImageIcon(85,115,R.drawable.sunclouds,104, 100)


    }
}

@Composable
fun MainComponent(innerPadding: PaddingValues){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .padding(innerPadding)

    ) {
        item {
            UpperHalf()
        }
        item{
            //WarningList should be of actual warning objects
            //the warnings should be organized by priority
            val warningList= mutableListOf(
                "Varsel om snø: Sørlig del av østlandet",
                "Varsel om snø: Sørlig del av østlandet"
            )
            WarningScroll(warningList = warningList)

        }

        item{
            val HourlyTemp= mutableListOf(
                "Varsel om snø: Sørlig del av østlandet",
                "Varsel om snø: Sørlig del av østlandet",
                "Varsel om snø: Sørlig del av østlandet",
                "Varsel om snø: Sørlig del av østlandet",
                "Varsel om snø: Sørlig del av østlandet",
                "Varsel om snø: Sørlig del av østlandet"
            )
            HourlyScroll(hourlyList = HourlyTemp)
        }
        item{
            val dailyTemp= mutableListOf(
                "V", "V", "V", "V", "V", "V", "d"
            )
            DailyTable(dailyTemp = dailyTemp)

        }

    }

}

@Composable
fun ImageIcon(y: Int, x: Int, symbolId: Int, width: Int, height: Int){
    Box(modifier = Modifier
        .offset(y = (y).dp, x=x.dp)
        // adjust the value to suit your design
    ) {
        Image(
            painter = painterResource(id = symbolId),
            contentDescription = "suncloud",
            modifier = Modifier
                .width(width.dp)
                .height(height.dp)
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

@Composable
fun DisplayWarning(warningContent: @Composable () -> Unit, warningDescription: String, warningTitle: String) {
    var expanded by remember { mutableStateOf(false) }
    val alpha = remember { Animatable(1f) }

    Box(modifier = Modifier
        .padding(16.dp)
        .width(380.dp)
        .clip(RoundedCornerShape(15.dp))
        .background(Color.White.copy(alpha = 0.7f))
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )

        )
        .clickable { expanded = !expanded }
        //.heightIn(min = if (expanded) 200.dp else 70.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .alpha(alpha.value)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row{
                ImageIcon(y = 5, x = -2, symbolId = R.drawable.warningiconorange, 55, 45)
                Box (
                    modifier = Modifier
                        .padding(8.dp)
                        .width(300.dp)
                ){
                    Text(
                        text = warningTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(700),
                    )
                }

            }
            if (expanded) {
                WarningContent(warningDescription)
            }

        }
    }

    LaunchedEffect(expanded) {
        if (expanded) {
            alpha.animateTo(
                targetValue = 0.8f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            )
        } else {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            )
        }


    }

}
@Composable
fun WarningContent(warningDescription: String) {
        Box(
            modifier = Modifier
                .width(329.dp)
                .fillMaxHeight()
                .padding(3.dp),
            contentAlignment = Alignment.BottomEnd

        ) {
            Text(
                text = warningDescription ,
                fontSize = 14.sp
            )
        }

    }


//Change the list parameter to be of the type Warning
@Composable
fun WarningScroll(warningList: MutableList<String>){
    if (warningList.isNotEmpty()){
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)){
            val description= "Lorem ipsum dolor sit amet, consectetur adipiscing eli,Lorem ipsum dolor sit amet,\""
            val title= "Varsel om snø: Sørlig del av østlandet"
            warningList.forEach{
                item {
                    DisplayWarning(
                        warningContent = { WarningContent(warningDescription = description) },
                        warningDescription = description,
                        warningTitle = title
                    )
                }
            }

        }
    }



}


//Should have parameters for a list of the type DateTime
//Variables for icon, time, temperature
@Composable
fun HourlyForecast(){
    Box(modifier = Modifier
        .padding(8.dp)
        .width(74.dp)
        .height(110.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(Color.White.copy(alpha = 0.7f))
        .drawWithContent {
            drawContent()
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.1f), // Set the opacity using the alpha value
                size = Size(74.dp.toPx(), 110.dp.toPx()),
                cornerRadius = CornerRadius(40f, 30f),
                style = Stroke(2f)
            )
        },
        contentAlignment = Alignment.Center

    ){

        Column (
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            ImageIcon(y = 0, x = 0, symbolId = R.drawable.sunclouds, width = 50, height = 40)
            Text(
                text = "10°",
                fontSize = 20.sp,

                )

            Text(
                text = "Nå",
                fontSize = 14.sp,

                )
        }

    }


}

//Change the list to of the type Datetime
@Composable
fun HourlyScroll(hourlyList: MutableList<String>){
    LazyRow(horizontalArrangement = Arrangement.spacedBy(1.dp)){
        hourlyList.forEach{
            item {
                HourlyForecast()
            }
        }
    }
}

@Composable
fun DailyForecast(day: String, weatherIcon: Int, midDayTemp: String){
    Row(
        modifier = Modifier
            .padding(8.dp)
    ){
        Text(
            text = day,
            fontSize = 20.sp,

            )
        Spacer(modifier = Modifier.padding(20.dp))

        ImageIcon(y = -3, x = -2, symbolId = weatherIcon, 35, 40)

        Spacer(modifier = Modifier.padding(20.dp))
        Text(
            text = midDayTemp,
            fontSize = 20.sp

        )
        Spacer(modifier = Modifier.padding(20.dp))
        Text(
            text = "10-20mm",
            fontSize = 20.sp
        )

    }

}



@Composable
fun DailyTable(dailyTemp: MutableList<String>){
    Box(modifier = Modifier
        .padding(16.dp)
        .width(380.dp)
        .height(390.dp)
        .clip(RoundedCornerShape(15.dp))
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
        Column {
            dailyTemp.forEach{
                DailyForecast(day = "Mandag", weatherIcon = R.drawable.heavyrain, midDayTemp = "2°",)
            }
        }

    }
}


