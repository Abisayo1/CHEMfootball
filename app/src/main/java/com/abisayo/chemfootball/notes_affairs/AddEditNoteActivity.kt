package com.abisayo.chemfootball.notes_affairs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.databinding.ActivityAddEditNoteBinding
import com.abisayo.chemfootball.models.Courses
import com.abisayo.chemfootball.models.Questions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditNoteBinding
    lateinit var viewModel: NoteViewModel
    var noteID = -1
    private lateinit var database : DatabaseReference
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioGroup2: RadioGroup
    private lateinit var radioGroup3: RadioGroup

    var selectedOption = ""
    var selectedOption2 = ""
    var selectedOption3 = ""
    var answer = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

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

        val noteType = intent.getStringExtra("noteType")

        if (noteType.equals("Edit")) {

            val question = intent.getStringExtra("question")
            val optionA = intent.getStringExtra("optionA")
            val optionB = intent.getStringExtra("optionB")
            val optionC = intent.getStringExtra("optionC")
            val optionD = intent.getStringExtra("optionD")
            val answers = intent.getStringExtra("answer")
            val counter = intent.getStringExtra("count")
            val timer = intent.getStringExtra("timer")
            val topic = intent.getStringExtra("noteTitle")
            val classs = intent.getStringExtra("noteDescription")
            noteID = intent.getIntExtra("noteID", -1)

            val answerSpinner = binding.answerSpinner
            val selectedAnswer = answerSpinner.selectedItem as String
            val items = resources.getStringArray(R.array.answer_options)

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            answerSpinner.adapter = adapter


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



            binding.questionn.setText(question)
            binding.editNoteOptionA.setText(optionA)
            binding.editNoteOptionB.setText(optionB)
            binding.editNoteOptionC.setText(optionC)
            binding.editNoteOptionD.setText(optionD)


            binding.topic.setText(topic)
            if (classs == "SS1"){
                binding.radioOption1.isChecked = true
            } else if (classs == "SS2") {
                binding.radioOption2.isChecked = true
            } else if (classs == "SS3") {
                binding.radioOption3.isChecked = true
            }

            if (counter == "Once") {
                binding.radioOption12.isChecked = true
            } else if (counter == "Repeated") {
                binding.radioOption22.isChecked = true
            }

            if (timer == "30 seconds") {
                binding.radioOption13.isChecked = true
            } else if (timer == "1 minute") {
                binding.radioOption23.isChecked = true
            } else if (timer == "2 minutes") {
                binding.radioOption33.isChecked = true
            } else if (timer == "3 minutes") {
                binding.radioOption43.isChecked = true
            }

        } else {
            binding.addUpdateBtn.setText("Save Course")
        }

        binding.addUpdateBtn.setOnClickListener {
            val answerSpinner = binding.answerSpinner
            val selectedAnswer = answerSpinner.selectedItem as String
            val topic = binding.topic.text.toString()
            val classs = selectedOption
            val question = binding.topic.text.toString()
            val optionA = binding.editNoteOptionA.text.toString()
            val optionB = binding.editNoteOptionB.text.toString()
            val optionC = binding.editNoteOptionC.text.toString()
            val optionD = binding.editNoteOptionD.text.toString()


            when (selectedAnswer) {
                "Option A" -> {
                    answer = "$optionA"
                }
                "Option B" -> {
                    answer = "$optionB"
                }
                "Option C" -> {
                    answer = "$optionC"

                }
                "Option D" -> {
                    answer = "$optionD"

                }

                "Select Answer" -> {
                    answer = "null"
                }

            }


            if (noteType.equals("Edit")) {
                if (binding.topic.text?.isNotEmpty() == true && binding.questionn.text.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())
                    val updateNote = Note(question, topic, classs, optionA, optionB, optionC, optionD,answer, selectedOption2, selectedOption3)
                    updateNote.id = noteID
                    viewModel.update(updateNote)
                    Toast.makeText(this, "Course Updated..", Toast.LENGTH_LONG)
                }
            }else {
                if (binding.topic.text?.isNotEmpty() == true && binding.questionn.text.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())
                    viewModel.addNote(Note(question, topic, classs, optionA, optionB, optionC, optionD,answer, selectedOption2, selectedOption3))
                    Toast.makeText(this, "Course Added.. ", Toast.LENGTH_LONG)


                }
            }
            startActivity(Intent(applicationContext, NoteActivity::class.java))
            this.finish()
        }

        binding.button2.setOnClickListener {
            val answerSpinner = binding.answerSpinner
            val selectedAnswer = answerSpinner.selectedItem as String
            val topic = binding.topic.text.toString()
            val classs = selectedOption
            val question = binding.topic.text.toString()
            val optionA = binding.editNoteOptionA.text.toString()
            val optionB = binding.editNoteOptionB.text.toString()
            val optionC = binding.editNoteOptionC.text.toString()
            val optionD = binding.editNoteOptionD.text.toString()


            when (selectedAnswer) {
                "Option A" -> {
                    answer = "$optionA"
                }
                "Option B" -> {
                    answer = "$optionB"
                }
                "Option C" -> {
                    answer = "$optionC"

                }
                "Option D" -> {
                    answer = "$optionD"

                }

                "Select Answer" -> {
                    answer = "null"
                }

            }
            launch("$topic", "$classs", "$question", "$optionA", "$optionB", "$optionC", "$optionD")
        }
    }

    fun saveTopics(clas: String, topic: String, repeated: String, timer: String) {
        val question = binding.topic.text.toString()
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("$clas")
        val courses = Courses(topic, repeated, timer, clas, "$question")
        if (userID != null) {
            database.child(topic).setValue(courses).addOnSuccessListener {
                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun launch(topic: String, classs: String, question: String, optionA: String, optionB: String, optionC : String, optionD: String) {
        database = FirebaseDatabase.getInstance().getReference("$selectedOption, $topic")
        val Question = Questions(classs, selectedOption2, question, "$optionA", "$optionB", "$optionC", "$optionD", answer, selectedOption3)
        database.child("$question").setValue(Question).addOnSuccessListener {
            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            saveTopics("$selectedOption", "$topic", selectedOption2, selectedOption3)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }
}