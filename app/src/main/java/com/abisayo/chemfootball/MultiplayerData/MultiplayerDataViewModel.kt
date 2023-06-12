package com.abisayo.computerize1.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abisayo.chemfootball.models.Player
import com.abisayo.computerize1.MultiplayerDataRepository

class MultiplayerDataViewModel : ViewModel() {

    private val repository : MultiplayerDataRepository
    private val _allData = MutableLiveData<List<Player>>()
    val allScores : LiveData<List<Player>> = _allData

    init {
        repository = MultiplayerDataRepository().getInstance()
        repository.loadData(_allData)
    }
}
