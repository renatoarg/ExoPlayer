package br.com.renatoarg.exoplayer

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
import br.com.renatoarg.exoplayer.model.MEDITATIONS
import br.com.renatoarg.exoplayer.model.Meditation
import br.com.renatoarg.exoplayer.model.service.MyPlayerService
import kotlinx.android.synthetic.main.fragment_minimized.view.*
import kotlinx.android.synthetic.main.fragment_player.*
import timber.log.Timber
import java.lang.ref.WeakReference

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MinimizedFragment : Fragment() {

    private var listener: MinimizedFragmentListener? = null
    private lateinit var mService: MyPlayerService
    private var mContext = WeakReference<Context>(this.context)
    private var meditation: Meditation? = null
    private var mBound: Boolean = false

    interface MinimizedFragmentListener {
        fun showPlayerFragment()
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart:")
        Intent(activity, MyPlayerService::class.java).also { intent ->
            activity!!.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            intent.putExtra(MEDITATIONS.MEDITATION, meditation)
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


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("onAttach:")
        if (context is MinimizedFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement MinimizedFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate:")
        arguments?.let {
            meditation = arguments!!.getParcelable(MEDITATIONS.MEDITATION)
            Timber.d("==>> MEDITATION: %s", meditation!!.image_url)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView:")
        val view = inflater.inflate(R.layout.fragment_minimized, container, false)
        view.minimized_layout.setOnClickListener {
            Timber.d("click:")
            listener!!.showPlayerFragment()
        }
        return view
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop:")
    }


    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach:")
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView:")

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MinimizedFragment().apply {
                Timber.d("newInstance:")
                arguments = Bundle().apply {
                    putParcelable(MEDITATIONS.MEDITATION, meditation)
                }
            }
    }
}
