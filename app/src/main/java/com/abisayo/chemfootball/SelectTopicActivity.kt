package com.abisayo.chemfootball

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.abisayo.chemfootball.SelectPlayers.SelectPlayersActivity
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityEnterNameBinding
import com.abisayo.chemfootball.databinding.ActivitySelectClassBinding
import com.abisayo.chemfootball.databinding.ActivitySelectTopicBinding
import com.abisayo.chemfootball.models.Questions
import com.abisayo.chemfootball.models.SS2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class SelectTopicActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectTopicBinding
    private lateinit var database: DatabaseReference
    var countNum = ""
    var topic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySelectTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val clas = intent.getStringExtra(Constants.CLASS)!!

        val topics = binding.editText.text






        binding.button.setOnClickListener {
                if (topics.isNotEmpty()) {
                    Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show()
                    readUserData()
                    getQuestionsSS2(clas, "$topics")
                    val handler = Handler()
                    val delayMillis = 6000L // 5 seconds
                    handler.postDelayed({
                        Toast.makeText(this, "$topic, $topics $countNum", Toast.LENGTH_SHORT).show()
                        if (topic == "$topics" && countNum == "Once") {
                            Toast.makeText(this, "Access denied", Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(this, SelectPlayersActivity::class.java)
                            intent.putExtra(Constants.CLASS, "$clas")
                            intent.putExtra("re", "$topics")
                            startActivity(intent)
                        }
                    }, delayMillis)

                } else {
                    Toast.makeText(this, "Enter a topic", Toast.LENGTH_SHORT).show()
                }

        }
    }

    private fun getQuestionsSS2(classs: String, topic: String) {
        // Retrieve the "questions" node from the database
        val questionsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("$classs, $topic")

// Initialize an empty array to store the questions
        val questions: ArrayList<Questions> = ArrayList()


// Add a listener to retrieve the questions
        questionsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the existing questions array
                questions.clear()


                // Iterate through the questions in the snapshot
                for (childSnapshot in dataSnapshot.children) {
                    // Get the question data
                    val question = childSnapshot.getValue(Questions::class.java)

                    // Add the question to the array
                    question?.let { questions.add(it) }
                }
               val questionCount = questions.size

                // Call a function to present the questions to the user
                presentQuestionsToUser(questions, questionCount)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        })

    }



    private fun presentQuestionsToUser(questions: ArrayList<Questions>, questionCount: Int) {


        // Get the current question
        val currentQuestion = questions[0]

        var count = currentQuestion.count
        if (count != null) {
            countNum = count
        }



        //Toast.makeText(this, "$count", Toast.LENGTH_SHORT).show()


        // All questions have been answered
        // Handle end of quiz or any other desired action

        // Dismiss the dialog



    }

    private fun readData(classs: String, topics: String) {
        database = FirebaseDatabase.getInstance().getReference("$classs, $topics")
        database.child("question").get().addOnSuccessListener {

            if (it.exists()){

                val count = it.child("count").getValue(String::class.java)!!
                countNum = count

                Toast.makeText(this, "$count", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Successfully Read", Toast.LENGTH_SHORT).show()

            }
            else
            {
                Toast.makeText(this, "User Does not Exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun readUserData() {
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val topics = binding.editText.text
        database = FirebaseDatabase.getInstance().getReference("Scores")


        if (userID != null) {
            database.child("$userID, $topics").get().addOnSuccessListener {
                if (it.exists()) {
                    val courseTitle = it.child("courseTitle").getValue(String::class.java)!!
                    topic = courseTitle

                    Toast.makeText(this, "$courseTitle", Toast.LENGTH_SHORT).show()

                } else {
                    //Toast.makeText(this, "User Does not Exist", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {

                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }
}