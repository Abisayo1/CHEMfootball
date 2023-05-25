package com.abisayo.chemfootball.SelectKeepers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager
import com.abisayo.chemfootball.R
import com.google.android.material.tabs.TabLayout

class SelectKeeperActivity : AppCompatActivity() {
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

    }
}

