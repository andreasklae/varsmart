package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Map
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings.Background
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Alert
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Geometry
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.ImageIcon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.WeatherScreenMap
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning.DisplayAllWarning
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning.WarningBox
import kotlin.math.roundToInt

var expanded = false

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WarningRow(
    data: DataHolder,
    range: Int,
    setPreview: () -> Unit,
    resetPreview: () -> Unit,
    selected: Boolean
) {
    val alerts = data.alertList

    // filters the alerts to only show the ones nearby
    val filteredAlerts = alerts.filter { it.distance <= range }

    // if there are one or more alerts nearby
    if (filteredAlerts.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { filteredAlerts.size })

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // A pager so that the user can scroll to each warning
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
                // new alert for each page
                val alert = filteredAlerts[pagerState.currentPage]

                DisplayWarning(
                    alert,
                    data.location.name,
                    setPreview = { setPreview() },
                    resetPreview = { resetPreview() },
                    selected = selected
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
                    // one dot for each page
                    filteredAlerts.forEach {
                        var dotModifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .size(10.dp)

                        // highlights the dot of the warning in focus
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
    }
    // when there are no alerts nearby
    else EmptyWarning(data.location.name)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayWarning(
    alert: Alert,
    location: String,
    setPreview: () -> Unit,
    resetPreview: () -> Unit,
    selected: Boolean
) {
    // description
    val warningDescription = "${alert.alert.properties.instruction}" +
            " \n${alert.alert.properties.description} ${alert.alert.properties.consequences}"

    // when the warning is relevant
    val interval = alert.alert.`when`.interval
    val start = isoToReadable(interval[0])
    val end = isoToReadable(interval[1])

    // title
    val warningTitle = alert.alert.properties.thing(alert.alert.properties.area)

    // risk level
    val warningLevel = alert.alert.properties.riskMatrixColor

    // distance to the warning area
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

                // warning symbol based on the risk level
                WarningIcon(warningLevel)

                // header
                Text(
                    text = "Farevarsel",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(15.dp))

                // button to show the warning in map
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .size(50.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape
                        )
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F1F1))
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Map,
                        contentDescription = "vis i kart",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )


                }
                Spacer(Modifier.weight(1f))

                // indicator for whether the waring is exposed or not
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )


            }
            Spacer(modifier = Modifier.height(5.dp))

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 5.dp),
                thickness = 1.dp,
                color = Color.Black
            )

            // when the warning is expanded
            if (expanded) {

                // title
                Text(
                    text = warningTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // distance to warning area
                Text(
                    text = distance.roundToInt().toString() + "km unna " + location,
                    fontWeight = FontWeight.Bold
                )

                // when the alert is active
                Text(
                    text = "Gjelder fra $start til $end",
                    fontWeight = FontWeight.Bold
                )

                // description
                Text(
                    text = warningDescription,
                    fontSize = 18.sp
                )
            }

            // when not expanded
            else {
                // shortens the title if its more than 15 chars
                var title = warningTitle
                if (title.length > 15) {
                    title = title.take(15) + "..."
                }

                // title
                Text(
                    text = title,
                    fontSize = 16.sp
                )

                // distance to warning area
                Text(text = distance.roundToInt().toString() + "km unna " + location)
            }
        }
    }

    // animates the expanding and minimizing
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

    // if the map should show
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
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    )
                    {
                        WeatherScreenMap(
                            alert,
                            setPreview = { setPreview() },
                            resetPreview = { resetPreview() },
                            selected = selected
                        )
                    }
                    // if a polygon is selected
                    if (selected) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Add a spacer to push the bottom content to the bottom
                            Spacer(modifier = Modifier.weight(1f))

                            // Use Box to align the content to the bottom center
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                LazyColumn() {
                                    item {
                                        // show the info of the warning
                                        WarningBox(alert.alert, true)
                                    }
                                }

                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                // button to hide the map
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .wrapContentWidth()
                        .clickable {
                            showDialog = false
                        }
                        .padding(15.dp)

                ) {
                    Icon(Icons.Filled.Close, "Lukk kartet")
                }
                Spacer(modifier = Modifier.weight(1f))
            }

        }
    }
}

// function for turning ISO time format into regular text
fun isoToReadable(iso: String): String {

    // finds the day and month
    val date = iso.split("T").first().split("-")
    val month = date[1]
    val day = date[2]

    // finds the time of day
    val time = iso.split("T")[1].split(":")
    val hour = time[0]
    val minute = time[1]

    // turns month number into text
    val monthName = {
        when (month.toInt()) {
            1 -> "Januar"
            2 -> "Februar"
            3 -> "Mars"
            4 -> "April"
            5 -> "Mai"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "August"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            else -> "Desember"
        }
    }

    return "$day. ${monthName()} $hour:$minute"
}

// what to show when there are no nearby alerts
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
            // header
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
                    // description
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

    // gives the correct warning icon based on the risk level of an alert
    when (warningLevel) {
        "Green" ->
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.warningiconyellow,
                40,
                40,
                "lav risiko"
            )

        "Yellow" ->
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.warningiconorange,
                40,
                40,
                "moderat risiko"
            )

        "Red" ->
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.warningiconred,
                40,
                40,
                "høy risiko"
            )

        else ->
            ImageIcon(
                y = 0,
                x = 0,
                symbolId = R.drawable.warningiconyellow,
                40,
                40,
                "risiko"
            )

    }
}