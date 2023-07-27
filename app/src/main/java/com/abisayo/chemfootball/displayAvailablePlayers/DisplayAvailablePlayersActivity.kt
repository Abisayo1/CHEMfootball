package com.abisayo.chemfootball.displayAvailablePlayers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.data.Constants

class DisplayAvailablePlayersActivity : AppCompatActivity() {
    private var clas = "SS1"
    private var player = ""
    private var name = "abisayo"
    private var topic = "theis is a topic"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_available_players)

        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()

        name = intent.getStringExtra(Constants.NAME).toString()
        topic = intent.getStringExtra("re").toString()


        replaceFragment(Homei())
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()

    }

    fun getClass(): String {
        return clas
    }

    fun getPLayer(): String {
        return player
    }

    fun getName(): String {
        return name
    }

    fun getTopic(): String {
        return topic
    }
}