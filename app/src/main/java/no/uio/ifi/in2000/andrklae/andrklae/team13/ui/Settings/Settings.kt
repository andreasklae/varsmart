package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings.Background
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.MrPraktiskBlink
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.glassEffect

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    age: Int,
    sliderPosition: Float,
    onSliderChange: (Float) -> Unit,
    onAgeChange: (Float) -> Unit,

    hobbies: List<String>,
    onAddHobby: (String) -> Unit,
    onRemoveHobby: (String) -> Unit,

    background: Background,
    onBackgroundChange: (Background) -> Unit
){
    println(background.imageId)

    val scrollState = rememberScrollState(age)
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

        InfoBox()
        Spacer(modifier = Modifier.height(15.dp))


        AgeSlider(
            age,
            sliderPosition,
            onSliderChange = { onSliderChange(it) },
            valueChange = {onAgeChange(it)}
        )
        Spacer(modifier = Modifier.height(15.dp))


        var hobbyText by remember { mutableStateOf("") }
        HobbyBox(
            hobbyText,
            hobbies,
            onTextChange = { hobbyText = it },
            addToHobbies = { onAddHobby(it) },
            removeFromHobbies = {onRemoveHobby(it)}

        )
        Spacer(modifier = Modifier.height(15.dp))

        var pickedBackgroundImage = background

        BackgroundImageChooser(
            onBackgroundChange = { onBackgroundChange(it)},
            pickedBackgroundImage
        )


        Spacer(modifier = Modifier.height(100.dp))

    }
}

@Composable
fun BackgroundImageChooser(
    onBackgroundChange: (Background) -> Unit,
    pickedBackground: Background
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(10.dp)
    ){
        Text(
            text = "Hvilken bakgrunn vil du ha på hjemskjermen??",
            fontSize = 15.sp
        )
        var scrollState = rememberScrollState()
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .horizontalScroll(scrollState)
        ) {
            Background.images.sortedBy { it != pickedBackground }.forEach{
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            onBackgroundChange(it)
                        }
                        .then(
                            if (pickedBackground == it) {
                                Modifier
                                    .background(Color.Blue)
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(11.dp))
                                    .background(Color.White)
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(9.dp))
                            } else Modifier

                        )
                ) {
                    Image(
                        painter = painterResource(it.imageId),
                        contentDescription = "Background",
                        modifier = Modifier
                            .width(120.dp)
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HobbyBox(
    hobbyText: String,
    hobbies: List<String>,
    onTextChange: (String) -> Unit,
    addToHobbies: (String) -> Unit,
    removeFromHobbies: (String) -> Unit
) {
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
        Text(
            text = "Mr. praktisk tar hensyn til hobbyene dine når han gir deg tips",
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            value = hobbyText,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.SportsSoccer, contentDescription = "Search Icon") },
            trailingIcon = {
                if (hobbyText.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Text",
                        modifier = Modifier.clickable {
                            onTextChange("")
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
                    addToHobbies(hobbyText)
                    onTextChange("")
                }
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (hobbies.isNotEmpty()){
            Text(text = "Hobbyer:")
            HorizontalDivider(
                modifier = Modifier
                    .height(2.dp)
                    .background(Color.Black)
            )
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow {
                hobbies.forEach {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { removeFromHobbies(it) }
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
    }
}

@Composable
fun InfoBox(){
    Spacer(modifier = Modifier.height(30.dp))
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(10.dp)
    ) {
        Text(
            text = "Hei! Jeg er Mr. Praktisk, din personlige KI assistent. " +
                    "Ser du meg på hjemskjermen betyr det at " +
                    "du kan trykke på meg for å få råd og informasjon. " +
                    "Jeg er basert på OpenAI sin GPT 3.5 som betyr at du bør ta det jeg sier med en klype salt. " +
                    "Ha en fin dag!",
            fontSize = 12.sp
        )
    }
    MrPraktiskBlink {}
}

@Composable
fun AgeSlider(
    alder: Int,
    sliderPosition: Float,
    onSliderChange: (Float) -> Unit,
    valueChange: (Float) -> Unit
){
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
            text = "Blir brukt for at Mr. praktisk skal gi deg tilpassede svar",
            fontSize = 10.sp
        )

        Slider(
            value = sliderPosition,
            onValueChange = onSliderChange,
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
        valueChange(sliderPosition)
        val text = {
            if (alder == 25) "25+ år"
            else alder.toString() + " år"
        }
        Text(text = text())

    }
}
