package com.example.AndroMoto.quickaccess

import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.AndroMoto.data.WayPointViewModel
import com.example.AndroMoto.quickaccess.navcontrols.NavControls
import com.example.AndroMoto.quickaccess.search.SearchBox
import com.example.AndroMoto.quickaccess.waypoints.WayPointsGrid
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.navigation.Navigator

@Composable
fun QuickAccess(
    wayPointViewModel: WayPointViewModel,
    navigator: Navigator?,
    navigateTo: (LatLng) -> Unit,
    togglePopUp: (String) -> Unit
) {
    var currentAudioGuidance = 0;
    val pagerState = rememberPagerState(pageCount = { 2 })

    HorizontalPager(
        state = pagerState,
        pageSpacing = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(49 / 24f)
    ) { page ->
        when (page) {
            0 -> {
                WayPoints(
                    wayPointViewModel,
                    { navigateTo(it) },
                    togglePopUp
                )
            }

            1 -> {
                NavControls(
                    currentAudioGuidance,
                    {
                        currentAudioGuidance = Navigator.AudioGuidance.VOICE_ALERTS_AND_GUIDANCE
                        navigator?.setAudioGuidance(currentAudioGuidance)
                    },
                    {
                        currentAudioGuidance = Navigator.AudioGuidance.SILENT
                        navigator?.setAudioGuidance(currentAudioGuidance)
                    },
                    {
                        navigator?.stopGuidance()
                    }
                )
            }
        }
    }
}

@Composable
fun WayPoints(
    wayPointViewModel: WayPointViewModel,
    navigateTo: (LatLng) -> Unit,
    togglePopUp: (String) -> Unit
) {

    val addresses = mapOf(
        "school" to LatLng(45.5381353, -73.6761281),
        "Work" to LatLng(45.4968509, -73.5887969),
        "Home" to LatLng(45.591099, -73.6116298)
    )

    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        horizontalArrangement = Arrangement.Center
    ) {
        WayPointsGrid(wayPointViewModel) { navigateTo(it) }
        SearchBox(togglePopUp)
    }
}