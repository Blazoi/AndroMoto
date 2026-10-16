package com.example.AndroMoto.quickaccess.search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.example.AndroMoto.TAG

@Composable
fun PopupBox(
    showPopup: Boolean,
    turnOff: () -> Unit,
    content: @Composable() (turnOff: () -> Unit) -> Unit
) {
    if (showPopup) {
        content({ turnOff() })
    }
}

@Composable
fun AddPopUp(turnOff: () -> Unit = {Log.d(TAG, "Flopped")}) {
    Box(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxSize()
            .background(Color(0, 0, 0, 150))
            .clickable(onClick = { }),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .padding(25.dp)
                .fillMaxWidth()
                .aspectRatio(5f)
                .zIndex(2f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(modifier = Modifier
                .weight(5f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(5.dp))
                .background(Color.Gray)
            )

            Icon(
                imageVector = Icons.Rounded.Navigation,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.secondary),
            )
        }
    }
}

@Composable
fun SearchPopUp() {
    Box(
        modifier = Modifier
            .fillMaxSize(1 / 2f)
            .zIndex(2f)
            .background(Color.Red)
    ){}
}