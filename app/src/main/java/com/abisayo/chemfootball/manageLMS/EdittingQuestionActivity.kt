package com.abisayo.chemfootball.manageLMS

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityEdittingQuestionBinding
import com.abisayo.chemfootball.models.Courses
import com.abisayo.chemfootball.models.Questions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EdittingQuestionActivity : AppCompatActivity() {

    var selectedOption = ""
    var selectedOption2 = ""
    var selectedOption3 = ""
    var answer = ""

    private lateinit var database : DatabaseReference
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioGroup2: RadioGroup
    private lateinit var radioGroup3: RadioGroup

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

        radioGroup = findViewById(R.id.radio_group)
        radioGroup2 = findViewById(R.id.radio_group2)
        radioGroup3 = findViewById(R.id.radio_group3)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedOption = selectedRadioButton.text.toString()

        }

        radioGroup2.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedOption2 = selectedRadioButton.text.toString()

        }

        radioGroup3.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedOption3 = selectedRadioButton.text.toString()

        }

        val question = intent.getStringExtra("question")
        val optionA = intent.getStringExtra("option1")
        val optionB = intent.getStringExtra("option2")
        val optionC = intent.getStringExtra("option3")
        val optionD = intent.getStringExtra("option4")
        val answers = intent.getStringExtra("answer")
        val counter = intent.getStringExtra("repeated")
        val timer = intent.getStringExtra("timer")
        val topic = intent.getStringExtra("re")
        val classs = intent.getStringExtra(Constants.CLASS)

        when (classs) {
            "SS1" -> {
                binding.radioOption1.isChecked = true
            }
            "SS2" -> {
                binding.radioOption2.isChecked = true
            }
            "SS3" -> {
                binding.radioOption3.isChecked = true
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
        binding.topic.setText("$topic")


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

        binding.addUpdateBtn.setOnClickListener {

            val answerSpinner = binding.answerSpinner

            val selectedAnswer = answerSpinner.selectedItem as String
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
            deleteData2("$classs", answer)
        }


        when (answers) {
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


    }

    fun saveTopics(clas: String, topic: String, repeated: String, timer: String, option1: String, option2: String, option3: String, option4: String, answer: String) {
        val question = binding.topic.text.toString()
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("$clas")
        val courses = Courses(topic, repeated, timer, clas, "$question", option1, option2, option3, option4, answer)
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
        val topic = binding.topic.text.toString()
        database = FirebaseDatabase.getInstance().getReference("$selectedOption, $topic")
        val Question = Questions(classs, selectedOption2, "$question", "$optionA", "$optionB", "$optionC", "$optionD", answer, selectedOption3)
        database.child("$question").setValue(Question).addOnSuccessListener {
            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            saveTopics("$selectedOption", "$topic", selectedOption2, selectedOption3, "$optionA", "$optionB", "$optionC", "$optionD", "$answer")
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
        val topic = binding.topic.text.toString()

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
}