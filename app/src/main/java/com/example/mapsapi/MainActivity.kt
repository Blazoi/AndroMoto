package com.example.mapsapi

import android.app.Activity
import android.location.Address
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.libraries.navigation.Navigator
import com.example.mapsapi.ui.theme.MapsAPITheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.navigation.NavigationApi
import com.google.android.libraries.navigation.NavigationView
import com.google.android.libraries.navigation.RoutingOptions
import com.google.android.libraries.navigation.SimulationOptions
import com.google.android.libraries.navigation.Waypoint
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
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
            MapsAPITheme {
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
                    destination,
                    {
                        destination = null
                        Log.d("DEBUGGING", "Arrived")
                    }
                )
            }
            Music()
            QuickAccess {
                targetPosition = it
                destination = it
            }
        }
    }
}

@Composable
fun DisplayMap(
    targetPosition: LatLng,
    cameraPosition: CameraPositionState,
    markerState: MarkerState
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(7 / 8f)
            .clip(RoundedCornerShape(10.dp)),
    ) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition
        ) {
            Marker(state = markerState) { }
        }
    }
}

@Composable
fun NavMap(destination: LatLng?, onArrived: () -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity
    val lifecycleOwner = LocalLifecycleOwner.current

    var navigator by remember { mutableStateOf<Navigator?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    val navigationView = remember { NavigationView(context) }

    LaunchedEffect(Unit) {
        NavigationApi.getNavigator(activity, object : NavigationApi.NavigatorListener {
            override fun onNavigatorReady(nav: Navigator) {
                navigator = nav
            }

            override fun onError(errorCode: Int) {
                error = when (errorCode) {
                    NavigationApi.ErrorCode.NOT_AUTHORIZED -> "API key invalid or not authorized for the Navigation SDK."
                    NavigationApi.ErrorCode.TERMS_NOT_ACCEPTED -> "User did not accept the Navigation Terms of Use."
                    NavigationApi.ErrorCode.NETWORK_ERROR -> "Network error."
                    NavigationApi.ErrorCode.LOCATION_PERMISSION_MISSING -> "Location permission is missing."
                    else -> "Error loading Navigation SDK: $errorCode"
                }
            }
        })
    }


    LaunchedEffect(navigator, destination) {
        val nav = navigator ?: return@LaunchedEffect
        val dest = destination ?: return@LaunchedEffect
        startNavigation(nav, dest) { error = it }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> navigationView.onCreate(null)
                Lifecycle.Event.ON_START -> navigationView.onStart()
                Lifecycle.Event.ON_RESUME -> navigationView.onResume()
                Lifecycle.Event.ON_PAUSE -> navigationView.onPause()
                Lifecycle.Event.ON_STOP -> navigationView.onStop()
                Lifecycle.Event.ON_DESTROY -> navigationView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            navigator?.stopGuidance()
            navigator?.clearDestinations()
            onArrived()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(7 / 8f)
            .clip(RoundedCornerShape(10.dp)),
    ) {
        AndroidView(
            factory = { navigationView },
            modifier = Modifier.fillMaxSize()
        )

        error?.let {
            Text(it)
        }
    }

}

fun startNavigation(
    navigator: Navigator,
    target: LatLng,
    onError: (String) -> Unit
) {
    val waypoint = Waypoint.builder()
        .setLatLng(target.latitude, target.longitude)
        .setTitle("Destination")
        .build()

    val routingOptions = RoutingOptions().travelMode(RoutingOptions.TravelMode.DRIVING)

    navigator.setDestination(waypoint, routingOptions)
        .setOnResultListener { status ->
            when (status) {
                Navigator.RouteStatus.OK -> {
                    navigator.startGuidance()
//                    navigator.simulator.simulateLocationsAlongExistingRoute(
//                        SimulationOptions().speedMultiplier(25f)
//                    )
                }

                Navigator.RouteStatus.NO_ROUTE_FOUND -> onError("No route found.")
                Navigator.RouteStatus.NETWORK_ERROR -> onError("Network error.")
                Navigator.RouteStatus.ROUTE_CANCELED -> onError("Route canceled.")
                else -> onError("Could not start navigation: $status")
            }
        }
}

@Composable
fun Music(
    title: String = "Song name",
    artist: String = "Artist",
    progress: Float = 0.1f,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album art
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Album art",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(14.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title + artist
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = artist,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Progress bar
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outlineVariant
                )

                // Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* previous */ }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Previous")
                    }
                    FilledIconButton(onClick = { isPlaying = !isPlaying }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Rounded.PlayArrow else Icons.Rounded.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play"
                        )
                    }
                    IconButton(onClick = { /* next */ }) {
                        Icon(Icons.Rounded.ArrowForward, contentDescription = "Next")
                    }
                }
            }
        }
    }
}

@Composable
fun QuickAccess(navigateTo: (LatLng) -> Unit) {

    val addresses = mapOf(
        "school" to LatLng(45.5381353, -73.6761281),
        "Work" to LatLng(45.4968509, -73.5887969),
        "Home" to LatLng(45.591099, -73.6116298)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(49 / 24f)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        horizontalArrangement = Arrangement.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            addresses.forEach { (name, coords) ->
                item {
                    Card(
                        onClick = {
                            navigateTo(coords)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = when (name.lowercase()) {
                                    "school" -> Icons.Rounded.School
                                    "work" -> Icons.Default.Work
                                    "home" -> Icons.Rounded.Home
                                    else -> Icons.Rounded.Place
                                },
                                contentDescription = null
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = name.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(4) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}