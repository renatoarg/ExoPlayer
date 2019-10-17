package br.com.renatoarg.exoplayer

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ui.PlayerView

class MainActivity : AppCompatActivity() {

    private val mediaPlayer = MediaPlayerImpl()
    private lateinit var videoView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mediaPlayer.releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaPlayer.releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.setMediaSessionState(false)
    }

    private fun init() {
        val videoUrl = "URL"
        videoView = findViewById(R.id.ep_video_view)
        videoView.player = mediaPlayer.getPlayerImpl(this)
        mediaPlayer.play(videoUrl)
    }
}