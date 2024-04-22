package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Alert
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.MapWithPolygon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect
import kotlin.math.roundToInt

var expanded = false

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WarningRow(data: DataHolder, range: Int) {
    val alerts = data.alertList
    val filteredAlerts = alerts.filter { it.distance <= range }
    if (filteredAlerts.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { filteredAlerts.size })

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalPager(
                verticalAlignment = Alignment.Top,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )

                    )

            ) {
                val alert = filteredAlerts[pagerState.currentPage]

                DisplayWarning(
                    alert,
                    data.location.name
                )
            }

            // if there are more than one alert, show page indicator
            if (filteredAlerts.size > 1) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .glassEffect()
                        .padding(5.dp)
                ) {
                    filteredAlerts.forEach {
                        var dotModifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .size(10.dp)
                        if (pagerState.currentPage == filteredAlerts.indexOf(it)) {
                            dotModifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .size(12.dp)

                        }
                        Box(modifier = dotModifier)
                    }
                }
            }

        }
    } else EmptyWarning(data.location.name)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayWarning(
    alert: Alert,
    location: String
) {
    val warningDescription = "${alert.alert.properties.instruction}" +
            " \n${alert.alert.properties.description} ${alert.alert.properties.consequences}"
    val warningTitle = alert.alert.properties.thing(alert.alert.properties.area)
    val warningLevel = alert.alert.properties.riskMatrixColor
    val distance = alert.distance
    var showDialog by remember { mutableStateOf(false) }
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                WarningIcon(warningLevel)

                Text(
                    text = "Farevarsel",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Map,
                        contentDescription = "vis i kart",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )


                }
                Spacer(Modifier.weight(1f))
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )


            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 5.dp),
                thickness = 1.dp,
                color = Color.Black
            )
            if (expanded) {
                Text(
                    text = warningTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = distance.roundToInt().toString() + "km unna " + location,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = warningDescription,
                    fontSize = 18.sp
                )
            } else {
                var title = warningTitle
                if (title.length > 15) {
                    title = title.take(15) + "..."
                }
                Text(
                    text = title,
                    fontSize = 16.sp
                )
                Text(text = distance.roundToInt().toString() + "km unna " + location)
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
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Surface(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.8f)
                        .clip(RoundedCornerShape(15.dp))

                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            //.padding(1.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    )
                    {
                        MapWithPolygon(alert)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .wrapContentWidth()
                        .clickable { showDialog = false }
                        .padding(15.dp)

                ) {
                    Icon(Icons.Filled.Close,"fjern fra favoritter")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun EmptyWarning(location: String) {
    val alpha = remember { Animatable(1f) }

    Box(
        modifier = Modifier
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
                    text = "Farevarsel",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 10.dp, bottom = 5.dp)
                )


            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 15.dp),
                thickness = 1.dp,
                color = Color.Black
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(300.dp)
                ) {

                    Text(
                        text = "Ingen farevarsler i nærheten av $location",
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
fun WarningIcon(warningLevel: String) {
    when (warningLevel) {
        "Green" ->
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.warningiconyellow,
                40,
                40
            )

        "Yellow" ->
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.warningiconorange,
                40,
                40
            )

        "Red" ->
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.warningiconred,
                40,
                40
            )

        else ->
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.warningiconyellow,
                40,
                40
            )

    }
}