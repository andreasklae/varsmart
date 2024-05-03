package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.DataHolder
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT.MrPraktiskAnimations
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings.Background
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Status
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents.Next24
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents.RainSunWind
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents.UpperHalf
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents.WarningRow
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.weather.coponents.WeekTable
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect


@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState"
)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WeatherScreen(
    data: DataHolder,
    background: Background,
    updateAll: () -> Unit,
    age: Int,
    gptMain: String,
    mainAnimation: MrPraktiskAnimations,
    hobbies: List<String>,
    updateMainGpt: (Int, List<String>) -> Unit,
    gpt24h: String,
    gpt24hAnimation: MrPraktiskAnimations,
    update24hGpt: (Int) -> Unit,
    gptWeek: String,
    updateGptWeek: (Int, List<String>) -> Unit,
    weekAnimation: MrPraktiskAnimations,
    setPreview: () -> Unit,
    resetPreview: () -> Unit,
    selected: Boolean

) {
    val scrollState = rememberScrollState()
    Box {
        // contents
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            val context = LocalContext.current
            // keeps track of the status of the data
            when (data.weatherStatus.value) {
                // loading
                Status.LOADING -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(70.dp))
                        Text(text = data.location.name, fontSize = 35.sp)
                        Spacer(modifier = Modifier.height(70.dp))
                        CircularProgressIndicator(color = Color.Black)
                    }

                }
                // success
                Status.SUCCESS -> {
                    UpperHalf(
                        data = data,
                        background = background,
                        updateAll = { updateAll() },
                        age = age,
                        gptText = gptMain,
                        hobbies = hobbies,
                        updateMainGpt = { age, hobbies -> updateMainGpt(age, hobbies) },
                        animation = mainAnimation
                    )

                    if (data.alertStatus.value == Status.SUCCESS) {
                        WarningRow(
                            data,
                            40,
                            setPreview = { setPreview() },
                            resetPreview = { resetPreview() },
                            selected = selected
                        )
                    }
                    Next24(
                        data = data,
                        age = age,
                        gpt24h = gpt24h,
                        updateGpt = { update24hGpt(age) },
                        animation = gpt24hAnimation
                    )

                    RainSunWind(data)
                    WeekTable(
                        data = data,
                        age = age,
                        hobbies = hobbies,
                        gptText = gptWeek,
                        updateWeekGpt = { age, hobbies -> updateGptWeek(age, hobbies) },
                        animation = weekAnimation
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                // failed
                Status.FAILED -> {
                    // if it doesnt have any data loaded previously
                    if (data.weather == null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.height(80.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .glassEffect()
                                    .padding(vertical = 20.dp)
                            )
                            {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .width(180.dp)
                                ) {
                                    Text(
                                        text = data.location.name,
                                        fontSize = 35.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
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
                                            Toast.makeText(
                                                context,
                                                "Tester internettforbindelsen...",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            updateAll()
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
                    // if it has previous data
                    else {
                        Toast.makeText(
                            context,
                            "Kunne ikke oppdatere, mangler internett",
                            Toast.LENGTH_SHORT
                        ).show()
                        UpperHalf(
                            data = data,
                            background = background,
                            updateAll = { updateAll() },
                            age = age,
                            gptText = gptMain,
                            hobbies = hobbies,
                            updateMainGpt = { age, hobbies -> updateMainGpt(age, hobbies) },
                            animation = mainAnimation
                        )

                        WarningRow(
                            data,
                            500,
                            setPreview = { setPreview() },
                            resetPreview = { resetPreview() },
                            selected = selected
                        )

                        Next24(
                            data = data,
                            age = age,
                            gpt24h = gpt24h,
                            updateGpt = { update24hGpt(age) },
                            animation = gpt24hAnimation
                        )

                        RainSunWind(data)

                        WeekTable(
                            data = data,
                            age = age,
                            hobbies = hobbies,
                            gptText = gptWeek,
                            updateWeekGpt = { age, hobbies -> updateGptWeek(age, hobbies) },
                            animation = weekAnimation
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                    }

                }
            }

        }
    }


}





