package com.shuklansh.mapsalphaproject

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.shuklansh.mapsalphaproject.ui.theme.MapsAlphaProjectTheme
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var ctx = LocalContext.current
            var gc= Geocoder(ctx, Locale.getDefault())
            var keybo = LocalSoftwareKeyboardController.current

            MapsAlphaProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var city by remember { mutableStateOf("") }
                    var latlongstr by remember { mutableStateOf("") }
                    var locationOfUser by remember { mutableStateOf( LatLng(0.0,0.0))}
                    val cameraposState = rememberCameraPositionState{
                        position = CameraPosition.fromLatLngZoom(locationOfUser,10f)
                    }
                    suspend fun CameraPositionState.centerOnLocation(location: LatLng)=animate(
                        update = CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude,location.longitude),
                            15f
                        )
                    )
                    var locationOfUserTitle by remember { mutableStateOf( "not entered")}
                    LaunchedEffect(key1 = locationOfUser ){
                        cameraposState.centerOnLocation(locationOfUser)
                    }

                    Scaffold(
                        Modifier
                            .fillMaxSize()
                        ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(it),
                        horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                            ) {
                            Spacer(modifier = Modifier.height(32.dp))

                            TextField(value = city , maxLines = 1, singleLine = true,onValueChange = {city = it}, keyboardActions = KeyboardActions(
                                onDone = {
                                    var address = gc.getFromLocationName(city,2)
                                    var locationLatLong = address?.get(0)
                                    latlongstr = if(locationLatLong!=null){"your Latitude : "+locationLatLong.latitude.toString() + " " + "Your Longitude" + locationLatLong.longitude.toString()} else {"could not find"}

                                        locationOfUser = LatLng(locationLatLong!!.latitude,locationLatLong.longitude)
                                        locationOfUserTitle = locationLatLong.locality
                                    keybo?.hide()

                                },
                                onGo = {
                                    var address = gc.getFromLocationName(city,2)
                                    var locationLatLong = address?.get(0)
                                    latlongstr = if(locationLatLong!=null){"your Latitude : "+locationLatLong.latitude.toString() + " " + "Your Longitude" + locationLatLong.longitude.toString()} else {"could not find"}

                                        locationOfUser = LatLng(locationLatLong!!.latitude,locationLatLong!!.longitude)
                                        locationOfUserTitle = locationLatLong!!.locality
                                    keybo?.hide()
                                }

                            ) )
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.1f), textAlign = TextAlign.Center , text = latlongstr)
                            Spacer(modifier = Modifier.height(32.dp))

                            GoogleMap(
                                modifier = Modifier.weight(1f),
                                cameraPositionState = cameraposState,
                            ){

                                Marker(
                                    state = MarkerState(position = locationOfUser),
                                    title = locationOfUserTitle,
                                    snippet = "Marker in ${locationOfUserTitle}"
                                )
                            }

                        }
                    }

                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MapsAlphaProjectTheme {
//        Greeting("Android")
//    }
//}