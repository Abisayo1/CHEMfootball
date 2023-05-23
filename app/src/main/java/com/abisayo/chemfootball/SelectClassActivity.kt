package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abisayo.chemfootball.databinding.ActivitySelectClassBinding

class SelectClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SS1.setOnClickListener {
            val intent = Intent(this, SelectPlayersActivity::class.java)
            startActivity(intent)
        }
    }
}