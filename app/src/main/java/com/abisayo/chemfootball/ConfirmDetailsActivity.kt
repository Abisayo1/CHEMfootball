package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityConfirmDetailsBinding
import com.abisayo.chemfootball.models.Multiplayer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ConfirmDetailsActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    private var clas = "SS1"
    private var player = ""
    var playFirst = ""
    var combineCode = ""
    var oppTopic = "Topic"
    private var timer: CountDownTimer? = null
    private lateinit var binding: ActivityConfirmDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityConfirmDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        val name = intent.getStringExtra(Constants.NAME).toString()
        val gameCode = intent.getStringExtra("1111").toString()
        val jointCode = intent.getStringExtra("123").toString()
        val oppName = intent.getStringExtra("oppName").toString()
        val topic = intent.getStringExtra("re").toString()
        val codes = intent.getStringExtra("aaaaa").toString()
        val  userID = FirebaseAuth.getInstance().currentUser?.uid



        binding.editTextName.setText("$name")
        binding.editTextGameCode.setText("$gameCode")
        binding.editTextComputer.setText("$oppName")


        binding.button1.setOnClickListener {
            val answerSpinner = binding.editTextWhoPlaying
            val selectedAnswer = answerSpinner.selectedItem as String
            when (selectedAnswer) {
                "You" -> {
                    playFirst = "$name"
                    combineCode = "$userID + $gameCode"
                }
                "Opponent" -> {
                    playFirst = "$oppName"
                    combineCode = "$gameCode + $userID"
                }

            }
            if (selectedAnswer != "Select First Player" ) {
                saveName("$name", "$oppName", "$combineCode", "$playFirst", player)
                saveNames("$name", "$oppName", "$gameCode", "$playFirst", player, "$topic")
                getTopic(gameCode)
                executeAfter2Seconds()
            } else {
                Toast.makeText(applicationContext, "Who is playing first?", Toast.LENGTH_SHORT)
                    .show()
            }


        }


        binding.button2.setOnClickListener {
            onBackPressed()
        }

    }

    fun saveName(name : String, opp_name : String, code: String, whoPlayFirst : String, Player: String) {
        val scoreStatus = "0"
        val my_score = "0"
        val opponent_score = "0"
        val trialNum = 0
        val whoPlayFirst = whoPlayFirst
        val players = Player
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        val multi_player = Multiplayer(name,opp_name, code, scoreStatus, my_score, opponent_score, trialNum, whoPlayFirst, players)
        if (userID != null) {
            database.child(code).setValue(multi_player).addOnSuccessListener {
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveNames(name : String, opp_name : String, code: String, whoPlayFirst : String, Player: String, topic: String) {
        val scoreStatus = "0"
        val my_score = "0"
        val opponent_score = "0"
        val trialNum = 0
        val whoPlayFirst = whoPlayFirst
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        val multi_player = Multiplayer(name,opp_name, code, scoreStatus, my_score, opponent_score, trialNum, whoPlayFirst, Player, topic)
        if (userID != null) {
            database.child(userID).setValue(multi_player).addOnSuccessListener {
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTopic(code: String) {
        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        database.child(code).get().addOnSuccessListener {

            if (it.exists()) {

                val topic = it.child("topic").getValue(String::class.java)!!
                oppTopic = topic

            } else {
                Toast.makeText(this, "Opponent is offline", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun executeAfter2Seconds() {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
        timer = object : CountDownTimer(5_000, 1_000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val keeper = intent.getStringExtra(Constants.KEEPER).toString()
                val name = intent.getStringExtra(Constants.NAME).toString()
                val gameCode = intent.getStringExtra("1111").toString()
                val jointCode = intent.getStringExtra("123").toString()
                val oppName = intent.getStringExtra("oppName").toString()
                val topic = intent.getStringExtra("re").toString()
                val codes = intent.getStringExtra("aaaaa").toString()
                val  userID = FirebaseAuth.getInstance().currentUser?.uid
                val answerSpinner = binding.editTextWhoPlaying
                val selectedAnswer = answerSpinner.selectedItem as String


                if (selectedAnswer != "Select First Player" ) {
                    if (topic != oppTopic) {
                        Toast.makeText(applicationContext, "You and your opponent must select the same topic", Toast.LENGTH_SHORT).show()
                    } else {
                        val jointCode = intent.getStringExtra("123").toString()
                        val intent = Intent(applicationContext, GameIntroActivity::class.java)
                        intent.putExtra(Constants.CLASS, clas)
                        intent.putExtra(Constants.KEEPER, keeper)
                        intent.putExtra(Constants.PLAYER, player)
                        intent.putExtra(Constants.NAME, "$name")
                        intent.putExtra("oppName", "$oppName")
                        intent.putExtra(Constants.GAME_MODE, "multi_player")
                        intent.putExtra("1111", "$gameCode")
                        intent.putExtra("samyy", "$playFirst")
                        intent.putExtra("123", "$combineCode")
                        intent.putExtra("re", "$topic")
                        startActivity(intent)
                    }

                } else {
                    Toast.makeText(applicationContext, "Who is playing first?", Toast.LENGTH_SHORT).show()
                }

            }

        }
        timer?.start()
    }





}
