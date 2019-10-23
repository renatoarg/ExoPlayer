package br.com.renatoarg.exoplayer.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.renatoarg.exoplayer.MinimizedFragment
import br.com.renatoarg.exoplayer.R
import br.com.renatoarg.exoplayer.model.MEDITATIONS
import br.com.renatoarg.exoplayer.model.Meditation
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity(), MeditationsAdapter.MeditationsAdapterInterface,
    PlayerFragment.PlayerFragmentListener, MinimizedFragment.MinimizedFragmentListener {

    override fun onClosePlayerFragment() {
        Timber.d("onClosePlayerFragment:")
        showPlayerFragment()
    }

    override fun showPlayerFragment() {
        Timber.d("showPlayerFragment:")
        expandBottomSheet(true)
        replaceFragmentBy(PlayerFragment())
    }

    private fun replaceFragmentBy(fragment: Fragment) {
        Timber.d("replaceFragmentBy:")
        val mFt: FragmentTransaction = supportFragmentManager.beginTransaction()
        mFt.replace(R.id.rl_bottomsheet_fragment_container, fragment)
        mFt.commit()
    }

    private val mMeditationsAdapter = MeditationsAdapter(this, ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate:")
        setContentView(R.layout.activity_main)

        val mMeditations = rv_meditations
        mMeditations.layoutManager = LinearLayoutManager(this)
        mMeditations.adapter = mMeditationsAdapter
        mMeditationsAdapter.update(MEDITATIONS.meditations)
    }


    override fun onMeditationRowClicked(meditation: Meditation) {
        Timber.d("onMeditationRowClicked:")
        expandBottomSheet(true)
        replaceFragmentBy(PlayerFragment.newInstance(meditation))
    }

    override fun onBackPressed() {
        Timber.d("onBackPressed:")
        if (bottom_sheet.measuredHeight > 621) {
            showMinimizedFragment()
        } else {
            super.onBackPressed()
        }
    }

    private fun showMinimizedFragment() {
        Timber.d("showMinimizedFragment:")
        expandBottomSheet(false)
        replaceFragmentBy(MinimizedFragment())
    }


    private fun expandBottomSheet(expand: Boolean) {
        Timber.d("expandBottomSheet: ")
        try {
            var height = 620

            if (expand) {
                val displayMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                height = displayMetrics.heightPixels - 80
                bottom_sheet.visibility = View.VISIBLE
            }

            val anim = ValueAnimator.ofInt(bottom_sheet.getMeasuredHeight(), height)

            anim.addUpdateListener { valueAnimator ->
                val `val` = valueAnimator.animatedValue as Int
                val layoutParams = bottom_sheet.getLayoutParams()
                layoutParams.height = `val`
                bottom_sheet.setLayoutParams(layoutParams)
            }

            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    Timber.d("onAnimationStart: ")
                }

                override fun onAnimationEnd(animation: Animator) {
                    Timber.d("onAnimationEnd: ")
                    val layoutParams = bottom_sheet.getLayoutParams()
                    layoutParams.height = height
                    bottom_sheet.setLayoutParams(layoutParams)
                    if (height == 0) {
                        bottom_sheet.visibility = View.GONE
                    } else {
                        bottom_sheet.visibility = View.VISIBLE
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                    Timber.d("onAnimationCancel: ")
                }

                override fun onAnimationRepeat(animation: Animator) {
                    Timber.d("onAnimationRepeat: ")
                }
            })
            anim.duration = 350
            anim.start()
        } catch (e: Exception) {
            Timber.e(e, "expandBottomSheet: ")
        }

    }

//    private fun replaceFragment(meditation: Meditation) {
//        Timber.d("replaceFragment:%s", supportFragmentManager.fragments.size)
//        mPlayerFragment = PlayerFragment.newInstance(meditation)
//        val mFt: FragmentTransaction = supportFragmentManager.beginTransaction()
//        mFt.replace(R.id.rl_bottomsheet_fragment_container, mPlayerFragment)
//        mFt.commit()
//    }

}
