package com.example.AndroMoto

import com.example.AndroMoto.music.Music
import com.example.AndroMoto.maps.NavMap
import com.example.AndroMoto.maps.DisplayMap
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.android.volley.BuildConfig
import com.example.AndroMoto.data.WayPoint
import com.example.AndroMoto.data.WayPointViewModel
import com.example.AndroMoto.ui.theme.AndroMotoTheme
import com.example.AndroMoto.quickaccess.QuickAccess
import com.example.AndroMoto.quickaccess.popups.AddPopUP
import com.example.AndroMoto.quickaccess.popups.SearchPopUP
//import com.example.AndroMoto.quickaccess.search.AddPopUp
//import com.example.AndroMoto.quickaccess.search.PopupBox
//import com.example.AndroMoto.quickaccess.search.SearchPopUp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.navigation.Navigator
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlin.getValue

public val TAG = "DEBUGGING"

class MainActivity : ComponentActivity() {

    val wayPointViewModel: WayPointViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // makes app accessible when closed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        Places.initializeWithNewPlacesApiEnabled(this, "AIzaSyAjJHzWJE8b4AUQWVROz-ucd2vDDDGyMhg")

        setContent {
            AndroMotoTheme {
                MainUI(wayPointViewModel)
            }
        }
    }
}

@Composable
fun MainUI(wayPointViewModel: WayPointViewModel) {

    Scaffold { innerPadding ->
        var isAddingVisible by remember { mutableStateOf(false) }
        var isSearchingVisible by remember { mutableStateOf(false) }

        var navigator by remember { mutableStateOf<Navigator?>(null) }

        var destination by remember { mutableStateOf<LatLng?>(null) }
        var targetPosition by remember { mutableStateOf(LatLng(45.5088, -73.5878)) }
        val markerState = rememberUpdatedMarkerState(position = targetPosition)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(targetPosition, 10f)
        }

        LaunchedEffect(targetPosition) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(targetPosition, 13f)
            )
        }

        if (isAddingVisible) {
            AddPopUP(wayPointViewModel) { isAddingVisible = false }
        }
        if (isSearchingVisible) {
            SearchPopUP(
                { isSearchingVisible = false },
                { destination = it })
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LaunchGate(
                    {
                        NavMap(
                            navigator,
                            { navigator = it },
                            destination
                        )
                    },
                    {
                        DisplayMap(
                            targetPosition,
                            cameraPositionState,
                            markerState
                        )
                    }

                )

                Music()
                QuickAccess(
                    wayPointViewModel,
                    navigator,
                    {
                        targetPosition = it
                        destination = it
                    },
                    {
                        if (it == "add") {
                            isAddingVisible = !isAddingVisible
                        } else {
                            isSearchingVisible = !isSearchingVisible
                        }
                    }
                )
            }
        }
    }
}