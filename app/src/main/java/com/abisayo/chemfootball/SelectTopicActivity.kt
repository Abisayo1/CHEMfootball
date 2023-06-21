package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.abisayo.chemfootball.SelectPlayers.SelectPlayersActivity
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityEnterNameBinding
import com.abisayo.chemfootball.databinding.ActivitySelectClassBinding
import com.abisayo.chemfootball.databinding.ActivitySelectTopicBinding

class SelectTopicActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectTopicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySelectTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val clas = intent.getStringExtra(Constants.CLASS)!!

        val topic = binding.editText.text

        binding.button.setOnClickListener {
            val intent = Intent(this, SelectPlayersActivity::class.java)
            intent.putExtra(Constants.CLASS, "$clas")
            intent.putExtra("re", topic)
            startActivity(intent)
        }
    }
}