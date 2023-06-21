package com.abisayo.chemfootball.SelectKeepers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.data.Constants
import com.google.android.material.tabs.TabLayout

class SelectKeeperActivity : AppCompatActivity() {
    private var clas = "SS1"
    private var player = ""
    private var topic = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_select_keeper)

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = ViewpagerAdapter1(supportFragmentManager)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)


        clas = intent.getStringExtra(Constants.CLASS).toString()
        player = intent.getStringExtra(Constants.PLAYER).toString()
        topic = intent.getStringExtra("re").toString()

    }

    fun getClass(): String {
        return clas
    }

    fun getPlayer(): String {
        return player
    }

    fun getTopic(): String {
        return topic
    }

}

