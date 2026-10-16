package com.example.AndroMoto.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WayPointViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: WayPointDAO = AppDataBase.getDatabase(application.applicationContext).WayPointDAO()

    val wayPoints: StateFlow<List<WayPoint>> = dao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun add(wayPoint: WayPoint) = viewModelScope.launch(Dispatchers.IO) { dao.insert(wayPoint) }
    fun update(wayPoint: WayPoint) = viewModelScope.launch(Dispatchers.IO) { dao.update(wayPoint) }
    fun delete(wayPoint: WayPoint) = viewModelScope.launch(Dispatchers.IO) { dao.delete(wayPoint) }
}