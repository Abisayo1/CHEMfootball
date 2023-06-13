package com.abisayo.chemfootball

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.core.content.ContentProviderCompat.requireContext
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityGameIntroBinding

class GameIntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameIntroBinding
    private lateinit var videoView: VideoView
    private var clas = "SS1"
    private var player = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityGameIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoView = binding.videoView

        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.chemfootball_intro) // Replace with your video file or URL
        videoView.setVideoURI(videoUri)

//        val mediaController = MediaController(this)
//        videoView.setMediaController(mediaController)

        videoView.start()

        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        val game_mode = intent.getStringExtra(Constants.GAME_MODE).toString()
        val gameCode = intent.getStringExtra("1111").toString()
        val oppName = intent.getStringExtra("oppName").toString()
        val playFirst = intent.getStringExtra("samyy").toString()

        val name = intent.getStringExtra(Constants.NAME).toString()




        binding.screen.setOnClickListener {
         videoView.stopPlayback()
            startNextActivity(game_mode)
        }

        videoView.setOnCompletionListener {
            // Video playback completed
            startNextActivity(game_mode)
        }
    }

    private fun startNextActivity(gameMode: String) {
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()
        val name = intent.getStringExtra(Constants.NAME).toString()
        val gameCode = intent.getStringExtra("1111").toString()
        val oppName = intent.getStringExtra("oppName").toString()
        val playFirst = intent.getStringExtra("samyy").toString()
        val intent = Intent(this, GamePlayActivity::class.java)
        intent.putExtra(Constants.CLASS, clas)
        intent.putExtra(Constants.KEEPER, keeper)
        intent.putExtra(Constants.PLAYER, player)
        intent.putExtra(Constants.NAME, name)
        intent.putExtra(Constants.GAME_MODE, gameMode)
        intent.putExtra("1111", gameCode)
        intent.putExtra("samyy", playFirst)
        intent.putExtra("oppName", oppName)
        startActivity(intent)
        finish() // Optional: Finish the current activity if needed
    }
}