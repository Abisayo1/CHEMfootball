package com.abisayo.chemfootball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatDelegate
import com.abisayo.chemfootball.databinding.ActivityGamePlayBinding


class GamePlayActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    var mMediaPlayer: MediaPlayer? = null
    private lateinit var binding: ActivityGamePlayBinding
    var i = 0
    var trialNum = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityGamePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openDialog()


    }


    // 1. Plays the water sound
    fun playSound(sound: Int) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, sound)
            mMediaPlayer!!.isLooping = false
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()

    }

    // 2. Pause playback
    fun pauseSound() {
        if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
    }

    // 3. Stops playback
    fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    // 4. Destroys the MediaPlayer instance when the app is closed
    override fun onStop() {
        super.onStop()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }


    fun deactivate(button: Button) {
        button.isClickable = false

    }

    @SuppressLint("SetTextI18n")
    fun openDialog() {
        val dialogLayoutBinding = layoutInflater.inflate(R.layout.dialog_layout, null)
        val question = dialogLayoutBinding.findViewById<TextView>(R.id.tv_login)
        val secondBtn = dialogLayoutBinding.findViewById<TextView>(R.id.secondbtn)
        val firstBtn = dialogLayoutBinding.findViewById<TextView>(R.id.firstbtn)
        val mydialog = Dialog(this)
        mydialog.setContentView(dialogLayoutBinding)
        mydialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mydialog.setCancelable(true)
        mydialog.show()


        firstBtn.setOnClickListener {
            mydialog.dismiss()
            playVideo("a")

        }

        secondBtn.setOnClickListener {
            mydialog.dismiss()
            playVideo("b")
        }



    }

    fun playVideo(option: String) {
        binding.videoView.visibility = View.VISIBLE

        videoView = binding.videoView

        if (option == "a") {
            val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.messi) // Replace with your video file or URL
            videoView.setVideoURI(videoUri)

            videoView.start()

        } else if (option == "b") {
            val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.messi_misses) // Replace with your video file or URL
            videoView.setVideoURI(videoUri)

            videoView.start()

        }


        videoView.setOnCompletionListener {
            // Video playback completed
            NextQuestion()
        }
    }

    private fun NextQuestion() {
        binding.background.visibility = View.VISIBLE
        binding.videoView.visibility = View.GONE
        openDialog()
    }

}