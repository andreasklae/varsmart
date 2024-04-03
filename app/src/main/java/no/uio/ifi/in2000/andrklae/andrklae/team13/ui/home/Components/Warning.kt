package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.Components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.glassEffect
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home.HomeViewModel

@Composable
fun Warning(homeVM: HomeViewModel){
    val warning by homeVM.warning.collectAsState()

    if (warning != null){
        println(warning!!.properties.area)
        DisplayWarning(
            warningContent = { WarningContent(warningDescription = warning!!.properties.area) },
            warningDescription = "${warning!!.properties.instruction} \n${warning!!.properties.description} ${warning!!.properties.consequences}",
            warningTitle = "${warning!!.properties.area}:\n${warning!!.properties.eventAwarenessName}",
            warningLevel = warning!!.properties.riskMatrixColor
        )
    } else {
        DisplayWarning(
            warningContent = { WarningContent(warningDescription = "No nearby alerts") },
            warningDescription = "No nearby alerts",
            warningTitle = "No nearby alerts",
            warningLevel = "Green"
        )
    }
}
@Composable
fun WarningContent(warningDescription: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.BottomEnd

    ) {
        Text(
            text = warningDescription,
            fontSize = 18.sp
        )
    }

}

@Composable
fun DisplayWarning(warningContent: @Composable () -> Unit, warningDescription: String, warningTitle: String, warningLevel: String) {
    var expanded by remember { mutableStateOf(false) }
    val alpha = remember { Animatable(1f) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
        .glassEffect()

        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )

        )
        .clickable { expanded = !expanded }
        //.heightIn(min = if (expanded) 200.dp else 70.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

        }
        Column(
            modifier = Modifier
                .alpha(alpha.value)
                .fillMaxSize()
        ) {
            Row {
                Text(
                    text = "Vær varsel",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp, bottom = 10.dp)
                )
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .fillMaxWidth()
                ){
                    when (warningLevel) {
                        "Green" ->
                            ImageIcon(y = 5, x = -2, symbolId = R.drawable.warningiconyellow, 55, 45)

                        "Yellow" ->
                            ImageIcon(y = 5, x = -2, symbolId = R.drawable.warningiconorange, 55, 45)

                        "Red" ->
                            ImageIcon(y = 5, x = -2, symbolId = R.drawable.warningiconred, 55, 45)

                        else ->
                            ImageIcon(y = 5, x = -2, symbolId = R.drawable.warningiconyellow, 55, 45)

                    }
                }

            }

            Divider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            Row(
                modifier = Modifier.padding(20.dp)
            ){

                Box(
                    modifier = Modifier
                        .width(300.dp)
                ) {
                    Text(
                        text = warningTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
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
