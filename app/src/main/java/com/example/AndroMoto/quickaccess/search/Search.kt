package com.example.AndroMoto.quickaccess.search

import android.service.autofill.OnClickAction
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SearchBox(togglePopUp: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Add(Modifier.weight(1f), togglePopUp)
            Search(Modifier.weight(1f), togglePopUp)
        }

    }
}

@Composable
fun Add(modifier: Modifier, togglePopUp: (String) -> Unit) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.onBackground)
            .clickable(onClick = {
                togglePopUp("add")
            }),
        contentAlignment = Alignment.Center
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(
                imageVector = Icons.Rounded.Add,
                tint = MaterialTheme.colorScheme.background,
                contentDescription = ""
            )
            Text(
                "Add",
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun Search(modifier: Modifier, togglePopUp: (String) -> Unit) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .clickable(onClick = {
                togglePopUp("search")
            }),
        contentAlignment = Alignment.Center
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(
                imageVector = Icons.Rounded.Search,
                tint = MaterialTheme.colorScheme.onSecondary,
                contentDescription = ""
            )
            Text(
                "Search",
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}