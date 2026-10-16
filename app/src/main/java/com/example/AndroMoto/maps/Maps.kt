package com.example.AndroMoto.maps

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.navigation.NavigationApi
import com.google.android.libraries.navigation.NavigationView
import com.google.android.libraries.navigation.Navigator
import com.google.android.libraries.navigation.RoutingOptions
import com.google.android.libraries.navigation.Waypoint
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

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
fun NavMap(
    navigator: Navigator?,
    setNavigator: (Navigator) -> Unit,
    destination: LatLng?,
    onArrived: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val lifecycleOwner = LocalLifecycleOwner.current

    var error by remember { mutableStateOf<String?>(null) }

    val navigationView = remember { NavigationView(context) }

    LaunchedEffect(Unit) {
        NavigationApi.getNavigator(activity, object : NavigationApi.NavigatorListener {
            override fun onNavigatorReady(nav: Navigator) {
                setNavigator(nav)
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