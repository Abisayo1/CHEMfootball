package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityUploadPlayerDetailsBinding
import com.abisayo.chemfootball.models.Multiplayer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UploadPlayerDetailsActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    private var clas = "SS1"
    private var player = ""
    private var code = ""
    private lateinit var binding: ActivityUploadPlayerDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityUploadPlayerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        val name = intent.getStringExtra(Constants.NAME)

        val  userID = FirebaseAuth.getInstance().currentUser?.uid


        val computer_name = binding.editTextComputer.text
        val game_code = binding.editTextGameCode.text
        val whoPlayFirst = binding.editTextWhoPlaying.text
        val myName = binding.editTextName.text


        binding.editTextName.setText("$name")
        binding.myCode.setText("Your special code is:\n$userID")


        binding.btnSubmit.setOnClickListener {
            if ("$whoPlayFirst" == "$name") {
                code = "$userID + $game_code"
            } else if ("$whoPlayFirst" == "$computer_name") {
                code = "$game_code + $userID"
            }
            if (computer_name.isNotBlank() && binding.editTextName.text.isNotBlank()) {
                if ("$whoPlayFirst" != "$computer_name" && "$whoPlayFirst" != "$name"){
                    Toast.makeText(this, "Select a valid player to start first", Toast.LENGTH_SHORT).show()
                } else {
                    saveName("$name", "$computer_name", "$code", "$whoPlayFirst")
                    saveNames("$name", "$computer_name", "$game_code", "$whoPlayFirst")
                    val intent = Intent(this, ConfirmDetailsActivity::class.java)
                    intent.putExtra(Constants.CLASS, clas)
                    intent.putExtra(Constants.KEEPER, keeper)
                    intent.putExtra(Constants.PLAYER, player)
                    intent.putExtra(Constants.NAME, "$name")
                    intent.putExtra("oppName", "$computer_name")
                    intent.putExtra(Constants.GAME_MODE, "multi_player")
                    intent.putExtra("1111", "$game_code")
                    intent.putExtra("samyy", "$whoPlayFirst")
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Please enter your opponent name", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun saveName(name : String, opp_name : String, code: String, whoPlayFirst : String) {
        val scoreStatus = "0"
        val my_score = "0"
        val opponent_score = "0"
        val trialNum = "0"
        val whoPlayFirst = whoPlayFirst
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        val multi_player = Multiplayer(name,opp_name, code, scoreStatus, my_score, opponent_score, trialNum, whoPlayFirst)
        if (userID != null) {
            database.child(code).setValue(multi_player).addOnSuccessListener {
                Toast.makeText(this, "$code", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveNames(name : String, opp_name : String, code: String, whoPlayFirst : String) {
        val scoreStatus = "0"
        val my_score = "0"
        val opponent_score = "0"
        val trialNum = "0"
        val whoPlayFirst = whoPlayFirst
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("Multiplayer")
        val multi_player = Multiplayer(name,opp_name, code, scoreStatus, my_score, opponent_score, trialNum, whoPlayFirst)
        if (userID != null) {
            database.child(userID).setValue(multi_player).addOnSuccessListener {
                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
