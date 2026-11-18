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
import com.example.AndroMoto.data.WayPoint
import com.example.AndroMoto.data.WayPointViewModel
import com.example.AndroMoto.music.SpotifyManager
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlin.getValue

public val TAG = "DEBUGGING"

class MainActivity : ComponentActivity() {

    val wayPointViewModel: WayPointViewModel by viewModels()
    private val spotify = SpotifyManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // makes app accessible when closed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        Places.initializeWithNewPlacesApiEnabled(this, BuildConfig.MAPS_API_KEY)

        setContent {
            AndroMotoTheme {
                MainUI(wayPointViewModel, spotify)
            }
        }
    }

    private var auth = false

    override fun onStart() {
        super.onStart()
        if (!auth) {
            auth = true
            authorizeSpotify()
        }
    }

    override fun onStop() {
        super.onStop()
        spotify.disconnect()
    }

    private val authLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val response = AuthorizationClient.getResponse(result.resultCode, result.data)
        if (response.type == AuthorizationResponse.Type.TOKEN) {
            spotify.connect(this)
        } else {
            Log.e("SPOTIFY", "Auth result: ${response.type} ${response.error}")
        }
    }

    private fun authorizeSpotify() {
        val request = AuthorizationRequest.Builder(
            BuildConfig.SPOTIFY_CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            "andromoto://callback"
        )
            .setScopes(arrayOf("app-remote-control"))
            .build()
        authLauncher.launch(AuthorizationClient.createLoginActivityIntent(this, request))
    }
}



@Composable
fun MainUI(wayPointViewModel: WayPointViewModel, spotify: SpotifyManager) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
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

                Music(spotify)
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