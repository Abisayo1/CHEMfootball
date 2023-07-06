package com.abisayo.chemfootball.displayAvailablePlayers

import androidx.lifecycle.MutableLiveData
import com.abisayo.chemfootball.models.Courses
import com.abisayo.chemfootball.models.Credentials
import com.google.firebase.database.*

class AvailablePlayersRepo {

    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("PlayersCred")

    @Volatile private var INSTANCE : AvailablePlayersRepo?= null

    fun getInstance() : AvailablePlayersRepo {
        return INSTANCE ?: synchronized(this) {

            val instance = AvailablePlayersRepo()
            INSTANCE = instance
            instance
        }

    }

    fun loadPlayers(playersList : MutableLiveData<List<Credentials>>){

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {

                    val _playersList : List<Credentials> = snapshot.children.map { dataSnapshot ->

                        dataSnapshot.getValue(Credentials::class.java)!!

                    }

                    playersList.postValue(_playersList)

                }catch (e: Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}