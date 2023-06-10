package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityEnterNameBinding

class EnterNameActivity : AppCompatActivity() {
    private var clas = "SS1"
    private var player = ""
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

        val name = binding.editText.text


        binding.button.setOnClickListener {
            if (name.isNotEmpty()) {
                val intent = Intent(this, GameIntroActivity::class.java)
                intent.putExtra(Constants.CLASS, clas)
                intent.putExtra(Constants.KEEPER, keeper)
                intent.putExtra(Constants.PLAYER, player)
                intent.putExtra(Constants.NAME, "$name")
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }

        }
    }
}