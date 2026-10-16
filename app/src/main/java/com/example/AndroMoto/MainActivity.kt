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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.AndroMoto.ui.theme.AndroMotoTheme
import com.example.AndroMoto.quickaccess.QuickAccess
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.navigation.Navigator
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        setContent {
            AndroMotoTheme {
                LaunchGate {
                    MainUI()
                }
            }
        }
    }
}

@Composable
fun MainUI() {
    Scaffold { innerPadding ->
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(25.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (false) {
                DisplayMap(targetPosition, cameraPositionState, markerState)
            } else {
                NavMap(
                    navigator,
                    {navigator = it},
                    destination,
                    {
                        destination = null
                        Log.d("DEBUGGING", "Arrived")
                    }
                )
            }
            Music()
            QuickAccess(navigator) {
                targetPosition = it
                destination = it
            }
        }
    }
}