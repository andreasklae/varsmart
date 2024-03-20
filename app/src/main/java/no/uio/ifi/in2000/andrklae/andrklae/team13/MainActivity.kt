package no.uio.ifi.in2000.andrklae.andrklae.team13
import androidx.lifecycle.observe
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.andrklae.andrklae.team13.TestFiles.MasterRepository
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.MVP
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.ComposeMapDemoMarkers
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map.MapViewModel
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.theme.Team13Theme

class MainActivity : ComponentActivity() {
    val masterRepository = MasterRepository()
    var myVariable = MutableLiveData<String?>()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                masterRepository.getDeviceLocation(fusedLocationProviderClient)
            }
            else{
                println("Permission not granted")
            }
        }

    private fun askPermissions() = when {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            masterRepository.getDeviceLocation(fusedLocationProviderClient)
        }
        else -> {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askPermissions()
        myVariable.observe(this){myVar ->

            myVar?.let{
                setContent {
                    Team13Theme {

                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            // Change to MVP
                            //ComposeMapDemoMarkers(mapViewModel)
                            MVP(masterRepository)
                        }
                    }
                }
            }

        }
        //masterRepository.currentLocation = mapViewModel.state.value.lastKnownLocation
        //masterRepository.printLocation()
        set()
    }
    private fun set(){
        lifecycleScope.launch {
            println("TESTTTTT")
            delay(100)
            myVariable.value = masterRepository.currentLocation.toString()
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Team13Theme {
        Greeting("Android")
    }
}