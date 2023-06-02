package com.abisayo.chemfootball

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.MediaController
import android.widget.VideoView
import com.abisayo.chemfootball.databinding.ActivityGameIntroBinding

class GameIntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameIntroBinding
    private lateinit var videoView: VideoView
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

        binding.screen.setOnClickListener {
         videoView.stopPlayback()
            startNextActivity()
        }

        videoView.setOnCompletionListener {
            // Video playback completed
            startNextActivity()
        }
    }

    private fun startNextActivity() {
        val intent = Intent(this, GamePlayActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the current activity if needed
    }
}