package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning

import android.content.Context
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.currentCameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Status
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Geometry
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Polygon
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.ActionButton
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents.WarningIcon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.getColorFromString
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents.isoToReadable

@Composable
fun WarningScreen(warningViewModel: WarningViewModel, context: Context) {
    val warningStatus by warningViewModel.loadingStatus.collectAsState()
    val warnings by warningViewModel.warnings.collectAsState()

    // keeps track of the warning api status
    when (warningStatus) {
        Status.LOADING -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(45.dp))
                Text(text = "Alle farevarsler", fontSize = 30.sp)
                Spacer(modifier = Modifier.height(45.dp))
                CircularProgressIndicator(color = Color.Black)
            }
        }

        Status.SUCCESS -> {
            warnings[0]?.let {
                LoadWarningScreen(
                    it,
                    { warningViewModel.loadWarnings() },
                    warningViewModel
                )
            }
        }

        Status.FAILED -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(45.dp))
                Text(text = "Alle farevarsler", fontSize = 30.sp)

                Spacer(modifier = Modifier.height(70.dp))

                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .glassEffect()
                        .background(Color.Red.copy(alpha = 0.3f))
                        .padding(20.dp)
                ) {
                    Text(text = "Feilet", fontSize = 25.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Sjekk internett tilkobling", fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .shadow(3.dp, RoundedCornerShape(16.dp))
                            .padding(3.dp)
                            .clip(RoundedCornerShape(13.dp))
                            .background(Color.White)
                            .clickable {
                                warningViewModel.loadWarnings()
                            }
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Last på nyt",
                            fontSize = 20.sp,
                        )
                    }
                }

            }
        }
    }
}

// showing all warnings on a map
@Composable
fun DisplayAllWarning(
    alerts: List<Feature>,
    findAllPolygons: (Geometry) -> List<Polygon>,
    warningViewModel: WarningViewModel
) {
    GoogleMap(
        modifier = Modifier
            .fillMaxSize(),
        uiSettings = MapUiSettings(zoomControlsEnabled = false),
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(59.9, 10.7), 5f)
        }
    ) {
        // draws a polygon for each alert
        alerts.forEach { feature ->
            var isPolygonSelected by remember { mutableStateOf(false) }

            // colors the polygon accoring to risk level
            val polygonColor = feature.properties.riskMatrixColor
            val polygons = findAllPolygons(feature.geometry)
            val area = feature.properties.area

            // for each polygon of an alert
            polygons.forEach { polygon ->
                Polygon(
                    points = polygon.coordinates,
                    clickable = true,
                    fillColor = if (isPolygonSelected)
                        getColorFromString(polygonColor).copy(alpha = 0.9f)
                    else getColorFromString(polygonColor).copy(alpha = 0.5f),
                    strokeColor = Color.Black,
                    strokeWidth = 5f,
                    tag = area,
                    onClick = {
                        // Handle polygon click event
                        if (isPolygonSelected) {
                            warningViewModel.resetPreview()
                            isPolygonSelected = false
                        } else {
                            warningViewModel.setPreview(feature)
                            isPolygonSelected = true
                        }
                    }
                )
            }
            // deselects the polygon when the map moves
            if (currentCameraPositionState.isMoving) {
                isPolygonSelected = false
            }
        }
        // show no warning box when moving the map
        if (currentCameraPositionState.isMoving) {
            warningViewModel.resetPreview()
        }

    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoadWarningScreen(
    warning: Warning,
    reload: () -> Unit,
    warningViewModel: WarningViewModel
) {
    var showMap by remember { mutableStateOf(false) }
    val selected by warningViewModel.selectedWarning.collectAsState()

    // column of all warnings
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )

            )

    ) {
        item {
            Spacer(modifier = Modifier.height(45.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // header
                Text(
                    text = "Alle farevarsler",
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                // refresh button
                Box(modifier = Modifier.align(Alignment.CenterEnd)){
                    ActionButton(icon = Icons.Filled.Refresh, onClick = { reload() })
                }

            }
            Spacer(modifier = Modifier.height(20.dp))

        }
        item {
            // button for showing the warnings on a map
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape
                    )
                    .padding(2.dp)
                    .clickable { showMap = true }
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                Icon(
                    Icons.Filled.Map,
                    "se i kart",
                    modifier = Modifier
                        .clickable {

                        }
                        .size(50.dp, 50.dp)
                )
                Text(text = "Se i kart")
            }
            Spacer(modifier = Modifier.height(40.dp))

        }

        // boxes for each favourite
        warning.features.forEach {
            item {
                WarningBox(it)
                Spacer(modifier = Modifier.height(20.dp))

            }
        }

        item {
            Spacer(modifier = Modifier.fillMaxHeight())
        }

    }
    if (showMap) {
        // show map as a dialog
        Dialog(onDismissRequest = { showMap = false }) {
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
                        // shows all the polygons on a map
                        DisplayAllWarning(
                            warning.features,
                            { geometry: Geometry ->
                                warningViewModel.warningRepo.findAllPolygons(
                                    geometry
                                        .coordinates
                                )
                            },
                            warningViewModel
                        )
                    }
                    // show info of the selected polygon
                    if (!(selected == null)) {
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
                                LazyColumn(){
                                    item {
                                        WarningBox(selected!!, true)
                                    }

                                }

                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .wrapContentWidth()
                        .clickable {
                            showMap = false
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

// box for displaying warning
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarningBox(feature: Feature, forMap: Boolean = false) {

    val warningDescription = "${feature.properties.instruction}" +
            " \n${feature.properties.description} ${feature.properties.consequences}"
    val warningTitle = feature.properties.thing(feature.properties.area)
    val warningLevel = feature.properties.riskMatrixColor
    var expanded by remember { mutableStateOf(false) }
    val alpha = remember { Animatable(1f) }
    val interval = feature.`when`.interval
    val start = isoToReadable(interval[0])
    val end = isoToReadable(interval[1])

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(
            horizontal = if (forMap) {
                5.dp
            } else {
                20.dp
            }
        )
        .glassEffect(
            // sets the color to white when it is used for a map and yellow if not
            color =
            if (forMap) {
                Color.White
            } else {
                Color(0xffffff8a)
            },
            alt =
            if (forMap) {
                true
            } else {
                false
            }
        )

        // animates expanding and retracting the box
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
                // title
                Text(
                    text = warningTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                // when the warning is relevant
                Text(
                    text = "Gjelder fra $start til $end",
                    fontWeight = FontWeight.Bold
                )
                // description
                Text(
                    text = warningDescription,
                    fontSize = 18.sp
                )
            } else {
                var title = warningTitle
                // shortens the title if it is more than 15 chars
                if (title.length > 15) {
                    title = title.take(15) + "..."
                }
                Text(
                    text = title,
                    fontSize = 16.sp
                )
            }
        }
    }

    // animates expanding and redacting
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