package com.abisayo.chemfootball.displaySS2Courses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.abisayo.chemfootball.Home2
import com.abisayo.chemfootball.R

class DisplaySS2CoursesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_ss2_courses)

        replaceFragment(Home2())
        Toast.makeText(this, "Topics loading...", Toast.LENGTH_LONG).show()
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()

    }

    fun getAdmin(): String {
        return intent.getStringExtra("Admin").toString()
    }
}