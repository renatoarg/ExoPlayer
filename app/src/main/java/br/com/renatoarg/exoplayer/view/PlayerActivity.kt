package br.com.renatoarg.exoplayer.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import br.com.renatoarg.exoplayer.R
import br.com.renatoarg.exoplayer.model.MEDITATIONS.MEDITATION
import br.com.renatoarg.exoplayer.model.Meditation
import br.com.renatoarg.exoplayer.model.service.MyPlayerService
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*
import timber.log.Timber

class PlayerActivity : AppCompatActivity() {


    private lateinit var playerView: PlayerView
    private lateinit var mService: MyPlayerService
    private var mBound: Boolean = false
    private var mMeditation:Meditation? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Timber.d("onServiceConnected:")
            val binder = service as MyPlayerService.LocalBinder
            mService = binder.getService()
            mBound = true
//            playerView.player = mService.player
//            if (mMeditation != mService.currentMeditation)
//                startOrContinuePlaying()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

//    private fun startOrContinuePlaying() {
//        try {
//            Timber.d("startOrContinuePlaying:")
//            mService.currentMeditation.let {
//                mMeditation?.let {
//                    if (mMeditation != mService.currentMeditation) {
//                        mService.setMeditation(mMeditation!!, null!!)
//                    }
//                }
//
//            }
//        } catch (e: Exception) {
//            Timber.e(e, "startOrContinuePlaying:")
//
//        }
//    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume:")
        intent.getParcelableExtra<Meditation>(MEDITATION)?.let {
            mMeditation = intent.getParcelableExtra(MEDITATION)!!
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart:")
        Intent(this, MyPlayerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop:")
        unbindService(connection)
        mBound = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate:")
        setContentView(R.layout.activity_player)
        val intent = Intent(this, MyPlayerService::class.java)
        Util.startForegroundService(this, intent)
        playerView = player_view
    }

}