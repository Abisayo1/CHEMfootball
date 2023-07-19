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
import android.os.CountDownTimer
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
import kotlin.concurrent.timer


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
    var j = 1
    var trialNumm = 0
    var trialNumb = 0
    private var timer: CountDownTimer? = null
    private var timers: Timer? = null
    var d = 0
    var isDialogOpen = false
    var shouldCheck = true
    var hasVideoPlayed = false

    // Create a handler and a delay duration for debouncing
    val debounceDelay = 5000L

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
            binding.background.isClickable = false
            oppName = intent.getStringExtra("oppName").toString()
            getTrialNum("$jointCode")
            val rootView = findViewById<View>(android.R.id.content)
            rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    // Show the dialog here
                    executeInitial()
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
             if (game_mode != "multi_player") {
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
        timer?.cancel()
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

        if (trialNumm >= questionCount && trial == 123) {
                Toast.makeText(this, "You have reached the end of this game", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, GameFinishedActivity::class.java)
                intent.putExtra(Constants.CLASS, clas)
                intent.putExtra("re", "$topic")
                intent.putExtra("scr", "$score")
                intent.putExtra("comp", "$scoreCT")
                intent.putExtra("opp", oppName)
                intent.putExtra("name", name)
                startActivity(intent)
            onDestroy()
                finish()
                // Perform the desired action here

        } else if (trialNumm <= questionCount) {
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
            onDestroy()
        } else if (trial <= questionCount) {
            getQuestionsSS2(classs = clas, topic)
        }
    }

    private fun oppTurn(option: String) {
        binding.background.isClickable = false
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        val dialogLayoutBinding = layoutInflater.inflate(R.layout.dialog_layout, null)
        val question = dialogLayoutBinding.findViewById<TextView>(R.id.question)
        val secondBtn = dialogLayoutBinding.findViewById<TextView>(R.id.secondbtn)
        val firstBtn = dialogLayoutBinding.findViewById<TextView>(R.id.firstbtn)
        val thirdBtn = dialogLayoutBinding.findViewById<TextView>(R.id.thridbtn)
        val fourthBtn = dialogLayoutBinding.findViewById<TextView>(R.id.fourthbtn)
        mydialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



       dismissDialog()
        d = 0

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
            executeAfter2Seconds()
        } else if (option == "lose") {
            VideoOppMisssPlay(play_misses)
            executeAfter2Seconds()
        }


        videoView.setOnCompletionListener {
            // Video playback completed
            NextQuestion()

        }

    }

    private fun playVideos(option: String, playerd: Boolean, direction: String) {
        binding.background.isClickable = false
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
                if (direction == "left") {
                    play = R.raw.ronaldo_left
                    play_misses = R.raw.ronaldo_misses
                } else if (direction == "right") {
                    play = R.raw.ronaldo
                    play_misses = R.raw.ronaldo_misses
                }
                else if (direction == "middle") {
                    play = R.raw.middle
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
                else if (direction == "middle") {
                    play = R.raw.middle
                    play_misses = R.raw.ronaldo_misses
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
                else if (direction == "middle") {
                    play = R.raw.middle
                    play_misses = R.raw.ronaldo_misses
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
                else if (direction == "middle") {
                    play = R.raw.middle
                    play_misses = R.raw.ronaldo_misses
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
                else if (direction == "middle") {
                    play = R.raw.middle
                    play_misses = R.raw.ronaldo_misses
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


        if (option == "win" && playerd) {
            saveScoreStatus("win")
            playVideo(play)
            executeAfter2Secondss()
        } else if (option == "lose" && playerd) {
            saveScoreStatus("lose")
            VideoMissPlay(play_misses)
            executeAfter2Secondss()

        } else if (option == "win" && !playerd) {
            saveScoreStatus("win")
            playVideo(play)
            executeAfter2Secondss()

        } else if (option == "lose" && !playerd) {
            saveScoreStatus("lose")
            VideoMissPlay(play_misses)
            executeAfter2Secondss()
        }

        videoView.setOnCompletionListener {
            NextQuestion()
            trialNumb = 1
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
                } else if (direction == "middle") {
                    play = R.raw.middle
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
                else if (direction == "middle") {
                    play = R.raw.middle
                    play_misses = R.raw.ronaldo_misses
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
                else if (direction == "middle") {
                    play = R.raw.middle
                    play_misses = R.raw.ronaldo_misses
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
                else if (direction == "middle") {
                    play = R.raw.middle
                    play_misses = R.raw.ronaldo_misses
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
                else if (direction == "middle") {
                    play = R.raw.middle
                    play_misses = R.raw.ronaldo_misses
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
            binding.background.isClickable = true
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

        val gameCode = intent.getStringExtra("1111").toString()

        trialNumb = 1
        shouldCheck = true
        binding.background.visibility = View.VISIBLE
        binding.videoView.visibility = View.GONE
        hasVideoPlayed = true
            getOppScore(gameCode)
        val ses = questionCount * 2
        if (trial == ses) {
            getOppScore(gameCode)
        }


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
        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        database.child(code).get().addOnSuccessListener {

            if (it.exists()) {
                val trialNum = it.child("trialNum").getValue(Int::class.java)!!
                val code = it.child("my_score").getValue(String::class.java)!!
                binding.scoreC.text = "$code"
                scoreCT = code
//                val ques = questionCount * 2
//                val west = ques - 1
//
//                if (trialNum == west) {
//                    dismissDialog()
//                    shouldCheck = false
//                    executeAfter2Secondssw()
//                }
                    if ("$playFirst" == "$name" && trialNum == trialNumm && isDialogOpen == false && shouldCheck == true && trialNumb != 0) {
                        automaticGamePlay()
                    } else if ("$playFirst" == "$oppName" && trialNum == trialNumm && isDialogOpen == false && shouldCheck == true && trialNumb != 0) {
                        automaticGamePlay()
                    }
                    if (trialNum > trialNumm && shouldCheck == true && trialNumb != 0) {
                        if (!isDialogOpen) {
                            automaticGamePlay()
                        }
                    } else if (trialNumm > trialNum && isDialogOpen == false && shouldCheck == true && trialNumb != 0) {
                        automaticGamePlay()
                    }

            } else {
                Toast.makeText(this, "Your Opponent is offline", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getOppScoress(code: String) {
        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        database.child(code).get().addOnSuccessListener {

            if (it.exists()) {

                val trialNum = it.child("trialNum").getValue(Int::class.java)!!
                val code = it.child("my_score").getValue(String::class.java)!!
                binding.scoreC.text = "$code"
                scoreCT = code
                val ques = questionCount * 2
                val west = ques - 1

                if (trialNum == west) {
                    dismissDialog()
                    shouldCheck = false
                    executeAfter2Secondssw()
                }

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

        val name = intent.getStringExtra(Constants.NAME).toString()
        val playFirst = intent.getStringExtra("samyy")
        // Code for the function you want to perform
        getTrialNum("$jointCode")
        getOppPlayerStatus(gameCode)
        getOppPlayer(gameCode)
        saveScore()
        if (oppScoreStatus == "win" || oppScoreStatus == "lose") {

            runOnUiThread {
                oppTurn(oppScoreStatus)
                // Update or interact with your views here
                // Update or interact with your views here
            }

        }
    }



    fun main() {
        // Define the interval in milliseconds
        val interval = 1000L // 5 seconds

        timers = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                performFunction()
            }
        }

        // Schedule the timer task to run at regular intervals
        timers?.scheduleAtFixedRate(timerTask, 0, interval)
    }

    fun mains() {
        val gameCode = intent.getStringExtra("1111").toString()
        // Define the interval in milliseconds
        val interval = 5000L // 5 seconds

        timers = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                getOppScore(gameCode)
            }
        }

        // Schedule the timer task to run at regular intervals
        timers?.scheduleAtFixedRate(timerTask, 0, interval)
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
                    mydialog?.setCancelable(false)
                    when ("$playFirst") {
                        "$oppName" -> {
                            if (trial % 2 == 0 || trial == 0) {
                                openDialogUpdate()
                            } else if (trial % 2 != 0 || trial != 0) {
                                j = 1
                                mydialogShow()
                                when(time) {
                                    "30 seconds" ->{
                                        if (j== 1) {
                                            executeAfter30Seconds()
                                        }
                                    }
                                    "1 minute" -> {
                                        if (j== 1) {
                                            executeAfter1Minute()
                                        }
                                    }

                                    "2 minutes" -> {
                                        if (j== 1) {
                                            executeAfter2Minutes()
                                        }

                                    }

                                    "3 minutes" -> {
                                        if (j== 1) {
                                            executeAfter3Minutes()
                                        }
                                    }
                                }
                            }
                        }

                        "$name" -> {
                            if (trial % 2 == 0 || trial == 0) {
                                j=1
                                mydialogShow()
                                when(time) {
                                    "30 seconds" ->{
                                        if (j== 1) {
                                            executeAfter30Seconds()
                                        }
                                    }
                                    "1 minute" -> {
                                        if (j== 1) {
                                            executeAfter1Minute()
                                        }
                                    }

                                    "2 minutes" -> {
                                        if (j== 1) {
                                            executeAfter2Minutes()
                                        }

                                    }

                                    "3 minutes" -> {
                                        if (j== 1) {
                                            executeAfter3Minutes()
                                        }
                                    }
                                }

                            } else if (trial % 2 != 0 || trial != 0) {
                                openDialogUpdate()

                            }
                        }
                    }

                    //    presentQuestionsToUser(questions, questionCount)
                } else if (game_mode != "multi_player") {
                    mydialog?.setCancelable(true)
                    mydialogShow()
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
                timer?.cancel()
                binding.background.isClickable = false
                dismissDialog()
                if (game_mode == "multi_player") {
                    shouldCheck = false
                    hasVideoPlayed = false
                    when ("$playFirst") {
                        "$oppName" -> {
                            if (trial % 2 == 0 || trial == 0) {
                                openDialogUpdate()
                            } else if (trial % 2 != 0 || trial != 0) {
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
                                        selectGoalScore2("win")
                                    } else if (trial % 2 == 1 || trial == 1) {
                                        binding.footballImageView.visibility = View.VISIBLE
                                        binding.leftLayout.visibility = View.VISIBLE
                                        binding.rightLayout.visibility = View.VISIBLE
                                        binding.chooseLayout.visibility = View.VISIBLE
                                        selectGoalScore2("win")
                                    }

                                } else if (!isCorrect) {
                                    if (trial % 2 == 0 || trial == 0) {
                                        binding.footballImageView.visibility = View.VISIBLE
                                        binding.leftLayout.visibility = View.VISIBLE
                                        binding.rightLayout.visibility = View.VISIBLE
                                        binding.chooseLayout.visibility = View.VISIBLE
                                        selectGoalScore2("lose")
                                    } else if (trial % 2 == 1 || trial == 1) {
                                        binding.footballImageView.visibility = View.VISIBLE
                                        binding.leftLayout.visibility = View.VISIBLE
                                        binding.rightLayout.visibility = View.VISIBLE
                                        binding.chooseLayout.visibility = View.VISIBLE
                                        selectGoalScore2("lose")
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
                                    if (trial % 2 == 0 || trial == 0) {
                                        binding.footballImageView.visibility = View.VISIBLE
                                        binding.leftLayout.visibility = View.VISIBLE
                                        binding.rightLayout.visibility = View.VISIBLE
                                        binding.chooseLayout.visibility = View.VISIBLE
                                        selectGoalScore2("win")
                                    } else if (trial % 2 == 1 || trial == 1) {
                                        binding.footballImageView.visibility = View.VISIBLE
                                        binding.leftLayout.visibility = View.VISIBLE
                                        binding.rightLayout.visibility = View.VISIBLE
                                        binding.chooseLayout.visibility = View.VISIBLE
                                        selectGoalScore2("win")
                                    }

                                } else if (!isCorrect) {
                                    if (trial % 2 == 0 || trial == 0) {
                                        binding.footballImageView.visibility = View.VISIBLE
                                        binding.leftLayout.visibility = View.VISIBLE
                                        binding.rightLayout.visibility = View.VISIBLE
                                        binding.chooseLayout.visibility = View.VISIBLE
                                        selectGoalScore2("lose")
                                    } else if (trial % 2 == 1 || trial == 1) {
                                        binding.footballImageView.visibility = View.VISIBLE
                                        binding.leftLayout.visibility = View.VISIBLE
                                        binding.rightLayout.visibility = View.VISIBLE
                                        binding.chooseLayout.visibility = View.VISIBLE
                                        selectGoalScore2("lose")
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
                            playVideo("win", false, "left")
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
                            playVideo("lose", false, "left")
                            trial++
                        }


                    }


                }
            }


        }

    }

    private fun executeAfter30Seconds() {
        timer = object : CountDownTimer(30_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val gameCode = intent.getStringExtra("1111").toString()
                val name = intent.getStringExtra(Constants.NAME).toString()
                val questions = questionCount * 2
                saveScore()
                val topic = intent.getStringExtra("re").toString()
                getOppScore(gameCode)

                clas = intent.getStringExtra(Constants.CLASS).toString()
                val game_mode = intent.getStringExtra(Constants.GAME_MODE).toString()

                val oppName = intent.getStringExtra("oppName").toString()
                val playFirst = intent.getStringExtra("samyy")
                val jointCode = intent.getStringExtra("123")

                player = intent.getStringExtra(Constants.PLAYER).toString()
                clas = intent.getStringExtra(Constants.CLASS).toString()

                if (trial >= questions && trial != 0) {
                    shouldCheck = false
                    Toast.makeText(applicationContext, "You have reached the end of this game", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, GameFinishedActivity::class.java)
                    intent.putExtra(Constants.CLASS, clas)
                    intent.putExtra("re", "$topic")
                    intent.putExtra("scr", "$score")
                    intent.putExtra("comp", "$scoreCT")
                    intent.putExtra("opp", oppName)
                    intent.putExtra("name", name)
                    startActivity(intent)
                    onDestroy()
                    finish()
                    // Perform the desired action here

                } else if (trial <= questions) {
                    trial++
                    saveTrialNum(
                        "$name",
                        "$oppName",
                        "$jointCode",
                        "$playFirst",
                        "$player",
                        trial
                    )
                    playVideos("lose", true, "left")
                }
            }

        }
        timer?.start()
    }

    private fun executeAfter1Minute() {
        timer = object : CountDownTimer(60_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val gameCode = intent.getStringExtra("1111").toString()
                val name = intent.getStringExtra(Constants.NAME).toString()
                val questions = questionCount * 2
                saveScore()
                val topic = intent.getStringExtra("re").toString()
                getOppScore(gameCode)

                clas = intent.getStringExtra(Constants.CLASS).toString()
                val game_mode = intent.getStringExtra(Constants.GAME_MODE).toString()

                val oppName = intent.getStringExtra("oppName").toString()
                val playFirst = intent.getStringExtra("samyy")
                val jointCode = intent.getStringExtra("123")

                player = intent.getStringExtra(Constants.PLAYER).toString()
                clas = intent.getStringExtra(Constants.CLASS).toString()

                if (trial >= questions && trial != 0) {
                    shouldCheck = false
                    Toast.makeText(applicationContext, "You have reached the end of this game", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, GameFinishedActivity::class.java)
                    intent.putExtra(Constants.CLASS, clas)
                    intent.putExtra("re", "$topic")
                    intent.putExtra("scr", "$score")
                    intent.putExtra("comp", "$scoreCT")
                    intent.putExtra("opp", oppName)
                    intent.putExtra("name", name)
                    startActivity(intent)
                    onDestroy()
                    finish()
                    // Perform the desired action here

                } else if (trial <= questions) {
                    trial++
                    saveTrialNum(
                        "$name",
                        "$oppName",
                        "$jointCode",
                        "$playFirst",
                        "$player",
                        trial
                    )
                    playVideos("lose", true, "left")
                }
            }

        }
        timer?.start()
    }

    private fun executeAfter2Minutes() {
        timer = object : CountDownTimer(120_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val gameCode = intent.getStringExtra("1111").toString()
                val name = intent.getStringExtra(Constants.NAME).toString()
                val questions = questionCount * 2
                saveScore()
                val topic = intent.getStringExtra("re").toString()
                getOppScore(gameCode)

                clas = intent.getStringExtra(Constants.CLASS).toString()
                val game_mode = intent.getStringExtra(Constants.GAME_MODE).toString()

                val oppName = intent.getStringExtra("oppName").toString()
                val playFirst = intent.getStringExtra("samyy")
                val jointCode = intent.getStringExtra("123")

                player = intent.getStringExtra(Constants.PLAYER).toString()
                clas = intent.getStringExtra(Constants.CLASS).toString()

                if (trial >= questions && trial != 0) {
                    shouldCheck = false
                    Toast.makeText(applicationContext, "You have reached the end of this game", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, GameFinishedActivity::class.java)
                    intent.putExtra(Constants.CLASS, clas)
                    intent.putExtra("re", "$topic")
                    intent.putExtra("scr", "$score")
                    intent.putExtra("comp", "$scoreCT")
                    intent.putExtra("opp", oppName)
                    intent.putExtra("name", name)
                    startActivity(intent)
                    onDestroy()
                    finish()
                    // Perform the desired action here

                } else if (trial <= questions) {
                    trial++
                    saveTrialNum(
                        "$name",
                        "$oppName",
                        "$jointCode",
                        "$playFirst",
                        "$player",
                        trial
                    )
                    playVideos("lose", true, "left")
                }
            }

        }
        timer?.start()
    }

    private fun executeAfter3Minutes() {
        timer = object : CountDownTimer(180_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val gameCode = intent.getStringExtra("1111").toString()
                val name = intent.getStringExtra(Constants.NAME).toString()
                val questions = questionCount * 2
                saveScore()
                val topic = intent.getStringExtra("re").toString()
                getOppScore(gameCode)

                clas = intent.getStringExtra(Constants.CLASS).toString()
                val game_mode = intent.getStringExtra(Constants.GAME_MODE).toString()

                val oppName = intent.getStringExtra("oppName").toString()
                val playFirst = intent.getStringExtra("samyy")
                val jointCode = intent.getStringExtra("123")

                player = intent.getStringExtra(Constants.PLAYER).toString()
                clas = intent.getStringExtra(Constants.CLASS).toString()

                if (trial >= questions && trial != 0) {
                    shouldCheck = false
                    Toast.makeText(applicationContext, "You have reached the end of this game", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, GameFinishedActivity::class.java)
                    intent.putExtra(Constants.CLASS, clas)
                    intent.putExtra("re", "$topic")
                    intent.putExtra("scr", "$score")
                    intent.putExtra("comp", "$scoreCT")
                    intent.putExtra("opp", oppName)
                    intent.putExtra("name", name)
                    startActivity(intent)
                    onDestroy()
                    finish()
                    // Perform the desired action here

                } else if (trial <= questions) {
                    trial++
                    saveTrialNum(
                        "$name",
                        "$oppName",
                        "$jointCode",
                        "$playFirst",
                        "$player",
                        trial
                    )
                    playVideos("lose", true, "left")
                }
            }

        }
        timer?.start()
    }

    private fun SaveTrialNum(
        name: String,
        oppName: String,
        jointCode: String?,
        playFirst: String?
    ) {
        trial++
        saveTrialNum(
            "$name",
            "$oppName",
            "$jointCode",
            "$playFirst",
            "$player",
            trial
        )
    }

    private fun executeAfter4Seconds() {
        timer = object : CountDownTimer(1_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                binding.background.isClickable = true
            }

        }
        timer?.start()
    }

    private fun executeAfter2Seconds() {
        timer = object : CountDownTimer(1_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                oppScoreStatus = "nil"
            }

        }
        timer?.start()
    }


    private fun executecheckPlay() {
        timer = object : CountDownTimer(5_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                if (d == 1)
                {
                    val gameCode = intent.getStringExtra("1111").toString()
                    dismissDialog()
                    getOppScore(gameCode)
                }
            }

        }
        timer?.start()
    }

    private fun executeNextQuestion() {
        timer = object : CountDownTimer(1_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
               if (hasVideoPlayed) {
                   NextQuestion()
               }
            }

        }
        timer?.start()
    }

    private fun executevidePLay() {
        timer = object : CountDownTimer(4_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {

            }

        }
        timer?.start()
    }

    private fun executeInitial() {
        timer = object : CountDownTimer(4_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                openDialog()
            }

        }
        timer?.start()
    }

    private fun executeAfter2Secondssw() {
                if (trial != 0) {
                    val name = intent.getStringExtra(Constants.NAME).toString()
                    val topic = intent.getStringExtra("re").toString()

                    Toast.makeText(
                        applicationContext,
                        "You have reached the end of this game",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(applicationContext, GameFinishedActivity::class.java)
                    intent.putExtra(Constants.CLASS, clas)
                    intent.putExtra("re", "$topic")
                    intent.putExtra("scr", "$score")
                    intent.putExtra("comp", "$scoreCT")
                    intent.putExtra("opp", oppName)
                    intent.putExtra("name", name)
                    startActivity(intent)
                    onDestroy()
                    finish()
                    // Perform the desired action here

                }



    }


    private fun executeAfter2Secondss() {
        timer = object : CountDownTimer(1_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                saveScoreStatus("nil")
            }

        }
        timer?.start()
    }

    private fun executeNotTurnPlay() {
        timer = object : CountDownTimer(5_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                goToNextActivity()
            }

        }
        timer?.start()
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
                mydialog?.setCancelable(false)


                secondBtn.visibility = View.GONE
                firstBtn.visibility = View.GONE
                thirdBtn.visibility = View.GONE
                fourthBtn.visibility = View.GONE

                getOppScoress(gameCode)



                question.text = "It is $oppName's turn to play"

                mydialogShow()


            }

    private fun mydialogShow() {
            mydialog?.show()
            isDialogOpen = true
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
        isDialogOpen = false
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


    fun selectGoalScore(status: String) {
        binding.background.isClickable = false
        Toast.makeText(this, "Drag ball to post", Toast.LENGTH_LONG).show()
        val footballView: ImageView = findViewById(R.id.footballImageView)
        val leftLayout: View = findViewById(R.id.leftLayout)
        val rightLayout: View = findViewById(R.id.rightLayout)
        val middleLayout: View = findViewById(R.id.middleLayout)
        val originalLayout: View = findViewById(R.id.originalLayout)
        moveBallToOrigin(footballView, originalLayout)


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
                    } else if (isViewIntersecting(footballView, middleLayout)) {
                        // Move the football to the center of the right layout
                        footballView.x = middleLayout.x + (middleLayout.width - footballView.width) / 2
                        footballView.y = middleLayout.y + (middleLayout.height - footballView.height) / 2
                        val direction = "middle"
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
                    else if (isViewIntersecting(footballView, originalLayout)) {
                        moveBallToOrigin(footballView, originalLayout)
                    }

                    true
                }
                else -> false
            }
        }
    }

    fun selectGoalScore2(status: String) {
        binding.background.isClickable = false
        val footballView: ImageView = findViewById(R.id.footballImageView)
        val leftLayout: View = findViewById(R.id.leftLayout)
        val rightLayout: View = findViewById(R.id.rightLayout)
        val originalLayout: View = findViewById(R.id.originalLayout)
        val middleLayout: View = findViewById(R.id.middleLayout)
        moveBallToOrigin(footballView, originalLayout)
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
                        playVideos(status, true, direction)
                        SaveTrialNum(name, oppName, jointCode, playFirst)
                        trialNumm++

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
                        playVideos(status, true, direction)
                        SaveTrialNum(name, oppName, jointCode, playFirst)
                        trialNumm++



                        // Handle option selection here
                        // You can check if the selected option is correct and perform any necessary actions

                        // Check if the selected option is the correct answer
                    }
                    else if (isViewIntersecting(footballView, middleLayout)) {
                        // Move the football to the center of the right layout
                        footballView.x = middleLayout.x + (middleLayout.width - footballView.width) / 2
                        footballView.y = middleLayout.y + (middleLayout.height - footballView.height) / 2
                        val direction = "middle"
                        binding.footballImageView.visibility = View.GONE
                        binding.leftLayout.visibility = View.GONE
                        binding.rightLayout.visibility = View.GONE
                        binding.chooseLayout.visibility = View.GONE
                        binding.background.visibility = View.VISIBLE
                        playVideos(status, true, direction)
                        SaveTrialNum(name, oppName, jointCode, playFirst)
                        trialNumm++



                        // Handle option selection here
                        // You can check if the selected option is correct and perform any necessary actions

                        // Check if the selected option is the correct answer
                    }
                    // If the football is dropped outside the layouts, reset its position
                    else if (isViewIntersecting(footballView, originalLayout)) {
                        moveBallToOrigin(footballView, originalLayout)
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

    override fun onDestroy() {
        super.onDestroy()

        // Cancel the timer and its associated tasks
        timers?.cancel()
        timers?.purge()
        shouldCheck = false
    }

    fun automaticGamePlay() {
        if (trialNumm == questionCount) {
            val name = intent.getStringExtra(Constants.NAME).toString()
            saveScore()
                Toast.makeText(this, "You have reached the end of this game", Toast.LENGTH_SHORT).show()

                // Perform the desired action here
            } else {;
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

        if (game_mode == "multi_player") {
            when ("$playFirst") {
                "$oppName" -> {
                    if (trial % 2 == 0 || trial == 0) {
                        openDialogUpdate()
                        getOppScore(gameCode)
                    } else if (trial % 2 != 0 || trial != 0) {
                        getOppScore(gameCode)
                        openDialog()

                    }


                }

                "$name" -> {
                    if (trial % 2 == 0 || trial == 0) {
                        getOppScore(gameCode)
                        openDialog()
                    } else if (trial % 2 != 0 || trial != 0) {
                        openDialogUpdate()
                        getOppScore(gameCode)

                    }
                }

            }

        }
    }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        shouldCheck = false
        dismissDialog()
        mydialog?.setCancelable(true)

        // Perform any necessary operations or cleanup before leaving

        // Call finish() to close the current activity and navigate to the previous activity
        finish()


    }

    fun goToNextActivity(){
        Toast.makeText(this, "You have reached the end of this game", Toast.LENGTH_SHORT).show()
        val name = intent.getStringExtra(Constants.NAME).toString()
        val topic = intent.getStringExtra("re").toString()
        val intent = Intent(this, GameFinishedActivity::class.java)
        intent.putExtra(Constants.CLASS, clas)
        intent.putExtra("re", "$topic")
        intent.putExtra("scr", "$score")
        intent.putExtra("comp", "$scoreCT")
        intent.putExtra("opp", oppName)
        intent.putExtra("name", name)
        startActivity(intent)
        onDestroy()
        finish()
    }







    private fun moveBallToOrigin(foot: ImageView, right: View) {
        foot.x = right.x + (right.width - foot.width) / 2
        foot.y = right.y + (right.height - foot.height) / 2
    }
    }
