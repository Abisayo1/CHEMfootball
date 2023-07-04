package com.abisayo.chemfootball.displaySS2Courses

import androidx.lifecycle.MutableLiveData
import com.abisayo.chemfootball.models.Courses
import com.google.firebase.database.*

class SS2CoursesRepo {

    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("SS2")

    @Volatile private var INSTANCE : SS2CoursesRepo?= null

    fun getInstance() : SS2CoursesRepo {
        return INSTANCE ?: synchronized(this) {

            val instance = SS2CoursesRepo()
            INSTANCE = instance
            instance
        }

    }

    fun loadCourses(courseList : MutableLiveData<List<Courses>>){

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {

                    val _courseList : List<Courses> = snapshot.children.map { dataSnapshot ->

                        dataSnapshot.getValue(Courses::class.java)!!

                    }

                    courseList.postValue(_courseList)

                }catch (e: Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}