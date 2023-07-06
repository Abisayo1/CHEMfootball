package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


        binding.editTextName.setText("$name")
        binding.editTextGameCode.setText("$gameCode")
        binding.editTextComputer.setText("$oppName")

        binding.button1.setOnClickListener {
            val answerSpinner = binding.editTextWhoPlaying
            val selectedAnswer = answerSpinner.selectedItem as String

            when (selectedAnswer) {
                "You" -> {
                    playFirst = "$name"
                }
                "Opponent" -> {
                    playFirst = "$oppName"
                }
                
            }

            if (selectedAnswer != "Select First Player" ) {
            saveName("$name", "$oppName", "$jointCode", "$playFirst", player)
            saveNames("$name", "$oppName", "$gameCode", "$playFirst", player)
            val jointCode = intent.getStringExtra("123").toString()
            val intent = Intent(this, GameIntroActivity::class.java)
            intent.putExtra(Constants.CLASS, clas)
            intent.putExtra(Constants.KEEPER, keeper)
            intent.putExtra(Constants.PLAYER, player)
            intent.putExtra(Constants.NAME, "$name")
            intent.putExtra("oppName", "$oppName")
            intent.putExtra(Constants.GAME_MODE, "multi_player")
            intent.putExtra("1111", "$gameCode")
            intent.putExtra("samyy", "$playFirst")
            intent.putExtra("123", "$jointCode")
            intent.putExtra("re", "$topic")
            startActivity(intent)

        } else {
                Toast.makeText(this, "Who is playing first?", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "$code", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveNames(name : String, opp_name : String, code: String, whoPlayFirst : String, Player: String) {
        val scoreStatus = "0"
        val my_score = "0"
        val opponent_score = "0"
        val trialNum = 0
        val whoPlayFirst = whoPlayFirst
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        val multi_player = Multiplayer(name,opp_name, code, scoreStatus, my_score, opponent_score, trialNum, whoPlayFirst, Player)
        if (userID != null) {
            database.child(userID).setValue(multi_player).addOnSuccessListener {
                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
