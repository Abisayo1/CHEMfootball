package com.abisayo.chemfootball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityDisplayAnswersBinding
import com.abisayo.chemfootball.models.*
import com.google.firebase.database.*
import java.util.*


class DisplayAnswersActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    var mMediaPlayer: MediaPlayer? = null
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityDisplayAnswersBinding
    var score = 0
    var trial = 0
    private var clas = "SS1"
    private var scoreC = 0
    private var player = ""
    var mydialog: Dialog? = null
    var oppPlayer = "Fragment6"
    var oppScoreStatus = "nil"
    var hasVideoPlay = 0
    var currentQuestionIndex = 0
    var questionCount = 0
    var oppName = "Computer"


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityDisplayAnswersBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val name = intent.getStringExtra(Constants.NAME).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val jointCode = intent.getStringExtra("123")
        val topic = intent.getStringExtra("re").toString()

        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()

        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // Show the dialog here
                getQuestionsSS2(clas, topic)
            }
        })

        binding.background.setOnClickListener {
            mydialog?.show()
        }

    }


    private fun getQuestionsSS2(classs: String, topic: String) {
        // Retrieve the "questions" node from the database
        val questionsRef: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("$classs, $topic")

// Initialize an empty array to store the questions
        val questions: ArrayList<SS2> = ArrayList()


// Add a listener to retrieve the questions
        questionsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the existing questions array
                questions.clear()


                // Iterate through the questions in the snapshot
                for (childSnapshot in dataSnapshot.children) {
                    // Get the question data
                    val question = childSnapshot.getValue(SS2::class.java)

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


    private fun presentQuestionsToUser(questions: ArrayList<SS2>, questionCount: Int) {
        val name = intent.getStringExtra(Constants.NAME).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val game_mode = intent.getStringExtra(Constants.GAME_MODE).toString()
        val gameCode = intent.getStringExtra("1111").toString()
        val oppName = intent.getStringExtra("oppName").toString()
        val playFirst = intent.getStringExtra("samyy")
        val jointCode = intent.getStringExtra("123")
        val topic = intent.getStringExtra("re").toString()

        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()

        // Get the current question
        val currentQuestion = questions[currentQuestionIndex]


        // Inflate the dialog box with the question and options
        // You can use a custom dialog or an AlertDialog for this

        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        val dialogLayoutBinding = layoutInflater.inflate(R.layout.dialog_layout, null)
        val question = dialogLayoutBinding.findViewById<TextView>(R.id.question)
        val secondBtn = dialogLayoutBinding.findViewById<TextView>(R.id.secondbtn)
        val firstBtn = dialogLayoutBinding.findViewById<TextView>(R.id.firstbtn)
        val thirdBtn = dialogLayoutBinding.findViewById<TextView>(R.id.thridbtn)
        val fourthBtn = dialogLayoutBinding.findViewById<TextView>(R.id.fourthbtn)
        mydialog = Dialog(this)
        mydialog?.setContentView(dialogLayoutBinding)
        mydialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mydialog?.setCancelable(true)


// Set the question and options in the dialog

        question.text = currentQuestion.question
        firstBtn.text = currentQuestion.option1
        secondBtn.text = currentQuestion.option2
        thirdBtn.text = currentQuestion.option3
        fourthBtn.text = currentQuestion.option4
        val ques = questionCount -1


// Store the correct answer
        val correctAnswer = currentQuestion.answer

        when (currentQuestion.answer) {
            "${firstBtn.text.toString()}" -> {
                firstBtn.background = resources.getDrawable(R.drawable.default_option_correct)
            }

            "${secondBtn.text.toString()}" -> {
                secondBtn.background = resources.getDrawable(R.drawable.default_option_correct)
            }

            "${thirdBtn.text.toString()}" -> {
                thirdBtn.background = resources.getDrawable(R.drawable.default_option_correct)
            }

            "${fourthBtn.text.toString()}" -> {
                fourthBtn.background = resources.getDrawable(R.drawable.default_option_correct)
            }

        }



            mydialog?.show()



        // All questions have been answered
        // Handle end of quiz or any other desired action

        // Dismiss the dialog


// Set click listeners for the option buttons
        val optionButtons = listOf(firstBtn, secondBtn, thirdBtn, fourthBtn)
        for (button in optionButtons) {
            button.setOnClickListener {
                dismissDialog()
                if (currentQuestionIndex >= ques && currentQuestionIndex != 0) {
                    Toast.makeText(this, "You have reached the end of this game", Toast.LENGTH_SHORT).show()
                } else if (currentQuestionIndex <= ques) {
                    currentQuestionIndex++

                    presentQuestionsToUser(questions, questionCount)
                }


                trial++




            }

        }


    }

        fun dismissDialog() {
            mydialog?.dismiss()
        }
    }

