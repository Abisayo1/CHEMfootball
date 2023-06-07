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
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatDelegate
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityGamePlayBinding


class GamePlayActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    var mMediaPlayer: MediaPlayer? = null
    private lateinit var binding: ActivityGamePlayBinding
    var score = 0
    var trialNum = 0
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityGamePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openDialog()

        player = intent.getStringExtra(Constants.PLAYER).toString()
        clas = intent.getStringExtra(Constants.CLASS).toString()
        val keeper = intent.getStringExtra(Constants.KEEPER).toString()

        Toast.makeText(this, "$player, $clas,  $keeper", Toast.LENGTH_SHORT).show()

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
        val thirdBtn = dialogLayoutBinding.findViewById<TextView>(R.id.thridbtn)
        val fourthBtn = dialogLayoutBinding.findViewById<TextView>(R.id.fourthbtn)
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

        thirdBtn.setOnClickListener {
            mydialog.dismiss()
            playVideo("c")
        }

        fourthBtn.setOnClickListener {
            mydialog.dismiss()
            playVideo("d")
        }



    }

    fun playVideo(option: String) {
        var play = R.raw.messi
        var play_misses = R.raw.messi_misses

        var keep = R.raw.allison_keeper_wins
        var keep_misses = R.raw.alisson_keeper_misses
        binding.videoView.visibility = View.VISIBLE

        videoView = binding.videoView

            when (player){
                "Fragment1" -> {
                    play = R.raw.ronaldo
                    play_misses = R.raw.ronaldo_misses
                }
                "Fragment2" -> {
                    play = R.raw.messi
                    play_misses = R.raw.messi_misses
                }
                "Fragment3" -> {
                    play = R.raw.mbappe
                    play_misses = R.raw.mbappe_misses
                }
                "Fragment4" -> {
                    play = R.raw.neymar
                    play_misses = R.raw.neymar_misses
                }
                "Fragment5" -> {
                    play = R.raw.rashford
                    play_misses = R.raw.rashford_misses
                }
                "Fragment6" -> {
                    play_misses = R.raw.de_bruyen_misses
                    play = R.raw.de_bruyen

                }
                "Fragment7" -> {
                    play = R.raw.messi
                    play_misses = R.raw.messi_misses
                }
                "Fragment8" -> {
                    play = R.raw.haaland
                    play_misses = R.raw.messi_misses
                }

            }
        if (option == "b") {
            playVideo(play)

        }  else if (option == "a" || option == "c" || option == "d") {
            VideoMissPlay(play_misses)

        }


        videoView.setOnCompletionListener {
            // Video playback completed
            NextQuestion()
        }
    }

    private fun VideoMissPlay(play_misses: Int) {
        val videoUri =
            Uri.parse("android.resource://" + packageName + "/" + play_misses) // Replace with your video file or URL
        PlayVideoM().setVideoURI(videoUri)

        videoView.start()
    }

    private fun playVideo(play: Int) {
        val videoUri =
            Uri.parse("android.resource://" + packageName + "/" + play) // Replace with your video file or URL
        videoView.setVideoURI(videoUri)

        videoView.start()
    }

    private fun PlayVideoM() = videoView

    private fun NextQuestion() {
        binding.background.visibility = View.VISIBLE
        binding.videoView.visibility = View.GONE
        openDialog()
    }

}