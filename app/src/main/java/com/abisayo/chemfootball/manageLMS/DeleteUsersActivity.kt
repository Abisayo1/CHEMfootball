package com.abisayo.chemfootball.manageLMS

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.SelectClassActivity
import com.abisayo.chemfootball.databinding.ActivityDeleteUsersBinding
import com.abisayo.chemfootball.displayAvailablePlayers.DisplayAvailablePlayersActivity

class DeleteUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteUsersBinding
    var admin = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityDeleteUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.deleteUsers.setOnClickListener {
            admin = "Admin"
            val intent = Intent(this, DisplayAvailablePlayersActivity::class.java)
            intent.putExtra("Admin", admin)
            startActivity(intent)
        }

        binding.selectUsers.setOnClickListener {
            admin = "Select"
            val intent = Intent(this, SelectClassActivity::class.java)
            intent.putExtra("Admin", admin)
            startActivity(intent)
        }
    }
}