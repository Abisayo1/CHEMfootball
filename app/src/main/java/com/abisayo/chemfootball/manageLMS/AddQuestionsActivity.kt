package com.abisayo.chemfootball.manageLMS

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.databinding.ActivityAddQuestionsBinding

class AddQuestionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddQuestionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityAddQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}