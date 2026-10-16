package com.example.AndroMoto.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.time.Instant

@Database(
    entities = [WayPoint::class],
    version = 5,
    exportSchema = false
)


abstract class AppDataBase : RoomDatabase() {
    abstract fun WayPointDAO(): WayPointDAO

    companion object {
        @Volatile
        private var Instance: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase =
            Instance ?:synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "waypoints_database"
                ).fallbackToDestructiveMigration()
                    .build().also { Instance = it }
            }
    }
}