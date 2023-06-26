package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.abisayo.chemfootball.databinding.ActivityEnterAdminPasswordBinding
import com.abisayo.chemfootball.manageLMS.ManageLMSActivity

class EnterAdminPassword : AppCompatActivity() {
    private lateinit var binding: ActivityEnterAdminPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityEnterAdminPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val password = binding.editTextTextPassword3.text

        binding.button.setOnClickListener {
            if (password.isNotEmpty()) {
                if ("$password" == "1loveu") {
                    val intent = Intent(this, ManageLMSActivity::class.java)
                    startActivity(intent)
                } else if ("$password" != "1loveu") {
                    Toast.makeText(this, "Access denied", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter password to gain access", Toast.LENGTH_SHORT).show()
            }
        }
    }
}