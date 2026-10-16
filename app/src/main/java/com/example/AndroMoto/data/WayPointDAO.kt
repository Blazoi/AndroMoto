package com.example.AndroMoto.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WayPointDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wayPoint: WayPoint)

    @Update
    suspend fun update(wayPoint: WayPoint)

    @Delete
    suspend fun delete(wayPoint: WayPoint)

    @Query("SELECT * FROM waypoints")
    fun getAll(): Flow<List<WayPoint>>
}