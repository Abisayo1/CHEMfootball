package com.abisayo.chemfootball.manageLMS

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityEdittingQuestionBinding
import com.abisayo.chemfootball.models.Courses
import com.abisayo.chemfootball.models.Questions
import com.abisayo.chemfootball.models.SS2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class EdittingQuestionActivity : AppCompatActivity() {

    var selectedOption = ""
    var selectedOption2 = ""
    var selectedOption3 = ""
    var selectedOption4 = ""
    var answer = ""
    var questionCount = 200000
    var topic = ""
    var restricted = ""
    var v = 0

    private lateinit var database : DatabaseReference
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioGroup2: RadioGroup
    private lateinit var radioGroup3: RadioGroup
    private lateinit var radioGroup4: RadioGroup

    var currentQuestionIndex = 0

    private lateinit var binding: ActivityEdittingQuestionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityEdittingQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)




        radioGroup2 = findViewById(R.id.radio_group2)
        radioGroup3 = findViewById(R.id.radio_group3)
        radioGroup4 = findViewById(R.id.radio_groupres)



        radioGroup2.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedOption2 = selectedRadioButton.text.toString()

        }

        radioGroup3.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedOption3 = selectedRadioButton.text.toString()

        }

        radioGroup4.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedOption4 = selectedRadioButton.text.toString()
        }

        val question = intent.getStringExtra("question")
        val optionA = intent.getStringExtra("option1")
        val optionB = intent.getStringExtra("option2")
        val optionC = intent.getStringExtra("option3")
        val optionD = intent.getStringExtra("option4")
        val answers = intent.getStringExtra("answer")
        val counter = intent.getStringExtra("repeated")
        val timer = intent.getStringExtra("timer")
        topic = intent.getStringExtra("re").toString()
        val classs = intent.getStringExtra(Constants.CLASS)
        restricted = intent.getStringExtra("restricted").toString()

        binding.topic12.setText("$topic")


        val answerSpinner = binding.answerSpinner
        val selectedAnswer = answerSpinner.selectedItem as String
        val items = resources.getStringArray(R.array.answer_options)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        answerSpinner.adapter = adapter

        if (classs != null) {
            getQuestions(classs, topic)
        }

        binding.addUpdateBtn.setOnClickListener {
            if (v ==0 ) {
                currentQuestionIndex++
            }
            if (classs != null) {
                val ans = currentQuestionIndex+1
                if (questionCount == ans) {
                    v = 2
                    Toast.makeText(this, "You have reached the end", Toast.LENGTH_SHORT).show()
                }else {
                    getQuestions(classs, topic)
                }
            }
        }


    }

    fun saveTopics(clas: String, topic: String, repeated: String, timer: String, option1: String, option2: String, option3: String, option4: String, answer: String, restricted: String) {
        val question = binding.questionn.text.toString()
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("$clas")
        val courses = Courses(topic, repeated, timer, clas, "$question", option1, option2, option3, option4, answer, restricted)
        if (userID != null) {
            database.child(topic).setValue(courses).addOnSuccessListener {
                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun launch(classs: String, answer: String) {
        val question = binding.questionn.text.toString()
        val optionA = binding.editNoteOptionA.text.toString()
        val optionB = binding.editNoteOptionB.text.toString()
        val optionC = binding.editNoteOptionC.text.toString()
        val optionD = binding.editNoteOptionD.text.toString()

        database = FirebaseDatabase.getInstance().getReference("$selectedOption, $topic")
        val Question = Questions(classs, selectedOption2, "$question", "$optionA", "$optionB", "$optionC", "$optionD", answer, selectedOption3)
        database.child("$question").setValue(Question).addOnSuccessListener {
            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            saveTopics("$selectedOption", "$topic", selectedOption2, selectedOption3, "$optionA", "$optionB", "$optionC", "$optionD", "$answer", selectedOption4)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteData2(classs: String, answer: String) {
        val question = binding.questionn.text.toString()
        val optionA = binding.editNoteOptionA.text.toString()
        val optionB = binding.editNoteOptionB.text.toString()
        val optionC = binding.editNoteOptionC.text.toString()
        val optionD = binding.editNoteOptionD.text.toString()

        var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("$selectedOption, $topic")
        database.child("$question").removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Successfuly Deleted", Toast.LENGTH_SHORT).show()
                launch("$classs", answer)

            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getQuestions(classs: String, topic: String) {
        // Retrieve the "questions" node from the database
        val questionsRef: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("$classs, $topic")

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
                questionCount = questions.size

                // Call a function to present the questions to the user
                presentQuestionsToUser(questions, questionCount)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        })

        // Define a data class to represent a question
        data class Question(val text: String, val options: List<String>)
    }


    private fun presentQuestionsToUser(questions: ArrayList<Questions>, questionCount: Int) {
        // Get the current question
        val currentQuestion = questions[currentQuestionIndex]

        val answer = currentQuestion.answer
        val counter = currentQuestion.count
        val optionA = currentQuestion.option1
        val optionB = currentQuestion.option2
        val optionC = currentQuestion.option3
        val optionD = currentQuestion.option4
        val question = currentQuestion.question
        val restricted = "$restricted"
        val timer = currentQuestion.timer


        when (restricted) {
            "Yes" -> {
                binding.Yes.isChecked = true
            }
            "No" -> {
                binding.No.isChecked = true
            }
            "" -> {
                binding.No.isChecked = true
            }
        }

        when (timer) {
            "30 seconds" -> {
                binding.radioOption13.isChecked = true
            }
            "1 minute" -> {
                binding.radioOption23.isChecked = true
            }
            "2 minutes" -> {
                binding.radioOption33.isChecked = true
            }
            "3 minutes" -> {
                binding.radioOption43.isChecked = true
            }
        }

        if (counter == "Once") {
            binding.radioOption12.isChecked = true
        } else if (counter == "Repeated") {
            binding.radioOption22.isChecked = true
        }

        binding.questionn.setText("$question")
        binding.editNoteOptionA.setText("$optionA")
        binding.editNoteOptionB.setText("$optionB")
        binding.editNoteOptionC.setText("$optionC")
        binding.editNoteOptionD.setText("$optionD")



        val answerSpinner = binding.answerSpinner
        val selectedAnswer = answerSpinner.selectedItem as String
        val items = resources.getStringArray(R.array.answer_options)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        answerSpinner.adapter = adapter

        val option1 = binding.editNoteOptionA.text
        val option2 = binding.editNoteOptionB.text
        val option3 = binding.editNoteOptionC.text
        val option4 = binding.editNoteOptionD.text



        when (answer) {
            "$optionA" -> {
                val desiredIndex = 1
                answerSpinner.setSelection(desiredIndex)
            }
            "$optionB" -> {
                val desiredIndex = 2
                answerSpinner.setSelection(desiredIndex)
            }
            "$optionC" -> {
                val desiredIndex = 3
                answerSpinner.setSelection(desiredIndex)
            }
            "$optionD" -> {
                val desiredIndex = 4
                answerSpinner.setSelection(desiredIndex)
            }
            "Selected Answer" -> {
                Toast.makeText(this, "What is the answer for this question?", Toast.LENGTH_SHORT)
            }
        }



        // Inflate the dialog box with the question and options
        // You can use a custom dialog or an AlertDialog for this

// Set the question and options in the dialog


// Store the correct answer
        if (currentQuestionIndex + 1 > questionCount) {
            Toast.makeText(this, "You have reached the end of the game", Toast.LENGTH_SHORT).show()
        } else {
            if (currentQuestionIndex < questionCount) {

            }
        }


        // All questions have been answered
        // Handle end of quiz or any other desired action

        // Dismiss the dialog


// Set click listeners for the option buttons




                    // Handle option selection here
                    // You can check if the selected option is correct and perform any necessary actions

                    // Check if the selected option is the correct answer

            }


        }


