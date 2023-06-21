package com.abisayo.chemfootball.SelectPlayers


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.data.Constants
import com.google.android.material.tabs.TabLayout

class SelectPlayersActivity : AppCompatActivity() {
    private var clas = "SS1"
    private var topic = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_select_players)

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = ViewpagerAdapter2(supportFragmentManager)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)

        clas = intent.getStringExtra(Constants.CLASS)!!
        topic = intent.getStringExtra("re")!!

    }

    fun getClass(): String {
        return clas
    }

    fun getTopic(): String {
        return topic
    }


}