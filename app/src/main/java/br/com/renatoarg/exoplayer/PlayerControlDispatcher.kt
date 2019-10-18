package br.com.renatoarg.exoplayer

import android.content.Context
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.Player
import timber.log.Timber

class PlayerControlDispatcher(val context: Context) : DefaultControlDispatcher() {

    lateinit var mInterface : ActivityInterface

    interface ActivityInterface {
        fun onPlayBtnClicked()
        fun onPauseBtnClicked()
    }


    override fun dispatchSetPlayWhenReady(player: Player?, playWhenReady: Boolean): Boolean {
        mInterface = context as PlayerActivity
        if(playWhenReady) {
            Timber.d("play")
            mInterface.onPlayBtnClicked()
        } else {
            Timber.d("pause:")
            mInterface.onPauseBtnClicked()
        }
        return super.dispatchSetPlayWhenReady(player, playWhenReady)
    }
}