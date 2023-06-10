package com.abisayo.chemfootball.manageLMS

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityManageLmsBinding

class ManageLMSActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageLmsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityManageLmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addQuestions.setOnClickListener {
            val intent = Intent(this, AddQuestionsActivity::class.java)
            intent.putExtra(Constants.CLASS, "SS3")
            startActivity(intent)
        }

        binding.checkStudentScores.setOnClickListener {
            val intent = Intent(this, DisplayStudentScoresActivity::class.java)
            startActivity(intent)
        }
    }
}