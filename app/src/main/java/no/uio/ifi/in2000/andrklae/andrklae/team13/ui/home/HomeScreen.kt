package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.andrklae.andrklae.team13.R
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.Team13Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        title = {
                        },
                        actions = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                favorittButton()
                                Spacer(modifier = Modifier.weight(1f))
                                mapButton()
                                settingsButton()
                            }


                        }
                    )
                }

            ) { innerPadding ->

                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ){
                    item {
                        mainForecast()
                    }
                }

            }
        }


}

@Preview
@Composable
fun prevHomeScreen(){
    HomeScreen()
}

@Composable
fun favorittButton(){
    IconButton(
        onClick = { /* handle button click */ },
        modifier = Modifier.size(100.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.favoritt),
            contentDescription = "Image Description",

            modifier = Modifier
                .size(40.dp)
                .padding(0.dp)
        )
    }
}

@Composable
fun settingsButton(){
    IconButton(
        onClick = { /* handle button click */ },
        modifier = Modifier.size(100.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = Color.Black
        )
    }
}

@Composable
fun mapButton(){
    IconButton(
        onClick = { /* handle button click */ },
        modifier = Modifier.size(100.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Settings",
            tint = Color.Black
        )
    }
}

@Composable
fun mainForecast(){
    Card(shape = MaterialTheme.shapes.large) {
        Text(text = "25 grader", modifier = Modifier.padding(16.dp))

    }

}


