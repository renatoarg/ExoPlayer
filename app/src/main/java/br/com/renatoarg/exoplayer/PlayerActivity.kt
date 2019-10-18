package br.com.renatoarg.exoplayer

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ui.PlayerView

class PlayerActivity : AppCompatActivity(), PlayerControlDispatcher.ActivityInterface {

    override fun onPlayBtnClicked() {
        Toast.makeText(this@PlayerActivity, "play", Toast.LENGTH_SHORT).show()
    }

    override fun onPauseBtnClicked() {
        Toast.makeText(this@PlayerActivity, "pause", Toast.LENGTH_SHORT).show()
    }

    private val mediaPlayer = MediaPlayerImpl()
    private lateinit var videoView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
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
        val videoUrl = intent.getStringExtra("uri")
        videoView = findViewById(R.id.ep_video_view)
        videoView.setControlDispatcher(PlayerControlDispatcher(this@PlayerActivity))
        videoView.player = mediaPlayer.getPlayerImpl(this)
        mediaPlayer.play(videoUrl)
    }
}