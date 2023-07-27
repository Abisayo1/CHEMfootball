package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivitySelectClassBinding
import com.abisayo.chemfootball.displaySS2Courses.DisplaySS2CoursesActivity
import com.abisayo.chemfootball.displaySS3courses.DisplaySS3CoursesActivity

class SelectClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val admin = intent.getStringExtra("Admin").toString()

        if (admin == "Admin") {
            binding.addMe.isVisible = false
            binding.manage.isVisible = false
        }




        binding.SS1.setOnClickListener {
            val intent = Intent(this, DisplayCoursesActivity::class.java)
            intent.putExtra(Constants.CLASS, "SS1")
            intent.putExtra("Admin", admin)
            startActivity(intent)
        }

        binding.SS2.setOnClickListener {
            val intent = Intent(this, DisplaySS2CoursesActivity::class.java)
            intent.putExtra(Constants.CLASS, "SS2")
            intent.putExtra("Admin", admin)
            startActivity(intent)
        }

        binding.SS3.setOnClickListener {
            val intent = Intent(this, DisplaySS3CoursesActivity::class.java)
            intent.putExtra(Constants.CLASS, "SS3")
            intent.putExtra("Admin", admin)
            startActivity(intent)
        }

        binding.addMe.setOnClickListener {
            val intent = Intent(this, DisplaySpecialCodeActivity::class.java)
            startActivity(intent)
        }

        binding.manage.setOnClickListener {
            val intent = Intent(this, EnterAdminPassword::class.java)
            startActivity(intent)
        }
    }
}