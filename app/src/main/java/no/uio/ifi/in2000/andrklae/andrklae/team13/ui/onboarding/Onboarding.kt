package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.onboarding

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings.AgeSlider
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings.HobbyBox
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings.InfoBox
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings.SettingsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Onboarding(
    context: Context,
    onboardingViewModel: OnboardingViewModel,
    settingsVM: SettingsViewModel
){
    val age = settingsVM.age.collectAsState()
    val sliderPosition = settingsVM.sliderPosition.collectAsState()
    var hobbyText by remember { mutableStateOf("") }
    var hobbies = settingsVM.hobbies.collectAsState()

    var consented by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(
        pageCount = { 4 },
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Værsmart",
            fontSize = 20.sp
            )
        Spacer(modifier = Modifier.height(20.dp))

        HorizontalPager(
            verticalAlignment = Alignment.Top,
            state = pagerState,
            contentPadding = PaddingValues(20.dp),
            pageSpacing = 20.dp,
            beyondBoundsPageCount = 4
        ) { page ->
            when(page){
                0 -> {
                    Column(
                        verticalArrangement = Arrangement.Top
                    ) {
                        InfoBox()
                    }
                }

                1 -> {
                    AgeSlider(
                        age = age.value,
                        sliderPosition = sliderPosition.value,
                        onSliderChange = { newPosition ->
                            settingsVM.changeSliderPosition(
                                newPosition
                            )
                        },
                        onValueChange = { fraction ->
                            settingsVM.changeAgeByFraction(
                                fraction,
                                context
                            )
                        }
                    )


                }

                2 -> {
                    HobbyBox(
                        hobbyText = hobbyText,
                        hobbies = hobbies.value,
                        onTextChange = { hobbyText = it },
                        addToHobbies = { hobby -> settingsVM.addHobby(hobby, context) },
                        removeFromHobbies = { hobby -> settingsVM.removeHobby(hobby, context) }
                    )
                }

                3 -> ConsentBox(consented, {consented = it})
            }
        }
        Row {
            for (i in 1..pagerState.pageCount){
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .padding(1.dp)
                        .clip(CircleShape)
                        .then(
                            if (i - 1 == pagerState.currentPage) {
                                Modifier.background(color = Color.Black)
                            } else {
                                Modifier.background(color = Color.Gray)
                            }
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            val coroutine = rememberCoroutineScope()
            if(pagerState.currentPage != 0){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .shadow(
                            elevation = 5.dp,
                            shape = CircleShape
                        )
                        .padding(2.dp)
                        .clickable {
                            coroutine.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }

                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription ="forrige" )
                    Text(text = "Forige")
                }
            }
            if (pagerState.currentPage != 3){
                Spacer(modifier = Modifier.width(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .shadow(
                            elevation = 5.dp,
                            shape = CircleShape
                        )
                        .padding(2.dp)
                        .clickable {
                            coroutine.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }

                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(10.dp)
                ) {
                    Text(text = "Neste")
                    Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription ="neste")

                }
            }
            else{
                Spacer(modifier = Modifier.width(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .shadow(
                            elevation = 5.dp,
                            shape = CircleShape
                        )
                        .padding(2.dp)
                        .clickable {
                            if (consented) {
                                onboardingViewModel.completeOnboarding(context)
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "Du må samtykke for å gå videre",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }

                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(10.dp)
                ) {
                    val color = {
                        if (consented){
                            Color.Black
                        }
                        else Color.LightGray
                    }
                    Text(
                        text = "Ferdig",
                        color = color()
                    )
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription ="neste",
                        tint = color()
                    )
                }
            }

        }

    }
}

@Composable
fun ConsentBox(
    consented: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(10.dp)
    ) {
        Text(
            text = "Ved bruk av Mr. Praktisk sender du alderen din, hobbyene dine og værdata " +
                    "(inludert stedsnavn) til Open AI. Annen personopplysninger blir " +
                    "ikke lagret."
                    ,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //modifier = Modifier.clickable { consented = !consented }
            ) {
                Checkbox(
                    checked = consented,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkmarkColor = Color.White,
                        checkedColor = Color.Black,
                        uncheckedColor = Color.Black
                    )
                )
            }
            Text(text = "Jeg samtykker")
        }
    }
}
