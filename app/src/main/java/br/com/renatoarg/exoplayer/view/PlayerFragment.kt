package br.com.renatoarg.exoplayer.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.renatoarg.exoplayer.R
import br.com.renatoarg.exoplayer.model.MEDITATIONS.MEDITATION
import br.com.renatoarg.exoplayer.model.Meditation
import br.com.renatoarg.exoplayer.model.service.MyPlayerService
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.fragment_player.view.*
import timber.log.Timber
import java.lang.ref.WeakReference

class PlayerFragment : Fragment() {
    private lateinit var mService: MyPlayerService
    private var mContext = WeakReference<Context>(this.context)
    private var meditation: Meditation? = null
    private var listener: PlayerFragmentListener? = null
    private var mBound: Boolean = false



    interface PlayerFragmentListener {
        fun onClosePlayerFragment()
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart:")
        Intent(activity, MyPlayerService::class.java).also { intent ->
            activity!!.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            intent.putExtra(MEDITATION, meditation)
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Timber.d("onServiceConnected:")
            val binder = service as MyPlayerService.LocalBinder
            mService = binder.getService()
            mBound = true
            player_view.player = mService.player
            player_view.showController()
//            playerView.controllerShowTimeoutMs = 0
//            if (meditation != mService.currentMeditation)
//                startOrContinuePlaying()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Timber.d("onServiceDisconnected:")
            mBound = false
        }
    }

//    private fun startOrContinuePlaying() {
//        try {
//            Timber.d("startOrContinuePlaying:")
//            mService.currentMeditation.let {
//                meditation?.let {
//                    if (meditation != mService.currentMeditation) {
//                        mService.setMeditation(meditation!!, mImage)
//                    }
//                }
//
//            }
//        } catch (e: Exception) {
//            Timber.e(e, "startOrContinuePlaying:")
//
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate:")
        arguments?.let {
            meditation = arguments!!.getParcelable(MEDITATION)
            Timber.d("==>> MEDITATION: %s", meditation!!.image_url)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView:")
        val view = inflater.inflate(R.layout.fragment_player, container, false)
        view.iv_close.setOnClickListener{
            listener!!.onClosePlayerFragment()
        }
//        tvTitleFragmentPlayer = view.findViewById(R.id.tv_title_fragment_player)
//        ivImagePlayer         = view.findViewById(R.id.iv_fragment_player)
//        playerView            = view.findViewById(R.id.player_view)
//        tvTitleFragmentPlayer.text = meditation!!.title
//
//        var target = object :Target {
//            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//
//            }
//
//            override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
//
//            }
//
//            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                ivImagePlayer.setImageBitmap(bitmap)
//                mImage = bitmap!!
//            }
//
//        }
//        Picasso.get().load(meditation!!.image_url).into(target)
        val intent = Intent(activity, MyPlayerService::class.java)
        intent.putExtra(MEDITATION, meditation)
        Util.startForegroundService(activity, intent)
        return view
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume:")
    }

    fun hide() {
        fl_fragment_player.visibility = View.GONE
    }

    fun show() {
        fl_fragment_player.visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("onAttach:")
        if (context is PlayerFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement MinimizedFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach:")
        listener = null
    }

    override fun onDestroyView() {
        Timber.d("onDestroyView:")
        super.onDestroyView()
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop:")
        activity!!.unbindService(connection)
        mBound = false
    }



    companion object {
        @JvmStatic
        fun newInstance(meditation: Meditation) =
            PlayerFragment().apply {
                Timber.d("newInstance:")
                arguments = Bundle().apply {
                    putParcelable(MEDITATION, meditation)
                }
            }
    }
}

