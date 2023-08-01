package com.abisayo.chemfootball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment

class DisplayCoursesActivity : AppCompatActivity() {
    private var admin = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_courses)

        replaceFragment(Home())
        admin = intent.getStringExtra("Admin").toString()

        Toast.makeText(this, "Topics loading...", Toast.LENGTH_LONG).show()
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()

    }


    fun getAdmin(): String {
        return admin
    }
}