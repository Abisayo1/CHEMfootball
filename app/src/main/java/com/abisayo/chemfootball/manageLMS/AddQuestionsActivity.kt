package com.abisayo.chemfootball.manageLMS

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.abisayo.chemfootball.databinding.ActivityAddQuestionsBinding
import com.abisayo.chemfootball.models.Courses
import com.abisayo.chemfootball.models.Questions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddQuestionsActivity : AppCompatActivity() {
    var questNum = "0"
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




        // Set a click listener for the update button
        binding.button.setOnClickListener {
            val answerSpinner = binding.answerSpinner

            val selectedAnswer = answerSpinner.selectedItem as String
            val selectedOption = intent.getStringExtra("selectedOption")
            val selectedOption2 = intent.getStringExtra("selectedOption2")
            val selectedoption3 = intent.getStringExtra("selectedOption3")
            val topic = intent.getStringExtra("tt")
            val question = question
            val option1 = option1
            val option2 = option2
            val option3 = option3
            val option4 = option4
            var answer = ""
            when (selectedAnswer) {
                "Option A" -> {
                    answer = "$option1"
                }
                "Option B" -> {
                    answer = "$option2"
                }
                "Option C" -> {
                    answer = "$option3"

                }
                "Option D" -> {
                    answer = "$option4"

                }

                "Select Answer" -> {
                    answer = "null"
                }

            }
            val  userID = FirebaseAuth.getInstance().currentUser?.uid

            database = FirebaseDatabase.getInstance().getReference("$selectedOption, $topic")
            val Question = Questions(selectedOption, selectedOption2, "$question", "$option1", "$option2", "$option3", "$option4",
                answer, selectedoption3
            )
            if (binding.questionEditText.text.isNotEmpty()) {
                if (binding.option2EditText.text.isNotEmpty() && answer != "null") {
                    if (userID != null) {
                        if (selectedOption != null) {
                            database.child("$question").setValue(Question).addOnSuccessListener {
                                if (selectedOption2 != null) {
                                    if (selectedoption3 != null) {
                                        saveTopics("$selectedOption", "$topic", selectedOption2, selectedoption3)
                                    }
                                }
                                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
                                binding.questionEditText.text.clear()
                                binding.option1EditText.text.clear()
                                binding.option2EditText.text.clear()
                                binding.option3EditText.text.clear()
                                binding.option4EditText.text.clear()
                            }.addOnFailureListener {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Please enter option or select a valid answer", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter question", Toast.LENGTH_SHORT).show()
            }

        }
    }


    fun saveTopics(clas: String, topic: String, repeated: String, timer: String) {
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("$clas")
        val courses = Courses(topic, repeated, timer)
        if (userID != null) {
            database.child(topic).setValue(courses).addOnSuccessListener {
                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}