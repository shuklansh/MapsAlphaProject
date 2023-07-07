package com.shuklansh.mapsalphaproject

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            var mapProperties by remember{ mutableStateOf(true) }

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
                    var properties by remember{
                        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
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
                                    if(address?.size!! >0){
                                        var locationLatLong = address?.get(0)
                                        latlongstr = if(locationLatLong!=null){"Your Latitude : "+locationLatLong.latitude.toString() + " " + "Your Longitude" + locationLatLong.longitude.toString()} else {"could not find"}

                                        locationOfUser = LatLng(locationLatLong!!.latitude,locationLatLong.longitude)
                                        locationOfUserTitle = if(locationLatLong.locality==null){"no locality"}else{locationLatLong.locality}
                                        keybo?.hide()
                                    }else{
                                        Toast.makeText(ctx,"Enter a valid location",Toast.LENGTH_SHORT).show()
                                    }

                                }

                            ),colors = TextFieldDefaults.textFieldColors(
                                //backgroundColor = Color(204, 204, 204, 255),
                                cursorColor = Color.Black,
                                //textColor = Color.Black,
                                disabledLabelColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                                shape = RoundedCornerShape(24.dp), )
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.1f), textAlign = TextAlign.Center , text = latlongstr)
                            Spacer(modifier = Modifier.height(22.dp))
                            Box(modifier = Modifier.weight(1f)){
                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = cameraposState,
                                    properties = properties
                                ){

                                    Marker(
                                        state = MarkerState(position = locationOfUser),
                                        title = locationOfUserTitle,
                                        snippet = "Marker in ${locationOfUserTitle}"
                                    )
                                }
                                Switch(modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(horizontal = 12.dp),checked = mapProperties, onCheckedChange = {if(mapProperties){
                                    properties = MapProperties(mapType = MapType.SATELLITE)
                                    mapProperties = !mapProperties
                                }else{
                                    properties = MapProperties(mapType = MapType.NORMAL)
                                    mapProperties = !mapProperties
                                } } )
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