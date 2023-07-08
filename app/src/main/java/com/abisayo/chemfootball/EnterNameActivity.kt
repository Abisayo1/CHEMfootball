package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityEnterNameBinding
import com.abisayo.chemfootball.displayAvailablePlayers.DisplayAvailablePlayersActivity
import com.abisayo.chemfootball.models.Player
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EnterNameActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    private var clas = "SS1"
    private var player = ""
    var named = "Computer"
    private lateinit var binding: ActivityEnterNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityEnterNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        val topic = intent.getStringExtra("re").toString()

        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        if (userID != null) {
            getName(userID)


        }



        binding.button.setOnClickListener {
                val intent = Intent(this, GameIntroActivity::class.java)
                intent.putExtra(Constants.CLASS, clas)
                intent.putExtra(Constants.KEEPER, keeper)
                intent.putExtra(Constants.PLAYER, player)
                intent.putExtra(Constants.NAME, named)
                intent.putExtra("re", "$topic")
                startActivity(intent)

        }

        binding.mulyiPlayer.setOnClickListener {
                saveName(named)
                val intent = Intent(this, DisplayAvailablePlayersActivity::class.java)
                intent.putExtra(Constants.CLASS, clas)
                intent.putExtra(Constants.KEEPER, keeper)
                intent.putExtra(Constants.PLAYER, player)
                intent.putExtra(Constants.NAME, named)
                intent.putExtra(Constants.GAME_MODE, "multi_player")
                intent.putExtra("re", "$topic")
                startActivity(intent)
        }
    }

    fun saveName(name : String) {
        val name = name
        val trialNum = "0-0"
        val score = "0-0"
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("Player")
        val player = Player("$name",trialNum, score)
            if (userID != null) {
                database.child(userID).setValue(player).addOnSuccessListener {
                    Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getName(code: String) {
        database = FirebaseDatabase.getInstance().getReference("PlayersCred")
        database.child(code).get().addOnSuccessListener {

            if (it.exists()) {

                val name = it.child("name").getValue(String::class.java)!!
                named = name
                binding.editText.setText(name)

            } else {
                Toast.makeText(this, "Could not find user", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }
}