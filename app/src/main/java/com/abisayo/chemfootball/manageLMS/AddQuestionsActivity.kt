package com.abisayo.chemfootball.manageLMS

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.databinding.ActivityAddQuestionsBinding
import com.abisayo.chemfootball.models.Questions
import com.abisayo.chemfootball.models.Scores
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddQuestionsActivity : AppCompatActivity() {
    var questNum = 0
    private lateinit var database : DatabaseReference
    private lateinit var binding: ActivityAddQuestionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityAddQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val question = binding.questionEditText.text
        val option1 = binding.option1EditText.text
        val option2 = binding.option2EditText.text
        val option3 = binding.option3EditText.text
        val option4 = binding.option4EditText.text
        val answer = binding.answerEditText.text





        // Set a click listener for the update button
        binding.button.setOnClickListener {

            questNum++

            val selectedOption = intent.getStringExtra("selectedOption")
            val topic = intent.getStringExtra("tt")
            val question = question
            val option1 = option1
            val option2 = option2
            val option3 = option3
            val option4 = option4
            val answer = answer
            val  userID = FirebaseAuth.getInstance().currentUser?.uid

            database = FirebaseDatabase.getInstance().getReference("$selectedOption, $topic")
            val Question = Questions(selectedOption, questNum, "$question", "$option1", "$option2", "$option3", "$option4", "$answer")
            if (question.isNotEmpty()) {
                if (option2.isNotEmpty()) {
                    if (userID != null) {
                        if (selectedOption != null) {
                            database.child("$question").setValue(Question).addOnSuccessListener {
                                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
                                question.clear()
                                option1.clear()
                                option2.clear()
                                option3.clear()
                                option4.clear()
                            }.addOnFailureListener {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Please enter option", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter question", Toast.LENGTH_SHORT).show()
            }

        }
    }
}