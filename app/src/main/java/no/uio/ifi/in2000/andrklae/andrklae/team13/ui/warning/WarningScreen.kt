package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.warning

import android.content.Context
import android.location.GnssAntennaInfo.Listener
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.currentCameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.ktx.model.cameraPosition
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Status
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Geometry
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Polygon
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Warning
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.ActionButton
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents.WarningIcon
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.getColorFromString
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect

@Composable
fun WarningScreen(warningViewModel: WarningViewModel, context: Context) {
    val warningStatus by warningViewModel.loadingStatus.collectAsState()
    val warnings by warningViewModel.warnings.collectAsState()

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
                CircularProgressIndicator()
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
                                Toast
                                    .makeText(
                                        context,
                                        "Tester internettforbindelsen...",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
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
        alerts.forEach { feature ->
            var isPolygonSelected by remember { mutableStateOf(false) }
            val polygonColor = feature.properties.riskMatrixColor
            val polygons = findAllPolygons(feature.geometry)
            val area = feature.properties.area
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
            if (currentCameraPositionState.isMoving) {
                isPolygonSelected = false
            }
        }
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
    // sorts favorite list to put current location at the top of the list

    // column of all favourites
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
        // spacer
        item {
            Spacer(modifier = Modifier.height(45.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Alle farevarsler",
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                Box(modifier = Modifier.align(Alignment.CenterEnd)){
                    ActionButton(icon = Icons.Filled.Refresh, onClick = {reload()})
                }

            }
            Spacer(modifier = Modifier.height(20.dp))

        }
        item {
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
                    "show all in map",
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
                            //.padding(1.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    )
                    {
                        DisplayAllWarning(
                            warning.features,
                            { geometry: Geometry ->
                                warningViewModel.warningRepositoryInterface.findAllPolygons(
                                    geometry
                                        .coordinates
                                )
                            },
                            warningViewModel
                        )
                    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarningBox(feature: Feature, forMap: Boolean = false) {

    val warningDescription = "${feature.properties.instruction}" +
            " \n${feature.properties.description} ${feature.properties.consequences}"
    val warningTitle = feature.properties.thing(feature.properties.area)
    val warningLevel = feature.properties.riskMatrixColor
    var expanded by remember { mutableStateOf(false) }
    val alpha = remember { Animatable(1f) }

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
            if (forMap) {
                Color.White
            } else {
                Color(0xffffff8a)
            },
            if (forMap) {
                true
            } else {
                false
            }
        )

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
                Text(
                    text = warningTitle,
                    fontSize = 20.sp,
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

fun calculatePolygonCenter(polygon: List<LatLng>): LatLng {
    var totalLat = 0.0
    var totalLng = 0.0

    for (point in polygon) {
        totalLat += point.latitude
        totalLng += point.longitude
    }

    val centerLat = totalLat / polygon.size
    val centerLng = totalLng / polygon.size

    return LatLng(centerLat, centerLng)
}

fun getColorFromString(colorString: String): Color {
    return when (colorString.lowercase()) {
        "yellow" -> Color.Yellow
        "green" -> Color.Green
        "orange" -> Color(0xFFFFA500)
        "red" -> Color.Red
        // Add more cases as needed
        else -> Color.Black // Default color or any other color you prefer
    }
}