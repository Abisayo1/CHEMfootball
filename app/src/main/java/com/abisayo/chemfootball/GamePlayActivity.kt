package com.abisayo.chemfootball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityGamePlayBinding
import com.abisayo.chemfootball.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    var mydialog: Dialog? = null
    var oppPlayer = "Fragment6"
    var oppScoreStatus = "nil"
    var studentScore = "JESUS IS LORD"
    var currentQuestionIndex = 0
    var questionCount = 0
    var oppName = "Computer"
    var time = "I min"
    val handler = Handler()
    var scoreCT = ""



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

        getTimerNum(clas, topic)



        binding.namePlayer.text = name


        if (game_mode == "multi_player") {
            oppName = intent.getStringExtra("oppName").toString()
            getTrialNum("$jointCode")
            val rootView = findViewById<View>(android.R.id.content)
            rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    // Show the dialog here
                    openDialog()
                }
            })
            getOppPlayer(gameCode)
            getOppScore(gameCode)
            main()
            binding.computerPlayer.text = oppName


        } else if (game_mode != "multi_player") {
            val rootView = findViewById<View>(android.R.id.content)
            rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    // Show the dialog here
                    openSingleDialog()
                }
            })
            binding.computerPlayer.text = "Computer"
            oppName = "Computer"

        }

        binding.background.setOnClickListener {
            saveScore()
            if (game_mode == "multi_player") {
                getOppScore(gameCode)
                openDialog()
            } else if (game_mode != "multi_player") {
                openSingleDialog()
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
        val gameCode = intent.getStringExtra("1111").toString()
        val name = intent.getStringExtra(Constants.NAME).toString()
        val questions = questionCount * 2
        saveScore()
        val topic = intent.getStringExtra("re").toString()
        getOppScore(gameCode)

        if (trial >= questions && trial != 0) {
                Toast.makeText(this, "You have reached the end of this game", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, GameFinishedActivity::class.java)
                intent.putExtra(Constants.CLASS, clas)
                intent.putExtra("re", "$topic")
                intent.putExtra("scr", "$score")
                intent.putExtra("comp", "$scoreCT")
                intent.putExtra("opp", oppName)
                intent.putExtra("name", name)
                startActivity(intent)
                finish()
                // Perform the desired action here

        } else if (trial <= questions) {
            getQuestionsSS2(classs = clas, topic)
        }
    }

    @SuppressLint("SetTextI18n")
    fun openSingleDialog() {
        saveScore()
        val topic = intent.getStringExtra("re").toString()
        val name = intent.getStringExtra(Constants.NAME).toString()

        if (trial >= questionCount && trial != 0) {
            Toast.makeText(this, "You have reached the end of this game", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, GameFinishedActivity::class.java)
            intent.putExtra(Constants.CLASS, clas)
            intent.putExtra("re", "$topic")
            intent.putExtra("scr", "$score")
            intent.putExtra("comp", "$scoreC")
            intent.putExtra("opp", oppName)
            intent.putExtra("name", name)
            startActivity(intent)
        } else if (trial <= questionCount) {
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
        mydialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))




        mydialog?.setCancelable(true)

       dismissDialog()

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

        if (option == "win") {
            playOppVideo(play)
        } else if (option == "lose") {
            VideoOppMisssPlay(play_misses)
        }


        videoView.setOnCompletionListener {
            // Video playback completed
            oppScoreStatus = "nil"
            NextQuestion()
        }

    }

    private fun playVideos(option: String, playerd: Boolean) {
        val name = intent.getStringExtra(Constants.NAME).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val jointCode = intent.getStringExtra("123")
        getTrialNum("$jointCode")



        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        val dialogLayoutBinding = layoutInflater.inflate(R.layout.dialog_layout, null)
        val question = dialogLayoutBinding.findViewById<TextView>(R.id.question)
        val secondBtn = dialogLayoutBinding.findViewById<TextView>(R.id.secondbtn)
        val firstBtn = dialogLayoutBinding.findViewById<TextView>(R.id.firstbtn)
        val thirdBtn = dialogLayoutBinding.findViewById<TextView>(R.id.thridbtn)
        val fourthBtn = dialogLayoutBinding.findViewById<TextView>(R.id.fourthbtn)
        mydialog?.setContentView(dialogLayoutBinding)
        mydialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))




        mydialog?.setCancelable(true)

        dismissDialog()



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

        videoView.setOnCompletionListener {
            NextQuestion()
            saveScoreStatus("nil")
        }


    }

    fun playVideo(option: String, playerd: Boolean, direction: String) {
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        var play = R.raw.messi
        var play_misses = R.raw.messi_misses

        var keep = R.raw.allison_keeper_wins
        var keep_misses = R.raw.alisson_keeper_misses
        binding.videoView.visibility = View.VISIBLE

        videoView = binding.videoView

        when (player) {
            "Fragment1" -> {
                if (direction == "left") {
                    play = R.raw.ronaldo_left
                    play_misses = R.raw.ronaldo_misses
                } else if (direction == "right") {
                    play = R.raw.ronaldo
                    play_misses = R.raw.ronaldo_misses
                }

            }
            "Fragment2" -> {
                if (direction == "right") {
                    play = R.raw.messi_right
                    play_misses = R.raw.messi_misses
                } else if (direction == "left") {
                    play = R.raw.messi
                    play_misses = R.raw.messi_misses
                }
            }
            "Fragment3" -> {
                if (direction == "left") {
                    play = R.raw.mbappe_left
                    play_misses = R.raw.mbappe_misses
                } else if (direction == "right") {
                    play = R.raw.mbappe
                    play_misses = R.raw.mbappe_misses
                }

            }
            "Fragment4" -> {
                if (direction == "left") {
                    play = R.raw.neymar_left
                    play_misses = R.raw.neymar_misses
                } else if (direction == "right") {
                    play = R.raw.neymer_right
                    play_misses = R.raw.neymar_misses
                }

            }
            "Fragment5" -> {
                if (direction == "left") {
                    play = R.raw.rashford_left
                    play_misses = R.raw.rashford_misses
                } else if (direction == "right") {
                    play = R.raw.rashford
                    play_misses = R.raw.rashford_misses
                }

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
        binding.scoreC.text = "$scoreC"
    }

    private fun  VideoMisssPlay(play_misses: Int) {
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
                val code = it.child("my_score").getValue(String::class.java)!!
                binding.scoreC.text = "$code"
                scoreCT = code

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
                Toast.makeText(this, "Could not find user", Toast.LENGTH_SHORT).show()
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
        getOppScore(gameCode)
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


// Store the correct answer
        val correctAnswer = currentQuestion.answer

        if (currentQuestionIndex + 1 > questionCount) {
            Toast.makeText(this, "You have reached the end of the game", Toast.LENGTH_SHORT).show()
        } else {
            if (currentQuestionIndex < questionCount) {
                if (game_mode == "multi_player") {
                    when ("$playFirst") {
                        "$oppName" -> {
                            if (trial % 2 == 0 || trial == 0) {
                                openDialogUpdate()
                            } else if (trial % 2 != 0 || trial != 0) {
                                mydialog?.show()
                                when(time) {
                                    "30 seconds" ->{
                                        executeAfter30Seconds()
                                    }
                                    "1 minute" -> {
                                        executeAfter1Minute()
                                    }

                                    "2 minutes" -> {
                                        executeAfter2Minutes()

                                    }

                                    "3 minutes" -> {
                                        executeAfter3Minutes()
                                    }
                                }
                            }
                        }

                        "$name" -> {
                            if (trial % 2 == 0 || trial == 0) {
                                mydialog?.setCancelable(true)
                                mydialog?.show()
                                when(time) {
                                    "30 seconds" ->{
                                        executeAfter30Seconds()
                                        }
                                    "1 minute" -> {
                                        executeAfter1Minute()
                                    }

                                    "2 minutes" -> {
                                        executeAfter2Minutes()

                                    }

                                    "3 minutes" -> {
                                        executeAfter3Minutes()
                                    }
                                }

                            } else if (trial % 2 != 0 || trial != 0) {
                                openDialogUpdate()

                            }
                        }
                    }

                    //    presentQuestionsToUser(questions, questionCount)
                } else if (game_mode != "multi_player") {
                    mydialog?.show()
                }
            }
        }


        // All questions have been answered
        // Handle end of quiz or any other desired action

        // Dismiss the dialog


// Set click listeners for the option buttons
        val optionButtons = listOf(firstBtn, secondBtn, thirdBtn, fourthBtn)
        for (button in optionButtons) {
            button.setOnClickListener {
                dismissDialog()
                if (game_mode == "multi_player") {
                    when ("$playFirst") {
                        "$oppName" -> {
                            if (trial % 2 == 0 || trial == 0) {
                                openDialogUpdate()
                            } else if (trial % 2 != 0 || trial != 0) {
                                currentQuestionIndex++
                                dismissDialog()


                                // Handle option selection here
                                // You can check if the selected option is correct and perform any necessary actions

                                // Check if the selected option is the correct answer
                                val selectedAnswer = button.text.toString()
                                val isCorrect = selectedAnswer == correctAnswer

                                if (isCorrect) {
                                    trial++
                                    saveTrialNum(
                                        "$name",
                                        "$oppName",
                                        "$jointCode",
                                        "$playFirst",
                                        "$player",
                                        trial
                                    )
                                    if (trial % 2 == 0 || trial == 0) {
                                        playVideos("win", true)
                                    } else if (trial % 2 == 1 || trial == 1) {
                                        playVideos("win", true)
                                    }

                                } else if (!isCorrect) {
                                    trial++
                                    saveTrialNum(
                                        "$name",
                                        "$oppName",
                                        "$jointCode",
                                        "$playFirst",
                                        "$player",
                                        trial
                                    )
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


                                // Handle option selection here
                                // You can check if the selected option is correct and perform any necessary actions

                                // Check if the selected option is the correct answer
                                val selectedAnswer = button.text.toString()
                                val isCorrect = selectedAnswer == correctAnswer

                                if (isCorrect) {
                                    trial++
                                    saveTrialNum(
                                        "$name",
                                        "$oppName",
                                        "$jointCode",
                                        "$playFirst",
                                        "$player",
                                        trial
                                    )
                                    if (trial % 2 == 0 || trial == 0) {
                                        playVideos("win", true)
                                    } else if (trial % 2 == 1 || trial == 1) {
                                        playVideos("win", true)
                                    }

                                } else if (!isCorrect) {
                                    trial++
                                    saveTrialNum(
                                        "$name",
                                        "$oppName",
                                        "$jointCode",
                                        "$playFirst",
                                        "$player",
                                        trial
                                    )
                                    if (trial % 2 == 0 || trial == 0) {
                                        playVideos("lose", true)
                                    } else if (trial % 2 == 1 || trial == 1) {
                                        playVideos("lose", true)
                                    }


                                }

                            } else if (trial % 2 != 0 || trial != 0) {
                                openDialogUpdate()

                            }
                        }

                    }
                } else if (game_mode != "multi_player") {
                    currentQuestionIndex++

                    // Handle option selection here
                    // You can check if the selected option is correct and perform any necessary actions

                    // Check if the selected option is the correct answer
                    val selectedAnswer = button.text.toString()
                    val isCorrect = selectedAnswer == correctAnswer

                    if (isCorrect) {
                        if (trial % 2 == 0 || trial == 0) {
                            binding.footballImageView.visibility = View.VISIBLE
                            binding.leftLayout.visibility = View.VISIBLE
                            binding.rightLayout.visibility = View.VISIBLE
                            binding.chooseLayout.visibility = View.VISIBLE
                            selectGoalScore("win")
                        } else if (trial % 2 != 0 || trial == 1) {
                            playVideo("win", false, "nil")
                            trial++
                        }

                    } else if (!isCorrect) {
                        if (trial % 2 == 0 || trial == 0) {
                            binding.footballImageView.visibility = View.VISIBLE
                            binding.leftLayout.visibility = View.VISIBLE
                            binding.rightLayout.visibility = View.VISIBLE
                            binding.chooseLayout.visibility = View.VISIBLE
                            selectGoalScore("lose")
                        } else if (trial % 2 != 0 || trial == 1) {
                            playVideo("lose", false, "nil")
                            trial++
                        }


                    }


                }
            }


        }

    }


    fun openDialogUpdate() {
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


                // Inflate the dialog box with the question and options
                // You can use a custom dialog or an AlertDialog for this

                val dialogLayoutBinding = layoutInflater.inflate(R.layout.dialog_layout, null)
                val question = dialogLayoutBinding.findViewById<TextView>(R.id.question)
                val secondBtn = dialogLayoutBinding.findViewById<TextView>(R.id.secondbtn)
                val firstBtn = dialogLayoutBinding.findViewById<TextView>(R.id.firstbtn)
                val thirdBtn = dialogLayoutBinding.findViewById<TextView>(R.id.thridbtn)
                val fourthBtn = dialogLayoutBinding.findViewById<TextView>(R.id.fourthbtn)
                mydialog?.setContentView(dialogLayoutBinding)


                secondBtn.visibility = View.GONE
                firstBtn.visibility = View.GONE
                thirdBtn.visibility = View.GONE
                fourthBtn.visibility = View.GONE


                question.text = "It is $oppName's turn to play"

                mydialog?.show()


            }

            fun saveScore() {
                val name = intent.getStringExtra(Constants.NAME).toString()
                clas = intent.getStringExtra(Constants.CLASS).toString()
                val topic = intent.getStringExtra("re").toString()

                player = intent.getStringExtra(Constants.PLAYER).toString()
                clas = intent.getStringExtra(Constants.CLASS).toString()


                val studentName = name
                val courseTitle = topic
                studentScore = "$name: $score - $oppName: $scoreC"
                val userID = FirebaseAuth.getInstance().currentUser?.uid

                database = FirebaseDatabase.getInstance().getReference("Scores")
                val scores = Scores(studentName, courseTitle, studentScore, userID)
                if (userID != null) {
                    database.child("$userID, $topic").setValue(scores).addOnSuccessListener {

                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

    fun dismissDialog() {
        mydialog?.dismiss()
    }

    private fun getTimerNum(cls: String, topc: String) {
        database = FirebaseDatabase.getInstance().getReference(cls)
        database.child(topc).get().addOnSuccessListener {

            if (it.exists()) {

                val timer = it.child("timer").getValue(String::class.java)!!
                time = timer


            } else {
                Toast.makeText(this, "Could not find user", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }

    fun executeAfter30Seconds()
    {
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
        handler.postDelayed({
            currentQuestionIndex++
            trial++
            saveTrialNum(
                                        "$name",
                                        "$oppName",
                                        "$jointCode",
                                        "$playFirst",
                                        "$player",
                                        trial
                                    )
            playVideos("lose", true)
            // Perform the desired action here
        }, 30000)
    }

    fun executeAfter1Minute() {
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
        handler.postDelayed({
            currentQuestionIndex++
            trial++
            saveTrialNum(
                                        "$name",
                                        "$oppName",
                                        "$jointCode",
                                        "$playFirst",
                                        "$player",
                                        trial
                                    )
            playVideos("lose", true)
            // Perform the desired action here
        }, 60000)
    }

    fun executeAfter2Minutes() {
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
        handler.postDelayed({
            currentQuestionIndex++
            trial++
            saveTrialNum(
                                        "$name",
                                        "$oppName",
                                        "$jointCode",
                                        "$playFirst",
                                        "$player",
                                        trial
                                    )
            playVideos("lose", true)
            // Perform the desired action here
        }, 120000)
    }

    fun executeAfter3Minutes() {

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

        handler.postDelayed({
            currentQuestionIndex++
            trial++
            saveTrialNum(
                                        "$name",
                                        "$oppName",
                                        "$jointCode",
                                        "$playFirst",
                                        "$player",
                                        trial
                                    )
            playVideos("lose", true)
            // Perform the desired action here
        }, 180000)
    }

    fun selectGoalScore(status: String) {
        val footballView: ImageView = findViewById(R.id.footballImageView)
        val leftLayout: View = findViewById(R.id.leftLayout)
        val rightLayout: View = findViewById(R.id.rightLayout)

        val originalX = footballView.x
        val originalY = footballView.y

        footballView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Capture the initial touch position
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    // Update the position of the football
                    footballView.x = event.rawX - footballView.width / 2
                    footballView.y = event.rawY - footballView.height / 2

                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Check if the football is dropped on the left layout
                    if (isViewIntersecting(footballView, leftLayout)) {
                        // Move the football to the center of the left layout
                        footballView.x = leftLayout.x + (leftLayout.width - footballView.width) / 2
                        footballView.y = leftLayout.y + (leftLayout.height - footballView.height) / 2
                        val direction = "left"
                        binding.footballImageView.visibility = View.GONE
                        binding.leftLayout.visibility = View.GONE
                        binding.rightLayout.visibility = View.GONE
                        binding.chooseLayout.visibility = View.GONE
                        binding.background.visibility = View.VISIBLE
                        playVideo(status, true, direction)
                        trial++
                    }
                    // Check if the football is dropped on the right layout
                    else if (isViewIntersecting(footballView, rightLayout)) {
                        // Move the football to the center of the right layout
                        footballView.x = rightLayout.x + (rightLayout.width - footballView.width) / 2
                        footballView.y = rightLayout.y + (rightLayout.height - footballView.height) / 2
                        val direction = "right"
                        binding.footballImageView.visibility = View.GONE
                        binding.leftLayout.visibility = View.GONE
                        binding.rightLayout.visibility = View.GONE
                        binding.chooseLayout.visibility = View.GONE
                        binding.background.visibility = View.VISIBLE
                        playVideo(status, true, direction)
                        trial++



                        // Handle option selection here
                        // You can check if the selected option is correct and perform any necessary actions

                        // Check if the selected option is the correct answer
                    }
                    // If the football is dropped outside the layouts, reset its position
                    else {
                        footballView.x = originalX
                        footballView.y = originalY
                    }

                    true
                }
                else -> false
            }
        }
    }

    // Function to check if two views intersect each other
    private fun isViewIntersecting(view1: View, view2: View): Boolean {
        val rect1 =
            Rect()
        view1.getHitRect(rect1)
        val rect2 = Rect()
        view2.getHitRect(rect2)
        return Rect.intersects(rect1, rect2)
    }
    }
