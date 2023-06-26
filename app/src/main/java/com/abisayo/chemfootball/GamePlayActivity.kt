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
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityGamePlayBinding
import com.abisayo.chemfootball.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class GamePlayActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    var mMediaPlayer: MediaPlayer? = null
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityGamePlayBinding
    var score = 0
    var trial = 0
    private var clas = "SS1"
    private var scoreC = 0
    private var player = ""
    lateinit var mydialog: Dialog
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
        binding = ActivityGamePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val name = intent.getStringExtra(Constants.NAME).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val game_mode = intent.getStringExtra(Constants.GAME_MODE).toString()
        val gameCode = intent.getStringExtra("1111").toString()
        val playFirst = intent.getStringExtra("samyy")
        val jointCode = intent.getStringExtra("123")
        val topic = intent.getStringExtra("re").toString()

        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        getOppPlayer(gameCode)

//        Toast.makeText(this, "$game_mode", Toast.LENGTH_SHORT).show()


        binding.namePlayer.text = name

        getQuestionsSS2(classs = clas, topic)

        if (game_mode == "multi_player") {
            getTrialNum("$jointCode")
            getOppPlayer(gameCode)
            getOppScore(gameCode)
            main()
            binding.computerPlayer.text = oppName
            oppName = intent.getStringExtra("oppName").toString()

        } else if (game_mode != "multi_player") {
            binding.computerPlayer.text = "Computer"
            oppName = "Computer"

        }

        binding.background.setOnClickListener {
                getOppScore(gameCode)
                if (trial == 8 && game_mode == "multi_player") {
                    saveName(name, "$trial", "$score - $scoreC")
                } else if (trial == 8 && game_mode != "multi_player") {
                    val noteTitle = clas
                    val studentName = name
                    val score = "$score - ${scoreC}"
                    val userID = FirebaseAuth.getInstance().currentUser?.uid

                    database = FirebaseDatabase.getInstance().getReference("Scores")
                    val Score = Scores(studentName, noteTitle, score, userID)
                    if (noteTitle != null) {
                        if (userID != null) {
                            database.child(userID + noteTitle).setValue(Score)
                                .addOnSuccessListener {
//                            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }

                    Toast.makeText(this, "You have reached the end of the game", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (game_mode == "multi_player") {
                        openDialog()
                        getOppPlayerStatus(gameCode)
                        if (oppScoreStatus == "win" || oppScoreStatus == "lose") {
                            oppTurn(oppScoreStatus)
                        } else if (oppScoreStatus == "nil") {
                            binding.dash.visibility = View.VISIBLE
                        }
                    } else if (game_mode != "multi_player") {
                        openSingleDialog()
                    }

                }



        }
    }


    // 1. Plays the water sound
    fun playSound(sound: Int) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, sound)
            mMediaPlayer!!.isLooping = false
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()

    }

    // 2. Pause playback
    fun pauseSound() {
        if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
    }

    // 3. Stops playback
    fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    // 4. Destroys the MediaPlayer instance when the app is closed
    override fun onStop() {
        super.onStop()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }


    fun deactivate(button: Button) {
        button.isClickable = false

    }

    @SuppressLint("SetTextI18n")
    fun openDialog() {
        saveScore()
        val topic = intent.getStringExtra("re").toString()

        if (trial == questionCount) {
            Toast.makeText(this, "You have reached the end of this game", Toast.LENGTH_SHORT).show()
        } else if (trial < questionCount) {
            getQuestionsSS2(classs = clas, topic)
        }
    }

    @SuppressLint("SetTextI18n")
    fun openSingleDialog() {
        saveScore()
        val topic = intent.getStringExtra("re").toString()

        if (trial == questionCount) {
            Toast.makeText(this, "You have reached the end of this game", Toast.LENGTH_SHORT).show()
        } else if (trial < questionCount) {
            getQuestionsSS2(classs = clas, topic)
        }
    }

    private fun oppTurn(option: String) {
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        val dialogLayoutBinding = layoutInflater.inflate(R.layout.dialog_layout, null)
        val question = dialogLayoutBinding.findViewById<TextView>(R.id.question)
        val secondBtn = dialogLayoutBinding.findViewById<TextView>(R.id.secondbtn)
        val firstBtn = dialogLayoutBinding.findViewById<TextView>(R.id.firstbtn)
        val thirdBtn = dialogLayoutBinding.findViewById<TextView>(R.id.thridbtn)
        val fourthBtn = dialogLayoutBinding.findViewById<TextView>(R.id.fourthbtn)
        mydialog.setContentView(dialogLayoutBinding)
        mydialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))




        mydialog.setCancelable(true)

        mydialog.cancel()
        mydialog.dismiss()



        var play = R.raw.messi
        var play_misses = R.raw.messi_misses

        var keep = R.raw.allison_keeper_wins
        var keep_misses = R.raw.alisson_keeper_misses
        binding.videoView.visibility = View.VISIBLE

        videoView = binding.videoView

        when (oppPlayer) {
            "Fragment1" -> {
                play = R.raw.ronaldo
                play_misses = R.raw.ronaldo_misses
            }
            "Fragment2" -> {
                play = R.raw.messi
                play_misses = R.raw.messi_misses
            }
            "Fragment3" -> {
                play = R.raw.mbappe
                play_misses = R.raw.mbappe_misses
            }
            "Fragment4" -> {
                play = R.raw.neymar
                play_misses = R.raw.neymar_misses
            }
            "Fragment5" -> {
                play = R.raw.rashford
                play_misses = R.raw.rashford_misses
            }
            "Fragment6" -> {
                play_misses = R.raw.de_bruyen_misses
                play = R.raw.de_bruyen

            }
            "Fragment7" -> {
                play = R.raw.messi
                play_misses = R.raw.messi_misses
            }
            "Fragment8" -> {
                play = R.raw.haaland
                play_misses = R.raw.messi_misses
            }


        }

        if(option == "win") {
            mydialog.dismiss()
            playOppVideo(play)
        } else if (option == "lose") {
            mydialog.dismiss()
            VideoOppMisssPlay(play_misses)
        }


        videoView.setOnCompletionListener {
            // Video playback completed
            oppScoreStatus = "nil"
            NextQuestion()
        }

    }

    private fun playVideos(option: String, playerd: Boolean) {
        trial++
        val name = intent.getStringExtra(Constants.NAME).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val game_mode = intent.getStringExtra(Constants.GAME_MODE).toString()
        val gameCode = intent.getStringExtra("1111").toString()
        val oppName = intent.getStringExtra("oppName").toString()
        val playFirst = intent.getStringExtra("samyy")
        val jointCode = intent.getStringExtra("123")
        getTrialNum("$jointCode")
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        val dialogLayoutBinding = layoutInflater.inflate(R.layout.dialog_layout, null)
        val question = dialogLayoutBinding.findViewById<TextView>(R.id.question)
        val secondBtn = dialogLayoutBinding.findViewById<TextView>(R.id.secondbtn)
        val firstBtn = dialogLayoutBinding.findViewById<TextView>(R.id.firstbtn)
        val thirdBtn = dialogLayoutBinding.findViewById<TextView>(R.id.thridbtn)
        val fourthBtn = dialogLayoutBinding.findViewById<TextView>(R.id.fourthbtn)
        val mydialog = Dialog(this)
        mydialog.setContentView(dialogLayoutBinding)
        mydialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        mydialog.setCancelable(true)
        var play = R.raw.messi
        var play_misses = R.raw.messi_misses

        var keep = R.raw.allison_keeper_wins
        var keep_misses = R.raw.alisson_keeper_misses
        binding.videoView.visibility = View.VISIBLE

        videoView = binding.videoView

        when (player) {
            "Fragment1" -> {
                play = R.raw.ronaldo
                play_misses = R.raw.ronaldo_misses
            }
            "Fragment2" -> {
                play = R.raw.messi
                play_misses = R.raw.messi_misses
            }
            "Fragment3" -> {
                play = R.raw.mbappe
                play_misses = R.raw.mbappe_misses
            }
            "Fragment4" -> {
                play = R.raw.neymar
                play_misses = R.raw.neymar_misses
            }
            "Fragment5" -> {
                play = R.raw.rashford
                play_misses = R.raw.rashford_misses
            }
            "Fragment6" -> {
                play_misses = R.raw.de_bruyen_misses
                play = R.raw.de_bruyen

            }
            "Fragment7" -> {
                play = R.raw.messi
                play_misses = R.raw.messi_misses
            }
            "Fragment8" -> {
                play = R.raw.haaland
                play_misses = R.raw.messi_misses
            }


        }

        if (option == "win" && playerd) {
            saveScoreStatus("win")
            playVideo(play)
        } else if (option == "lose" && playerd) {
            saveScoreStatus("lose")
            VideoMissPlay(play_misses)

        } else if (option == "win" && !playerd) {
            saveScoreStatus("win")
            playVideo(play)

        } else if (option == "lose" && !playerd) {
            saveScoreStatus("lose")
            VideoMissPlay(play_misses)
        }

        saveTrialNum(
            "$name",
            "$oppName",
            "$jointCode",
            "$playFirst",
            "$player",
            trial
        )

        videoView.setOnCompletionListener {
            NextQuestion()
            saveScoreStatus("nil")
        }


    }

    fun playVideo(option: String, playerd: Boolean) {
        trial++
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        var play = R.raw.messi
        var play_misses = R.raw.messi_misses

        var keep = R.raw.allison_keeper_wins
        var keep_misses = R.raw.alisson_keeper_misses
        binding.videoView.visibility = View.VISIBLE

        videoView = binding.videoView

        when (player) {
            "Fragment1" -> {
                play = R.raw.ronaldo
                play_misses = R.raw.ronaldo_misses
            }
            "Fragment2" -> {
                play = R.raw.messi
                play_misses = R.raw.messi_misses
            }
            "Fragment3" -> {
                play = R.raw.mbappe
                play_misses = R.raw.mbappe_misses
            }
            "Fragment4" -> {
                play = R.raw.neymar
                play_misses = R.raw.neymar_misses
            }
            "Fragment5" -> {
                play = R.raw.rashford
                play_misses = R.raw.rashford_misses
            }
            "Fragment6" -> {
                play_misses = R.raw.de_bruyen_misses
                play = R.raw.de_bruyen

            }
            "Fragment7" -> {
                play = R.raw.messi
                play_misses = R.raw.messi_misses
            }
            "Fragment8" -> {
                play = R.raw.haaland
                play_misses = R.raw.messi_misses
            }

        }

        when (keeper) {
            "FragmentII" -> {
                keep = R.raw.ederson_keeper_wins
                keep_misses = R.raw.ederson_keeper_misses
            }
            "FragmentV" -> {
                keep = R.raw.allison_keeper_wins
                keep_misses = R.raw.alisson_keeper_misses
            }
            "FragmentVII" -> {
                keep = R.raw.donarouma_keeper_wins
                keep_misses = R.raw.donarouma_keeper_misses
            }

        }

        if (option == "win" && playerd) {
            playVideo(play)
        } else if (option == "lose" && playerd) {
            VideoMissPlay(play_misses)

        } else if (option == "win" && !playerd) {
            playVideo(keep)

        } else if (option == "lose" && !playerd) {
            VideoMisssPlay(keep_misses)
        }

        videoView.setOnCompletionListener {
            NextQuestion()
        }
    }

    private fun VideoMissPlay(play_misses: Int) {
        val videoUri =
            Uri.parse("android.resource://" + packageName + "/" + play_misses) // Replace with your video file or URL
        PlayVideoM().setVideoURI(videoUri)

        videoView.start()
        binding.scoreV.text = "$score"
    }

    private fun VideoMisssPlay(play_misses: Int) {
        scoreC++
        val videoUri =
            Uri.parse("android.resource://" + packageName + "/" + play_misses) // Replace with your video file or URL
        PlayVideoM().setVideoURI(videoUri)

        videoView.start()
        binding.scoreC.text = "$scoreC"
    }

    private fun VideoOppMisssPlay(play_misses: Int) {
        val videoUri =
            Uri.parse("android.resource://" + packageName + "/" + play_misses) // Replace with your video file or URL
        PlayVideoM().setVideoURI(videoUri)

        videoView.start()
        binding.scoreC.text = "$scoreC"
    }

    private fun playVideo(play: Int) {
        score++
        val videoUri =
            Uri.parse("android.resource://" + packageName + "/" + play) // Replace with your video file or URL
        videoView.setVideoURI(videoUri)

        videoView.start()
        binding.scoreV.text = "$score"
    }

    private fun playOppVideo(play: Int) {
        val videoUri =
            Uri.parse("android.resource://" + packageName + "/" + play) // Replace with your video file or URL
        videoView.setVideoURI(videoUri)

        videoView.start()
        binding.scoreV.text = "$score"
    }

    private fun playyVideo(play: Int) {
        val videoUri =
            Uri.parse("android.resource://" + packageName + "/" + play) // Replace with your video file or URL
        videoView.setVideoURI(videoUri)

        videoView.start()
        binding.scoreV.text = "$score"
    }

    private fun PlayVideoM() = videoView

    private fun NextQuestion() {
        binding.background.visibility = View.VISIBLE
        binding.videoView.visibility = View.GONE
    }

    fun saveName(name: String, trialNum: String, score: String) {
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("Player")
        val player = Player("$name", trialNum, score)
        if (userID != null) {
            database.child(userID).setValue(player).addOnSuccessListener {
                Toast.makeText(this, "Go Player!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getOppScore(code: String) {
        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        database.child(code).get().addOnSuccessListener {

            if (it.exists()) {

                val trialNum = it.child("trialNum").value
                val code = it.child("my_score").value
                binding.scoreC.text = "$code"

            } else {
                Toast.makeText(this, "Your Opponent is offline", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getTrialNum(code: String) {
        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        database.child(code).get().addOnSuccessListener {

            if (it.exists()) {

                val trialsNum = it.child("trialNum").getValue(Int::class.java)!!
                trial = trialsNum

            } else {
                Toast.makeText(this, "User Does not Exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }


    fun performFunction() {
        val jointCode = intent.getStringExtra("123")
        val gameCode = intent.getStringExtra("1111").toString()
        // Code for the function you want to perform
        getTrialNum("$jointCode")
        getOppPlayerStatus(gameCode)
        getOppPlayer(gameCode)
        if (oppScoreStatus == "win" || oppScoreStatus == "lose") {

            runOnUiThread {
                oppTurn(oppScoreStatus)
                // Update or interact with your views here
            }

        }
    }


    fun main() {
        // Define the interval in milliseconds
        val interval = 3000L // 5 seconds

        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                performFunction()
            }
        }

        // Schedule the timer task to run at regular intervals
        timer.scheduleAtFixedRate(timerTask, 0, interval)
    }

    fun saveTrialNum(
        name: String,
        opp_name: String,
        code: String,
        whoPlayFirst: String,
        Player: String,
        trialNum: Int
    ) {
        val scoreStatus = "0"
        val my_score = "0"
        val opponent_score = "0"
        val whoPlayFirst = whoPlayFirst
        val players = Player
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        val multi_player = Multiplayer(
            name,
            opp_name,
            code,
            scoreStatus,
            my_score,
            opponent_score,
            trialNum,
            whoPlayFirst,
            players
        )
        if (userID != null) {
            database.child(code).setValue(multi_player).addOnSuccessListener {
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveScoreStatus(scoreStatus: String) {
        val my_score = "$score"
        val opponent_score = "0"
        val whoPlayFirst = ""
        val players = player
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        val multi_player = Multiplayer(
            "name",
            "opp_name",
            "code",
            scoreStatus,
            my_score,
            opponent_score,
            trial,
            whoPlayFirst,
            players
        )
        if (userID != null) {
            database.child(userID).setValue(multi_player).addOnSuccessListener {
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getOppPlayer(code: String) {
        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        database.child(code).get().addOnSuccessListener {

            if (it.exists()) {

                val player = it.child("player").getValue(String::class.java)!!
                oppPlayer = player


            } else {
                Toast.makeText(this, "User Does not Exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getOppPlayerStatus(code: String) {
        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        database.child(code).get().addOnSuccessListener {

            if (it.exists()) {

                val scoreStatus = it.child("scoreStatus").getValue(String::class.java)!!
                oppScoreStatus = scoreStatus


            } else {
                Toast.makeText(this, "User Does not Exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }


    }


    private fun getQuestionsSS2(classs: String, topic: String) {
        // Retrieve the "questions" node from the database
        val questionsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("$classs, $topic")

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
        mydialog.setContentView(dialogLayoutBinding)
        mydialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

// Set the question and options in the dialog

        question.text = currentQuestion.question
        firstBtn.text = currentQuestion.option1
        secondBtn.text = currentQuestion.option2
        thirdBtn.text = currentQuestion.option3
        fourthBtn.text = currentQuestion.option4

// Store the correct answer
        val correctAnswer = currentQuestion.answer

        if (currentQuestionIndex+1 > questionCount) {
            Toast.makeText(this, "You have reached the end of the game", Toast.LENGTH_SHORT).show()
        }
        else {
            mydialog.setCancelable(true)
            mydialog.show()
            if (currentQuestionIndex < questionCount) {
                //    presentQuestionsToUser(questions, questionCount)
            }
        }



            // All questions have been answered
            // Handle end of quiz or any other desired action

        // Dismiss the dialog


// Set click listeners for the option buttons
        val optionButtons = listOf(firstBtn, secondBtn, thirdBtn, fourthBtn)
        for (button in optionButtons) {
            button.setOnClickListener {
                if (game_mode == "multi_player") {
                    when ("$playFirst") {
                        "$oppName" -> {
                            if (trial % 2 == 0 || trial == 0) {
                                Toast.makeText(
                                    this,
                                    "It is not your turn to play",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else if (trial % 2 != 0 || trial != 0) {
                                currentQuestionIndex++
                                hasVideoPlay = 1
                                mydialog.cancel()
                                mydialog.dismiss()
                                hasVideoPlay = 1

                                // Handle option selection here
                                // You can check if the selected option is correct and perform any necessary actions

                                // Check if the selected option is the correct answer
                                val selectedAnswer = button.text.toString()
                                val isCorrect = selectedAnswer == correctAnswer

                                if (isCorrect) {
                                    if (trial % 2 == 0 || trial == 0) {
                                        playVideos("win", true)
                                    } else if (trial % 2 == 1 || trial == 1) {
                                        playVideos("win", true)
                                    }

                                } else if (!isCorrect) {
                                    if (trial % 2 == 0 || trial == 0) {
                                        playVideos("lose", true)
                                    } else if (trial % 2 == 1 || trial == 1) {
                                        playVideos("lose", true)
                                    }


                                }
                            }


                        }

                        "$name" -> {
                            if (trial % 2 == 0 || trial == 0) {
                                currentQuestionIndex++
                                hasVideoPlay = 1
                                mydialog.cancel()
                                mydialog.dismiss()
                                hasVideoPlay = 1

                                // Handle option selection here
                                // You can check if the selected option is correct and perform any necessary actions

                                // Check if the selected option is the correct answer
                                val selectedAnswer = button.text.toString()
                                val isCorrect = selectedAnswer == correctAnswer

                                if (isCorrect) {
                                    if (trial % 2 == 0 || trial == 0) {
                                        playVideos("win", true)
                                    } else if (trial % 2 == 1 || trial == 1) {
                                        playVideos("win", true)
                                    }

                                } else if (!isCorrect) {
                                    if (trial % 2 == 0 || trial == 0) {
                                        playVideos("lose", true)
                                    } else if (trial % 2 == 1 || trial == 1) {
                                        playVideos("lose", true)
                                    }


                                }

                            } else if (trial % 2 != 0 || trial != 0) {
                                Toast.makeText(
                                    this,
                                    "It is not your turn to play",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }
                        }

                    }
                } else if (game_mode != "multi_player") {
                    mydialog.cancel()
                    mydialog.dismiss()
                    currentQuestionIndex++

                    // Handle option selection here
                    // You can check if the selected option is correct and perform any necessary actions

                    // Check if the selected option is the correct answer
                    val selectedAnswer = button.text.toString()
                    val isCorrect = selectedAnswer == correctAnswer

                    if (isCorrect) {
                        if (trial % 2 == 0 || trial == 0) {
                            playVideo("win", true)
                        } else if (trial % 2 != 0 || trial == 1) {
                            playVideo("win", false)
                        }

                    } else if (!isCorrect) {
                        if (trial % 2 == 0 || trial == 0) {
                            playVideo("lose", true)
                        } else if (trial % 2 != 1 || trial == 1) {
                            playVideo("lose", false)
                        }


                    }


                }
            }


        }

    }

    fun saveScore() {
        val name = intent.getStringExtra(Constants.NAME).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val topic = intent.getStringExtra("re").toString()

        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()

        val studentName = name
        val courseTitle = topic
        val studentScore = "$name: $score - $oppName: $scoreC"
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("Scores")
        val scores = Scores(studentName, courseTitle, studentScore, userID)
        if (userID != null) {
            database.child(userID).setValue(scores).addOnSuccessListener {
                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}