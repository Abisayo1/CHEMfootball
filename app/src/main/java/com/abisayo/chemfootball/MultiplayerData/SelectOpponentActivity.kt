package com.abisayo.chemfootball.MultiplayerData

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.StudentScores
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivitySelectOpponentBinding

class SelectOpponentActivity : AppCompatActivity() {
    private var clas = "SS1"
    private var player = ""
    private lateinit var binding: ActivitySelectOpponentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectOpponentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        replaceFragment(MultiplayerData())

        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        val game_mode = intent.getStringExtra(Constants.GAME_MODE).toString()

        val name = intent.getStringExtra(Constants.NAME).toString()
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()

    }
}