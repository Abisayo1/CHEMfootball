package com.abisayo.chemfootball.displaySS3courses
import androidx.lifecycle.MutableLiveData
import com.abisayo.chemfootball.models.Courses
import com.google.firebase.database.*

class SS3CoursesRepo {

    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("SS3")

    @Volatile private var INSTANCE : SS3CoursesRepo?= null

    fun getInstance() : SS3CoursesRepo {
        return INSTANCE ?: synchronized(this) {

            val instance = SS3CoursesRepo()
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