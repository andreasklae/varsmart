package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.MrPraktisk
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    settingsVm: SettingsViewModel
){
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .glassEffect()
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Innstillinger",
            fontSize = 40.sp
        )
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF5F5F5))
                .padding(10.dp)
        ) {
            Text(
                text = "Hei! Jeg er Mr. Praktisk, din personlige assistent. " +
                        "Ser du meg på hjemskjermen betyr det at " +
                        "du kan trykke på meg for å få råd og informasjon. " +
                        "Jeg er en KI basert på OpenAI sin GPT 3.5 som betyr at du bør ta det jeg sier med en klype salt. " +
                        "Ha en fin dag!",
                fontSize = 12.sp
            )
        }
        MrPraktisk {}
        Spacer(modifier = Modifier.height(30.dp))

        // age slider
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF5F5F5))
                .padding(10.dp)
        ) {
            Text(
                text = "Hvor gammel er du?",
                fontSize = 15.sp
            )
            Text(
                text = "Blir brukt for at Mr. praktisk skal gi bedre svar",
                fontSize = 10.sp
            )
            var sliderPosition by remember { mutableFloatStateOf(0f) }
            val alder by settingsVm.age.collectAsState()
            Slider(
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                },
                colors = SliderColors(
                    thumbColor = Color.Black,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White,
                    activeTickColor = Color.Black,
                    inactiveTickColor = Color.Black,
                    disabledThumbColor = Color.Black,
                    disabledActiveTrackColor = Color.White,
                    disabledActiveTickColor = Color.White,
                    disabledInactiveTrackColor = Color.White,
                    disabledInactiveTickColor = Color.White
                )
            )
            settingsVm.changeAgeByFraction(sliderPosition.toDouble())
            val text = {
                if (alder == 25) "25+ år"
                else alder.toString() + " år"
            }
            Text(text = text())

        }
        Spacer(modifier = Modifier.height(15.dp))

        // Hobbies
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF5F5F5))
                .padding(10.dp)
        ) {
            Text(
                text = "Har du noen hobbyer?",
                fontSize = 15.sp
            )
            var text by remember { mutableStateOf("") }
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            var hobbies = settingsVm.hobbies.collectAsState()

            OutlinedTextField(
                value = text,
                onValueChange = {text = it},
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.SportsSoccer, contentDescription = "Search Icon") },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Text",
                            modifier = Modifier.clickable {
                                text = ""
                            }
                        )
                    }
                },
                placeholder = { Text("Skriv en hobby...") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        settingsVm.addHobby(text)
                        text = ""
                    }
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Hobbyer:")
            HorizontalDivider(modifier = Modifier
                .height(2.dp)
                .background(Color.Black))
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow {
                hobbies.value.forEach {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { settingsVm.removeHobby(it) }
                            .background(Color.Red.copy(alpha = 0.5f))
                            .padding(5.dp)
                    ) {
                        Row {
                            Text(text = it)
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Play",)
                        }

                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }


        }
        Spacer(modifier = Modifier.height(100.dp))

    }
}
