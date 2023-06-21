package com.abisayo.chemfootball.manageLMS

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.abisayo.chemfootball.R

class SelectQuestionClassActivity : AppCompatActivity() {
    private lateinit var radioGroup: RadioGroup
    private lateinit var nextButton: Button
    private lateinit var editText: EditText

    var selectedOption = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_question_class)

        radioGroup = findViewById(R.id.radio_group)
        nextButton = findViewById(R.id.button)

        editText = findViewById(R.id.questionEditText)

        val topic = editText.text

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedOption = selectedRadioButton.text.toString()

            nextButton.isEnabled = true


        }


       nextButton.setOnClickListener {
           val intent = Intent(this, AddQuestionsActivity::class.java)
           intent.putExtra("selectedOption", selectedOption)
           intent.putExtra("tt", "$topic")
           startActivity(intent)
       }
    }
}

