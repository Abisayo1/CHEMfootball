package com.abisayo.chemfootball.manageLMS

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.SelectClassActivity
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityManageQuestionsBinding
import com.abisayo.chemfootball.notes_affairs.NoteActivity

class ManageQuestionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageQuestionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityManageQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editQuestion.setOnClickListener {
            val intent = Intent(this, SelectClassActivity::class.java)
            intent.putExtra("Admin", "Admin")
            startActivity(intent)
        }

        binding.addQuestions.setOnClickListener {
            val intent = Intent(this, SelectQuestionClassActivity::class.java)
            startActivity(intent)
        }

        binding.saveQuestions.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }
    }
}