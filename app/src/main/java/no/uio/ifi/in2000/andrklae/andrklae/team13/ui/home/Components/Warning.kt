package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.glassEffect
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.HomeViewModel
import kotlin.math.roundToInt

var expanded = false
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Warning(homeVM: HomeViewModel, range: Int){
    val alerts by homeVM.alerts.collectAsState()
    val filteredAlerts = alerts.filter { it.distance <= range }
    val pageState = rememberPagerState(pageCount = { filteredAlerts.size })
    if (filteredAlerts.isNotEmpty()){
        val scrollState = rememberLazyListState()
        val flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState)
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            state = scrollState,
            flingBehavior = flingBehavior
            ) {
            items(filteredAlerts.size) { item ->
                val alert = filteredAlerts[item]
                var focused = false
                var foucusedMod = Modifier.fillParentMaxWidth()
                if (scrollState.firstVisibleItemIndex == item && !scrollState.isScrollInProgress){
                    focused = true
                } else focused = false

                if (focused){
                    foucusedMod = Modifier.fillParentMaxWidth()
                }
                else{
                    foucusedMod = Modifier
                        .fillParentMaxWidth()
                        .alpha(0.5f)
                }
                Box(
                    modifier = foucusedMod
                ){
                    DisplayWarning(
                        warningDescription = "${alert.alert.properties.instruction} \n${alert.alert.properties.description} ${alert.alert.properties.consequences}",
                        warningTitle = alert.alert.properties.area,
                        warningLevel = alert.alert.properties.riskMatrixColor,
                        distance = alert.distance
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier
            )
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    filteredAlerts.forEach{
                        var dotModifier = Modifier
                            .padding(2.dp)
                            .glassEffect()
                            .glassEffect()
                            .glassEffect()
                            .clip(CircleShape)
                            .size(15.dp)
                        if (scrollState.firstVisibleItemIndex != filteredAlerts.indexOf(it)){
                            dotModifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .size(10.dp)

                        }
                        Box(modifier = dotModifier)

                    }
                }

            }

        }
    } else EmptyWarning()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayWarning(
    warningDescription: String,
    warningTitle: String,
    warningLevel: String,
    distance: Double
) {
    var expanded by remember { mutableStateOf(false) }
    val alpha = remember { Animatable(1f) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .glassEffect()

        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )

        )
        .clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .alpha(alpha.value)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth() // Ensure the Row fills the max width
            ) {
                WarningIcon(warningLevel)

                Text(
                    text = "Værvarsel",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )

            }
            Divider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            if (expanded) {
                Text(
                    text = warningTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = warningDescription,
                    fontSize = 18.sp
                )
            }
            else{
                var title = warningTitle
                if (title.length > 25){
                    title = title.take(25) + "..."
                }
                Text(
                    text = title,
                    fontSize = 16.sp
                )
                Text(text = distance.roundToInt().toString() + "km unna")
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
fun EmptyWarning(){
    val alpha = remember { Animatable(1f) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .glassEffect()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {

        }
        Column(
            modifier = Modifier
                .alpha(alpha.value)
                .fillMaxSize()
        ) {
            Row {
                Text(
                    text = "Værvarsel",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 10.dp, bottom = 5.dp)
                )


            }

            Divider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp)
            ){
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(300.dp)
                ) {

                    Text(
                        text = "Ingen farevarsler i nærheten",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(Modifier.height(30.dp))
                }

            }

        }
    }

}

@Composable
fun WarningIcon(warningLevel: String){
    when (warningLevel) {
        "Green" ->
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.warningiconyellow,
                50,
                40
            )

        "Yellow" ->
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.warningiconorange,
                40,
                30
            )

        "Red" ->
            ImageIcon(
                y = 5,
                x = -2,
                symbolId = R.drawable.warningiconred,
                55,
                45
            )

        else ->
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.warningiconyellow,
                55,
                45
            )

    }
}