package com.abisayo.chemfootball.displayAvailablePlayers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abisayo.chemfootball.models.Courses
import com.abisayo.chemfootball.models.Credentials

class AvailablePlayersViewModel : ViewModel() {

    private val repository : AvailablePlayersRepo
    private val _allPlayers = MutableLiveData<List<Credentials>>()
    val allPlayers : LiveData<List<Credentials>> = _allPlayers

    init {
        repository = AvailablePlayersRepo().getInstance()
        repository.loadPlayers(_allPlayers)
    }
}