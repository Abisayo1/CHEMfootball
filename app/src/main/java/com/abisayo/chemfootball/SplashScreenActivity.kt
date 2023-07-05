package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.abisayo.chemfootball.SelectPlayers.SelectPlayersActivity
import com.abisayo.chemfootball.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        if (firebaseAuth.currentUser != null) {

            binding.splash.alpha = 0f
            binding.splash.animate().setDuration(2000).alpha(1f).withEndAction {
                val intent = Intent(this, MainActivity::class.java)
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                startActivity(intent)
                finish()
            }

        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
}