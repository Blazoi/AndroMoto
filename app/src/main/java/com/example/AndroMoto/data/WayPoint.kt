package com.example.AndroMoto.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Place
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "waypoints")
data class WayPoint (
    var name: String,
    var coords: String,
    var address: String = "",
    var icon: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)