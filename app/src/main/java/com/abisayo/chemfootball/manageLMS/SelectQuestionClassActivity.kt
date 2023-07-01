package com.abisayo.chemfootball.manageLMS

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.models.Scores
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SelectQuestionClassActivity : AppCompatActivity() {
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioGroup2: RadioGroup
    private lateinit var nextButton: Button
    private lateinit var editText: EditText

    var selectedOption = ""
    var selectedOption2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_question_class)

        radioGroup = findViewById(R.id.radio_group)
        nextButton = findViewById(R.id.button)
        radioGroup2 = findViewById(R.id.radio_group2)

        editText = findViewById(R.id.questionEditText)

        val topic = editText.text

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedOption = selectedRadioButton.text.toString()

            nextButton.isEnabled = true


        }

        radioGroup2.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedOption2 = selectedRadioButton.text.toString()

            nextButton.isEnabled = true


        }




       nextButton.setOnClickListener {
           if (topic.isNotEmpty()) {
               val intent = Intent(this, AddQuestionsActivity::class.java)
               intent.putExtra("selectedOption", selectedOption)
               intent.putExtra("selectedOption2", selectedOption2)
               intent.putExtra("tt", "$topic")
               startActivity(intent)
           } else {
               Toast.makeText(this, "Please enter a topic", Toast.LENGTH_SHORT).show()
           }
       }
    }

}

