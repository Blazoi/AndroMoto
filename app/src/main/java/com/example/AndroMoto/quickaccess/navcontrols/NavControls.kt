package com.example.AndroMoto.quickaccess.navcontrols

import android.graphics.Color
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.libraries.navigation.Navigator

@Composable
fun NavControls(
    audioGuidance: Int,
    turnVoiceOn: () -> Unit,
    turnVoiceOff: () -> Unit,
    endRoute: () -> Unit,
) {
    var isVoiceOn by remember { mutableStateOf(audioGuidance == Navigator.AudioGuidance.VOICE_ALERTS_AND_GUIDANCE) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            onClick = {
                if (isVoiceOn) {
                    turnVoiceOff()
                    isVoiceOn = false
                } else {
                    turnVoiceOn()
                    isVoiceOn = true
                }
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .aspectRatio(1f),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = when (isVoiceOn) {
                        true -> Icons.Rounded.VolumeOff
                        else -> Icons.Rounded.VolumeUp
                    },
                    modifier = Modifier
                        .size(40.dp),
                    contentDescription = null
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = if (isVoiceOn) "Mute" else "Voice On",
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    fontSize = 20.sp
                )
            }
        }

        Card(
            onClick = { endRoute() },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .aspectRatio(1f),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.Cancel,
                    modifier = Modifier
                        .size(40.dp),
                    contentDescription = null
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "End Trip",
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    fontSize = 20.sp
                )
            }
        }
    }
}