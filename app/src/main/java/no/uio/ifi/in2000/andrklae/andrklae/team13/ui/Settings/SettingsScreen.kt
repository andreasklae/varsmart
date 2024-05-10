package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT.MrPraktiskAnimations
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Settings.Background
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Components.MrPraktisk
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
) {
    val context = LocalContext.current

    val scrollState = rememberScrollState(age)

    // scrollable column of the contents
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

        // header
        Text(
            text = "Innstillinger",
            fontSize = 40.sp
        )
        Spacer(modifier = Modifier.height(60.dp))

        // info from Mr. Praktisk
        InfoBox()
        Spacer(modifier = Modifier.height(15.dp))

        // setting age
        AgeSlider(
            age,
            sliderPosition,
            onSliderChange = { onSliderChange(it) },
            onValueChange = { onAgeChange(it) }
        )
        Spacer(modifier = Modifier.height(15.dp))


        var hobbyText by remember { mutableStateOf("") }
        // adding or removing hobbies
        HobbyBox(
            hobbyText,
            hobbies,
            onTextChange = { hobbyText = it },
            addToHobbies = { onAddHobby(it) },
            removeFromHobbies = { onRemoveHobby(it) }

        )
        Spacer(modifier = Modifier.height(15.dp))

        var pickedBackgroundImage = background
        // background chooser
        BackgroundImageChooser(
            onBackgroundChange = {
                Toast.makeText(context, "Endrer bakgrunnsbilde...", Toast.LENGTH_SHORT).show()
                onBackgroundChange(it)
            },
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
    ) {
        Text(
            text = "Hvilken bakgrunn vil du ha?",
            fontSize = 15.sp
        )
        var scrollState = rememberScrollState()

        // scrollable row of each background the user can pick
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .horizontalScroll(scrollState)
        ) {
            Background.images.sortedBy { it != pickedBackground }.forEach {
                val coroutine = rememberCoroutineScope()
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            coroutine.launch {
                                // sets the background if the user clicks it
                                onBackgroundChange(it)
                                // scrolls the row to the start
                                scrollState.animateScrollTo(
                                    value = 0,
                                    animationSpec = tween(
                                        durationMillis = 600,  // Duration of the animation
                                        easing = LinearOutSlowInEasing
                                    )
                                )
                            }
                        }
                        .then(
                            // outlines the picked background
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
                        contentDescription = it.name,
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
        // header
        Text(
            text = "Har du noen hobbyer?",
            fontSize = 15.sp
        )
        // info
        Text(
            text = "Mr. praktisk tar hensyn til hobbyene dine når han gir deg tips",
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        // text-field for typing hobbies
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
                // when clicking ok on the keyboard
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    // if the textfield has text
                    if (hobbyText.isNotEmpty()) {
                        // adds the typed hobby to the list of hobbies
                        addToHobbies(hobbyText)
                    }
                    // clears the text-field
                    onTextChange("")
                }
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        // if the list of hobbies is not empty
        if (hobbies.isNotEmpty()) {
            // header
            Text(text = "Hobbyer:")
            HorizontalDivider(
                modifier = Modifier
                    .height(2.dp)
                    .background(Color.Black)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // flow row with boxes for each hobby
            FlowRow {
                hobbies.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                // removes the hobby when clicking it
                                removeFromHobbies(it)
                            }
                            .background(Color.Red.copy(alpha = 0.5f))
                            .padding(5.dp)
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier
                                .widthIn(max = 150.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "fjern",
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }
    }
}

@Composable
fun InfoBox() {
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
                    "Jeg er basert på OpenAI GPT 4, en kunstig intelligens, som betyr " +
                    "at du bør ta det jeg sier med en klype salt. " +
                    "Ha en fin dag!",
            fontSize = 12.sp
        )
    }
    MrPraktisk(animation = MrPraktiskAnimations.BLINK, generateText = {})
}

@Composable
fun AgeSlider(
    age: Int,
    sliderPosition: Float,
    onSliderChange: (Float) -> Unit,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(10.dp)
    ) {
        // header
        Text(
            text = "Hvor gammel er du?",
            fontSize = 15.sp
        )
        // info
        Text(
            text = "Blir brukt for at Mr. praktisk skal gi deg tilpassede svar",
            fontSize = 10.sp
        )

        // slider for changing age
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
        onValueChange(sliderPosition)
        val text = {
            if (age == 25) "25+ år"
            else age.toString() + " år"
        }
        Text(text = text())

    }
}
