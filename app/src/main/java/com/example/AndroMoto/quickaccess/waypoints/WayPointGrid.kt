package com.example.AndroMoto.quickaccess.waypoints

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalGasStation
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.AndroMoto.data.WayPointViewModel
import com.google.android.gms.maps.model.LatLng
import kotlin.collections.forEach
import androidx.compose.runtime.collectAsState
import com.example.AndroMoto.data.WayPoint

@Composable
fun WayPointsGrid(wayPointViewModel: WayPointViewModel, navigateTo: (LatLng) -> Unit) {

    val icons = arrayOf(
        Icons.Rounded.Place,
        Icons.Rounded.Home,
        Icons.Rounded.School,
        Icons.Rounded.Work,
        Icons.Rounded.Fastfood,
        Icons.Rounded.LocalGasStation
    )

    val waypoints: List<WayPoint> = wayPointViewModel.wayPoints.collectAsState().value

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f),
        contentPadding = PaddingValues(8.dp),
        userScrollEnabled = true,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        waypoints.forEach { (name, coordinates, address, icon, id) ->
            val latitude = coordinates.split(",")[0].trim().toDouble()
            val longitude = coordinates.split(",")[1].trim().toDouble()

            item {
                Card(
                    onClick = {
                        navigateTo(LatLng(latitude, longitude))
                        Log.d("DEBUGGING", "Clicked on $name")
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
                            imageVector = icons[icon],
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
}