package com.example.AndroMoto.quickaccess.popups

import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.AndroMoto.TAG
import com.example.AndroMoto.data.WayPoint
import com.example.AndroMoto.data.WayPointViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AddPopUP(
    wayPointViewModel: WayPointViewModel,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        modifier = Modifier
            .height(400.dp),
        shape = RoundedCornerShape(28.dp),

        title = {
            Text(
                text = "Add to quick access",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        },

        text = {
            FindAddress(
                onDismiss
            ) { wayPoint, latLong ->
                wayPointViewModel.add( wayPoint )
            }
        }
    )
}

@Composable
fun SearchPopUP(
    onDismiss: () -> Unit,
    navigateTo: (LatLng) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        modifier = Modifier.height(400.dp),
        shape = RoundedCornerShape(28.dp),

        title = {
            Text(
                text = "Search",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        },

        text = {
            FindAddress(
                onDismiss
            ) { wayPoint, latLong ->
                navigateTo( latLong ?: LatLng(0.0, 0.0) )
            }
        }
    )
}

@Composable
fun FindAddress(
    closePopUp: () -> Unit,
    action: (
        waypoint: WayPoint,
        latLong: LatLng?,
    ) -> Unit
) {

    val context = LocalContext.current
    val placesClient = remember { Places.createClient(context) }
    var sessionToken by remember { mutableStateOf(AutocompleteSessionToken.newInstance()) }

    var query by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var skipNextSearch by remember { mutableStateOf(false) }

    val geocoder = Geocoder(context, Locale.getDefault())
    val kbController = LocalSoftwareKeyboardController.current

    LaunchedEffect(query) {
        if (skipNextSearch) {
            skipNextSearch = false
            return@LaunchedEffect
        }

        if (query.length < 3) {
            predictions = emptyList()
            return@LaunchedEffect
        }

        delay(300.milliseconds)
        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(sessionToken)
            .setQuery(query)
            .setCountries("CA")
            .build()

        predictions = try {
            placesClient.findAutocompletePredictions(request)
                .await()
                .autocompletePredictions
        } catch (e: ApiException) {
            emptyList()
        }

    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search for an address") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = true
        ) {
            predictions.forEach { prediction ->

                val address = prediction.getPrimaryText(null).toString()
                val secondary = prediction.getSecondaryText(null).toString()
                var latitude = ""
                var longitude = ""

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .clickable {
                                kbController?.hide()
                                closePopUp()

                                try {
                                    val output = geocoder.getFromLocationName(address, 1)
                                    if (!output.isNullOrEmpty()) {
                                        latitude = output[0].latitude.toString().take(8)
                                        longitude = output[0].longitude.toString().take(8)
                                    }
                                } catch (e: Exception) {
                                    Log.d(TAG, e.toString())
                                }

                                action(
                                    WayPoint(
                                        prediction.getPrimaryText(null).toString(),
                                        "$latitude,$longitude",
                                        address
                                    ),
                                    LatLng(latitude.toDouble(), longitude.toDouble()),
                                )
                            }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )

                        Column(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = address,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (secondary.isNotEmpty()) {
                                Text(
                                    text = secondary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}