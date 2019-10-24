package br.com.renatoarg.exoplayer.model.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import br.com.renatoarg.exoplayer.R
import br.com.renatoarg.exoplayer.model.MEDITATIONS
import br.com.renatoarg.exoplayer.model.Meditation
import br.com.renatoarg.exoplayer.view.MainActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import timber.log.Timber
import java.lang.ref.WeakReference

class MyPlayerService : Service() {

    lateinit var currentMeditation:Meditation
    lateinit var player: SimpleExoPlayer
    private var playerState: Int = -1
    lateinit var dataSourceFactory: DefaultDataSourceFactory
    private var trackSelector = DefaultTrackSelector()
    private val binder = LocalBinder()
    private val context = WeakReference<Context>(this)

    private var mImage: Bitmap? = null
    private lateinit var mediaSource:ExtractorMediaSource
    lateinit var playerNotificationManager: PlayerNotificationManager

//    fun setMeditation(
//        meditation: Meditation,
//        mImage: Bitmap?
//    ) {
//        Timber.d("setMeditation:")
//        currentMeditation = meditation
//        player.stop()
//        mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(meditation.uri))!!
//        this.mImage = mImage!!
//        playMedia()
//        startNotification()
//    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate:")
        this.mImage = null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand:")
        intent?.let {
            var meditation = it.getParcelableExtra(MEDITATIONS.MEDITATION) as Meditation
            when(::currentMeditation.isInitialized) {
                true -> {
                    Timber.d("currentMediation initialized:")
                    meditation?.let {
                        if (currentMeditation == meditation) {
                            Timber.d("MEDIA ${currentMeditation.title} (SAME MEDIA):")
                        } else {
                            Timber.d("MEDIA ${meditation.title} (DIFFERENT MEDIA):")
                            this.player.stop()
                            this.player.release()
                            this.playerNotificationManager.setPlayer(null)
                            playMedia(meditation)
                        }
                    }
                }
                false -> {
                    Timber.d("currentMediation NOT initialized:")
                    meditation?.let {
                        playMedia(meditation)
                    }
                }
            }
        }
        return START_STICKY
    }


    private fun startNotification() {
        Timber.d("startNotification:")
        playerNotificationManager.setNotificationListener(mNotificationListener())
        playerNotificationManager.setPlayer(player)
    }

    private fun mNotificationListener(): PlayerNotificationManager.NotificationListener {
        Timber.d("mNotificationListener:")
        return object :
            PlayerNotificationManager.NotificationListener {
            override fun onNotificationCancelled(notificationId: Int) {
                stopSelf()
            }

            override fun onNotificationStarted(notificationId: Int, notification: Notification?) {
                startForeground(notificationId, notification)
            }
        }
    }

    private fun playMedia(meditation:Meditation) {
        Timber.d("playMedia:")
        this.currentMeditation = meditation
        this.dataSourceFactory = DefaultDataSourceFactory(context.get(), "exo-player-test")
        this.mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(currentMeditation.uri))!!
        this.player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        this.player.addListener(mPlayerEventListener())
        this.player.prepare(mediaSource, true, true)
        this.player.playWhenReady = true
        this.playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            "Exoplayer Test channel id",
            R.string.exo_player_test,
            1000,
            getMediaDescriptionAdapter()
        )
        startNotification()
    }

    private fun getMediaDescriptionAdapter(): PlayerNotificationManager.MediaDescriptionAdapter {
        Timber.d("getMediaDescriptionAdapter:")
        return object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun createCurrentContentIntent(player: Player?): PendingIntent? {
                val intent = Intent(context.get(), MainActivity::class.java)
                return PendingIntent.getActivity(
                    context.get(),
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            override fun getCurrentContentText(player: Player?): String? {
                return currentMeditation!!.description
            }

            override fun getCurrentContentTitle(player: Player?): String {
                return currentMeditation!!.title
            }

            override fun getCurrentLargeIcon(
                player: Player?,
                callback: PlayerNotificationManager.BitmapCallback?
            ): Bitmap? {
                mImage.let {
                    return mImage
                }
                return null
            }

        }
    }

    private fun mPlayerEventListener(): Player.EventListener {
        Timber.d("mPlayerEventListener:")
        return playerEventListener()
    }

    private fun playerEventListener(): Player.EventListener {
        Timber.d("playerEventListener:")
        return mEventListener()
    }

    private fun mEventListener(): Player.EventListener {
        Timber.d("mEventListener:")
        return object : Player.EventListener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
                Timber.d("onPlaybackParametersChanged:")
            }

            override fun onSeekProcessed() {
                Timber.d("onSeekProcessed:")
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray?,
                trackSelections: TrackSelectionArray?
            ) {
                Timber.d("onTracksChanged: %s", player.currentWindowIndex)
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
                Timber.e(error, "onPlayerError:")
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                Timber.d("onLoadingChanged: isLoading: %s", isLoading)
            }

            override fun onPositionDiscontinuity(reason: Int) {
                Timber.d("onPositionDiscontinuity: reason: %s", reason)
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                Timber.d("onRepeatModeChanged: repeatMode: %s", repeatMode)
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                Timber.d("onShuffleModeEnabledChanged: shuffleModeEnabled: %s", shuffleModeEnabled)
            }

            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
                Timber.d(
                    "onTimelineChanged: timeline: %s, manifest: %s, reason: %s",
                    timeline,
                    manifest.toString(),
                    reason
                )
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                Timber.d("onPlayerStateChanged: playbackState: %s", getPlayerState(playbackState))
            }

        }
    }

    private fun getPlayerState(playbackState: Int): String {
        playerState = playbackState
        return when (playbackState) {
            Player.STATE_IDLE -> "STATE_IDLE"
            Player.STATE_READY -> "STATE_READY"
            Player.STATE_BUFFERING -> "STATE_BUFFERING"
            Player.STATE_ENDED -> "STATE_ENDED"
            ExoPlayer.DISCONTINUITY_REASON_PERIOD_TRANSITION -> "DISCONTINUITY_REASON_PERIOD_TRANSITION"
            else -> "UNKNOWN"
        }
    }

    override fun onDestroy() {
        Timber.d("onDestroy:")
        this.playerNotificationManager.setPlayer(null)
        this.player.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("onBind:")
        return binder
    }

    fun setPlayerDataSourceFactory(dataSourceFactory: DefaultDataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory
    }


    inner class LocalBinder : Binder() {
        fun getService() = this@MyPlayerService
    }

}