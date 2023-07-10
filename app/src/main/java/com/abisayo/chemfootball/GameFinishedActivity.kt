package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityDisplayAnswersBinding
import com.abisayo.chemfootball.databinding.ActivityGameFinishedBinding

class GameFinishedActivity : AppCompatActivity() {
    private var clas = "SS1"
    private lateinit var binding: ActivityGameFinishedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityGameFinishedBinding.inflate(layoutInflater)
        setContentView(binding.root)



        clas = intent.getStringExtra(Constants.CLASS).toString()
        val topic = intent.getStringExtra("re").toString()
        val userScore = intent.getStringExtra("scr").toString()
        val compScore = intent.getStringExtra("comp").toString()
        val name = intent.getStringExtra("name").toString()
        val oppName = intent.getStringExtra("opp").toString()

        binding.scoreV.text = userScore
        binding.scoreC.text = compScore

        binding.computerPlayer.text = oppName
        binding.namePlayer.text = name



        clas = intent.getStringExtra(Constants.CLASS).toString()

        binding.seeAnswers.setOnClickListener {
            val intent = Intent(this, DisplayAnswersActivity::class.java)
            intent.putExtra(Constants.CLASS, clas)
            intent.putExtra("re", "$topic")
            startActivity(intent)
        }

        binding.goHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}