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
        val playFirst = intent.getStringExtra("samyy").toString()
        val gameCode = intent.getStringExtra("1111").toString()
        val jointCode = intent.getStringExtra("123").toString()
        val oppName = intent.getStringExtra("oppName").toString()


        binding.editTextName.setText("$name")
        binding.editTextGameCode.setText("$gameCode")
        binding.editTextComputer.setText("$oppName")
        binding.editTextWhoPlaying.setText("$playFirst")

        Toast.makeText(this, "$name, $playFirst, $gameCode, $oppName", Toast.LENGTH_SHORT).show()

        binding.button1.setOnClickListener {
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
            startActivity(intent)

        }


        binding.button2.setOnClickListener {
            onBackPressed()
        }

    }


}
