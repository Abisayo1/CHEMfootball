package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivitySelectClassBinding
import com.abisayo.chemfootball.manageLMS.ManageLMSActivity

class SelectClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SS1.setOnClickListener {
            val intent = Intent(this, DisplayCoursesActivity::class.java)
            intent.putExtra(Constants.CLASS, "SS1")
            startActivity(intent)
        }

        binding.SS2.setOnClickListener {
            val intent = Intent(this, DisplayCoursesActivity::class.java)
            intent.putExtra(Constants.CLASS, "SS2")
            startActivity(intent)
        }

        binding.SS3.setOnClickListener {
            val intent = Intent(this, DisplayCoursesActivity::class.java)
            intent.putExtra(Constants.CLASS, "SS3")
            startActivity(intent)
        }
        binding.manage.setOnClickListener {
            val intent = Intent(this, EnterAdminPassword::class.java)
            startActivity(intent)
        }
    }
}