package com.abisayo.chemfootball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatDelegate
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityGamePlayBinding
import com.abisayo.chemfootball.models.Multiplayer
import com.abisayo.chemfootball.models.Player
import com.abisayo.chemfootball.models.Scores
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class GamePlayActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    var mMediaPlayer: MediaPlayer? = null
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityGamePlayBinding
    var score = 0
    var trialNum = 0
    private var clas = "SS1"
    private var scoreC = 0
    private var player = ""
    private var oppPlayer = "Abisayo"
    var oppScoreStatus = "nil"
    var hasVideoPlay = 0
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
        val oppName = intent.getStringExtra("oppName").toString()
        val playFirst = intent.getStringExtra("samyy")
        val jointCode = intent.getStringExtra("123")

        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()

//        Toast.makeText(this, "$game_mode", Toast.LENGTH_SHORT).show()

        binding.namePlayer.text = name



        if (game_mode == "multi_player") {
            getTrialNum("$jointCode")
            getOppPlayer(gameCode)
            getOppScore(gameCode)
            main()
            binding.computerPlayer.text = oppName
            openDialog()
        } else {
            binding.computerPlayer.text = "Computer"
            openSingleDialog()
        }

        binding.background.setOnClickListener {
            getOppScore(gameCode)
            if (trialNum == 8 && game_mode == "multi_player") {
                saveName(name, "$trialNum", "$score - $scoreC")
            } else if (trialNum == 8 && game_mode != "multi_player") {
                val noteTitle = clas
                val studentName = name
                val score = "$score - ${scoreC}"
                val userID = FirebaseAuth.getInstance().currentUser?.uid

                database = FirebaseDatabase.getInstance().getReference("Scores")
                val Score = Scores(studentName, noteTitle, score, userID)
                if (noteTitle != null) {
                    if (userID != null) {
                        database.child(userID + noteTitle).setValue(Score).addOnSuccessListener {
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

        when (trialNum) {
            1 -> {
                question.text = "Which of the following salts is insoluble in water?"
                firstBtn.text = "Pb(NO)3"
                secondBtn.text = "Na2CO3"
                thirdBtn.text = "AgNO3"
                fourthBtn.text = "AgCl"
            }
            2 -> {
                question.text = "Which of the following statements is correct"
                firstBtn.text = "Gases increase with increase in temperature"
                secondBtn.text = "Gases decrese with increase in temperature"
                thirdBtn.text = "most solid solute decrease with increase in temperature"
                fourthBtn.text = "most solid solute is constant"
            }
            3 -> {
                question.text =
                    "for a given solute, the concentration of its saturated solution in different solvents are:"
                firstBtn.text = "the same at the same temperature"
                secondBtn.text = "different at the same temperature"
                thirdBtn.text = "the same at different temperature"
                fourthBtn.text = "constant"
            }
            4 -> {
                question.text =
                    "On which of the following is the solubility of gaseous substance dependent?"
                firstBtn.text = "Nature of solvent, Nature of solute, Temperature and Pressure"
                secondBtn.text = "Nature of solvent & nature of Nature of solute"
                thirdBtn.text = "Nature of solute"
                fourthBtn.text = "None of the above"
            }
            5 -> {
                question.text =
                    "A change in temperature of a saturated solution disturbs the equilibrium between the:"
                firstBtn.text = "Dissolved solute and the solvent"
                secondBtn.text = "Solvent and undissolved solute"
                thirdBtn.text = "Dissolved solute and undissolved solute"
                fourthBtn.text = "Dissolved solute and the solution"
            }
            6 -> {
                question.text = "A super saturated solution is said to contain"
                firstBtn.text =
                    "More solute than it can dissolve at a given temperature in the presence of undissolved solute"
                secondBtn.text =
                    "as much solute as it can dissolve at a given temperature in the presence of undissolved solute"
                thirdBtn.text = "I don't know"
                fourthBtn.text = "All of the above"
            }
            7 -> {
                question.text = "Sodium Chloride has no solubility product value because of its"
                firstBtn.text = "Saline nature"
                secondBtn.text = "high solubility"
                thirdBtn.text = "low solubility"
                fourthBtn.text = "insolubility"
            }
            else -> {

            }

        }

        mydialog.show()

        firstBtn.setOnClickListener {
            when ("$playFirst") {
                "$oppName" -> {
                    if (trialNum % 2 == 0 || trialNum == 0) {
                        Toast.makeText(this, "It is not your turn to play", Toast.LENGTH_SHORT)
                            .show()
                    } else if (trialNum % 2 != 0 || trialNum != 0) {
                        trialNum++
                        saveTrialNum(
                            "$name",
                            "$oppName",
                            "$jointCode",
                            "$playFirst",
                            "$player",
                            trialNum
                        )
                        mydialog.dismiss()
                        playVideos("a")
                    }


                }

                "$name" -> {
                    if (trialNum % 2 == 0 || trialNum == 0) {
                        mydialog.dismiss()
                        trialNum++
                        saveTrialNum(
                            "$name",
                            "$oppName",
                            "$jointCode",
                            "$playFirst",
                            "$player",
                            trialNum
                        )
                        playVideos("a")

                    } else if (trialNum % 2 != 0 || trialNum != 0) {
                        Toast.makeText(this, "It is not your turn to play", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

            }
        }

        secondBtn.setOnClickListener {
            when ("$playFirst") {
                "$oppName" -> {
                    if (trialNum % 2 == 0 || trialNum == 0) {
                        Toast.makeText(this, "It is not your turn to play", Toast.LENGTH_SHORT)
                            .show()
                    } else if (trialNum % 2 != 0 || trialNum != 0) {
                        mydialog.dismiss()
                        trialNum++
                        saveTrialNum(
                            "$name",
                            "$oppName",
                            "$jointCode",
                            "$playFirst",
                            "$player",
                            trialNum
                        )
                        playVideos("b")
                    }


                }

                "$name" -> {
                    if (trialNum % 2 == 0 || trialNum == 0) {
                        mydialog.dismiss()
                        trialNum++
                        saveTrialNum(
                            "$name",
                            "$oppName",
                            "$jointCode",
                            "$playFirst",
                            "$player",
                            trialNum
                        )
                        playVideos("b")

                    } else if (trialNum % 2 != 0 || trialNum != 0) {
                        Toast.makeText(this, "It is not your turn to play", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

            }
        }

        thirdBtn.setOnClickListener {
            when ("$playFirst") {
                "$oppName" -> {
                    if (trialNum % 2 == 0 || trialNum == 0) {
                        Toast.makeText(this, "It is not your turn to play", Toast.LENGTH_SHORT)
                            .show()
                    } else if (trialNum % 2 != 0 || trialNum != 0) {
                        mydialog.dismiss()
                        trialNum++
                        saveTrialNum(
                            "$name",
                            "$oppName",
                            "$jointCode",
                            "$playFirst",
                            "$player",
                            trialNum
                        )
                        playVideos("c")
                    }


                }

                "$name" -> {
                    if (trialNum % 2 == 0 || trialNum == 0) {
                        mydialog.dismiss()
                        trialNum++
                        saveTrialNum(
                            "$name",
                            "$oppName",
                            "$jointCode",
                            "$playFirst",
                            "$player",
                            trialNum
                        )
                        playVideos("c")

                    } else if (trialNum % 2 != 0 || trialNum != 0) {
                        Toast.makeText(this, "It is not your turn to play", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

            }
        }

        fourthBtn.setOnClickListener {
            when ("$playFirst") {
                "$oppName" -> {
                    if (trialNum % 2 == 0 || trialNum == 0) {
                        Toast.makeText(this, "It is not your turn to play", Toast.LENGTH_SHORT)
                            .show()
                    } else if (trialNum % 2 != 0 || trialNum != 0) {
                        mydialog.dismiss()
                        trialNum++
                        saveTrialNum(
                            "$name",
                            "$oppName",
                            "$jointCode",
                            "$playFirst",
                            "$player",
                            trialNum
                        )
                        playVideos("d")
                    }


                }

                "$name" -> {
                    if (trialNum % 2 == 0 || trialNum == 0) {
                        mydialog.dismiss()
                        trialNum++
                        saveTrialNum(
                            "$name",
                            "$oppName",
                            "$jointCode",
                            "$playFirst",
                            "$player",
                            trialNum
                        )
                        playVideos("d")

                    } else if (trialNum % 2 != 0 || trialNum != 0) {
                        Toast.makeText(this, "It is not your turn to play", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

            }
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
        val mydialog = Dialog(this)
        mydialog.setContentView(dialogLayoutBinding)
        mydialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        mydialog.setCancelable(true)

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

        if (trialNum == 0 && option == "win") {
            playOppVideo(play)
            saveScoreStatus("win")
        } else if (trialNum == 0 && option == "lose") {
            VideoOppMisssPlay(play_misses)

        }
        if (trialNum == 1 && option == "win") {
            playOppVideo(play)
        } else if (trialNum == 1 && option == "lose") {
            VideoOppMisssPlay(play_misses)
        }

        if (trialNum == 2 && option == "win") {
            playOppVideo(play)
        } else if (trialNum == 2 && option == "lose") {
            VideoOppMisssPlay(play_misses)
        }

        if (trialNum == 3 && option == "win") {
            playOppVideo(play)
        } else if (trialNum == 3 && option == "lose") {
            VideoOppMisssPlay(play_misses)
        }
        if (trialNum == 4 && option == "win") {
            playOppVideo(play)
        } else if (trialNum == 4 && option == "lose") {
            VideoOppMisssPlay(play_misses)
        }
        if (trialNum == 5 && option == "win") {
            playOppVideo(play)
        } else if (trialNum == 5 && option == "lose") {
            VideoOppMisssPlay(play_misses)
        }
        if (trialNum == 6 && option == "win") {
            playOppVideo(play)
        } else if (trialNum == 6 && option == "lose") {
            VideoOppMisssPlay(play_misses)
        }
        if (trialNum == 7 && option == "win") {
            playOppVideo(play)
        } else if (trialNum == 7 && option == "lose") {
            VideoOppMisssPlay(play_misses)
        }


        videoView.setOnCompletionListener {
            // Video playback completed
            oppScoreStatus = "nil"
            NextQuestion()
        }

    }

    private fun playVideos(option: String) {
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

        if (trialNum == 0 && option == "b") {
            playVideo(play)
            saveScoreStatus("win")
        } else if (trialNum == 0 && option != "b") {
            VideoMissPlay(play_misses)
            saveScoreStatus("lose")

        }
        if (trialNum == 1 && option == "d") {
            playVideo(play)
            saveScoreStatus("win")
        } else if (trialNum == 1 && option != "d") {
            VideoMissPlay(play_misses)
            saveScoreStatus("lose")
        }

        if (trialNum == 2 && option == "b") {
            playVideo(play)
            saveScoreStatus("win")
        } else if (trialNum == 2 && option != "b") {
            VideoMissPlay(play_misses)
            saveScoreStatus("lose")
        }

        if (trialNum == 3 && option == "b") {
            playVideo(play)
            saveScoreStatus("win")
        } else if (trialNum == 3 && option != "b") {
            VideoMissPlay(play_misses)
            saveScoreStatus("lose")
        }
        if (trialNum == 4 && option == "a") {
            playVideo(play)
            saveScoreStatus("win")
        } else if (trialNum == 4 && option != "a") {
            VideoMissPlay(play_misses)
            saveScoreStatus("lose")
        }
        if (trialNum == 5 && option == "c") {
            playVideo(play)
            saveScoreStatus("win")
        } else if (trialNum == 5 && option != "c") {
            VideoMissPlay(play_misses)
            saveScoreStatus("lose")
        }
        if (trialNum == 6 && option == "a") {
            playVideo(play)
            saveScoreStatus("win")
        } else if (trialNum == 6 && option != "a") {
            VideoMissPlay(play_misses)
            saveScoreStatus("lose")
        }
        if (trialNum == 7 && option == "a") {
            playVideo(play)
            saveScoreStatus("win")
        } else if (trialNum == 7 && option != "a") {
            VideoMissPlay(play_misses)
            saveScoreStatus("lose")
        }


        videoView.setOnCompletionListener {
            // Video playback completed
            NextQuestion()
            saveScoreStatus("nil")
        }

    }

    fun playVideo(option: String) {
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

        if (trialNum == 0 && option == "b") {
            playVideo(play)
        } else if (trialNum == 0 && option != "b") {
            VideoMissPlay(play_misses)

        }
        if (trialNum == 1 && option == "d") {
            playyVideo(keep)
        } else if (trialNum == 1 && option != "d") {
            VideoMisssPlay(keep_misses)
        }

        if (trialNum == 2 && option == "b") {
            playVideo(play)
        } else if (trialNum == 2 && option != "b") {
            VideoMissPlay(play_misses)
        }

        if (trialNum == 3 && option == "b") {
            playyVideo(keep)
        } else if (trialNum == 3 && option != "b") {
            VideoMisssPlay(keep_misses)
        }
        if (trialNum == 4 && option == "a") {
            playVideo(play)
        } else if (trialNum == 4 && option != "a") {
            VideoMissPlay(play_misses)
        }
        if (trialNum == 5 && option == "c") {
            playyVideo(keep)
        } else if (trialNum == 5 && option != "c") {
            VideoMisssPlay(keep_misses)
        }
        if (trialNum == 6 && option == "a") {
            playVideo(play)
        } else if (trialNum == 6 && option != "a") {
            VideoMissPlay(play_misses)
        }
        if (trialNum == 7 && option == "a") {
            playyVideo(keep)
        } else if (trialNum == 7 && option != "a") {
            VideoMisssPlay(keep_misses)
        }


        videoView.setOnCompletionListener {
            // Video playback completed
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
                trialNum = trialsNum

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
        if (oppScoreStatus == "win" || oppScoreStatus == "lose") {

            runOnUiThread {
                oppTurn(oppScoreStatus)
                // Update or interact with your views here
            }

        }
    }


    fun main() {
        // Define the interval in milliseconds
        val interval = 5000L // 5 seconds

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
        val players = ""
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        val multi_player = Multiplayer(
            "name",
            "opp_name",
            "code",
            scoreStatus,
            my_score,
            opponent_score,
            trialNum,
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


    @SuppressLint("SetTextI18n")
    fun openSingleDialog() {
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

        when (trialNum) {
            1 -> {
                question.text = "Which of the following salts is insoluble in water?"
                firstBtn.text = "Pb(NO)3"
                secondBtn.text = "Na2CO3"
                thirdBtn.text = "AgNO3"
                fourthBtn.text = "AgCl"
            }
            2 -> {
                question.text = "Which of the following statements is correct"
                firstBtn.text = "Gases increase with increase in temperature"
                secondBtn.text = "Gases decrese with increase in temperature"
                thirdBtn.text = "most solid solute decrease with increase in temperature"
                fourthBtn.text = "most solid solute is constant"
            }
            3 -> {
                question.text =
                    "for a given solute, the concentration of its saturated solution in different solvents are:"
                firstBtn.text = "the same at the same temperature"
                secondBtn.text = "different at the same temperature"
                thirdBtn.text = "the same at different temperature"
                fourthBtn.text = "constant"
            }
            4 -> {
                question.text =
                    "On which of the following is the solubility of gaseous substance dependent?"
                firstBtn.text = "Nature of solvent, Nature of solute, Temperature and Pressure"
                secondBtn.text = "Nature of solvent & nature of Nature of solute"
                thirdBtn.text = "Nature of solute"
                fourthBtn.text = "None of the above"
            }
            5 -> {
                question.text =
                    "A change in temperature of a saturated solution disturbs the equilibrium between the:"
                firstBtn.text = "Dissolved solute and the solvent"
                secondBtn.text = "Solvent and undissolved solute"
                thirdBtn.text = "Dissolved solute and undissolved solute"
                fourthBtn.text = "Dissolved solute and the solution"
            }
            6 -> {
                question.text = "A super saturated solution is said to contain"
                firstBtn.text =
                    "More solute than it can dissolve at a given temperature in the presence of undissolved solute"
                secondBtn.text =
                    "as much solute as it can dissolve at a given temperature in the presence of undissolved solute"
                thirdBtn.text = "I don't know"
                fourthBtn.text = "All of the above"
            }
            7 -> {
                question.text = "Sodium Chloride has no solubility product value because of its"
                firstBtn.text = "Saline nature"
                secondBtn.text = "high solubility"
                thirdBtn.text = "low solubility"
                fourthBtn.text = "insolubility"
            }
            else -> {

            }

        }

        mydialog.show()

        firstBtn.setOnClickListener {
            trialNum++
            mydialog.dismiss()
            playVideo("a")
        }

        secondBtn.setOnClickListener {
            trialNum++
            mydialog.dismiss()
            playVideo("b")
        }

        thirdBtn.setOnClickListener {
            trialNum++
            mydialog.dismiss()
            playVideo("c")


        }

        fourthBtn.setOnClickListener {
            trialNum++
            mydialog.dismiss()
            playVideo("d")

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
}