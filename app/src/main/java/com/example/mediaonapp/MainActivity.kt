package com.example.mediaonapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler= Handler(Looper.getMainLooper()) //loopar a sick bar

        mediaPlayer = MediaPlayer.create(this, R.raw.testesong)
        seekBar = findViewById(R.id.sk_seekBar)
        val player = findViewById<Button>(R.id.bt_play)


        player.setOnClickListener {
            if (mediaPlayer == null || !mediaPlayer!!.isPlaying) {
                mediaPlayer?.start() //start music if mediaPlayer is null or not playing
                initSeekBar()
            } else {
                mediaPlayer?.pause() //music is playing, so when we press the button again its pause()
                handler.removeCallbacks(runnable)
            }
        }

    }

    private fun initSeekBar(){
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
               if(fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        // Displaying the time
        val tvPlayed = findViewById<TextView>(R.id.tv_played)
        val tvTotalPlay = findViewById<TextView>(R.id.tv_totalPlay)
        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition
            handler.postDelayed(runnable, 10)

            // Displaying elapsed time in the format 0:00
            val playedTime = mediaPlayer!!.currentPosition / 1000
            tvPlayed.text = String.format("%d:%02d", playedTime / 60, playedTime % 60)

            // Displaying total time in minutes and seconds (e.g., 01:46)
            val duration = mediaPlayer!!.duration / 1000
            val minutes = TimeUnit.SECONDS.toMinutes(duration.toLong())
            val seconds = duration % 60
            tvTotalPlay.text = String.format("%02d:%02d", minutes, seconds)
        }
        handler.postDelayed(runnable, 1000)
    }


    override fun onDestroy() {
        super.onDestroy()
        // Release the media player when the activity is destroyed
        mediaPlayer?.release()

    }
}