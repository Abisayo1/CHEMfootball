package com.abisayo.computerize1

import androidx.lifecycle.MutableLiveData
import com.abisayo.chemfootball.models.Player
import com.abisayo.chemfootball.models.Scores
import com.google.firebase.database.*

class MultiplayerDataRepository {

    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("Player")

    @Volatile private var INSTANCE : MultiplayerDataRepository?= null

    fun getInstance() : MultiplayerDataRepository {
        return INSTANCE ?: synchronized(this) {

            val instance = MultiplayerDataRepository()
            INSTANCE = instance
            instance
        }

    }

    fun loadData(courseList : MutableLiveData<List<Player>>){

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {

                    val _multiplayerDataList : List<Player> = snapshot.children.map { dataSnapshot ->

                        dataSnapshot.getValue(Player::class.java)!!

                    }

                    courseList.postValue(_multiplayerDataList)

                }catch (e: Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}