package br.com.isgreen.archandroid.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

/**
 * Created by Éverdes Soares on 07/15/2020
 */

class AnimationUtil {

    companion object {

        private const val DURATION_DEFAULT = 300L

        fun showView(
            view: View?,
            @AnimRes animRes: Int,
            duration: Long? = DURATION_DEFAULT,
            delayMillis: Long? = 0L,
            onAnimationEndCallback: OnAnimationFinishCallback? = null
        ): Animation? {
            return animateView(
                view,
                false,
                duration,
                delayMillis,
                animRes,
                onAnimationEndCallback
            )
        }

        fun hideView(
            view: View?,
            @AnimRes animRes: Int,
            duration: Long? = DURATION_DEFAULT,
            delayMillis: Long? = 0L,
            onAnimationEndCallback: OnAnimationFinishCallback? = null
        ): Animation? {
            return animateView(
                view,
                true,
                duration,
                delayMillis,
                animRes,
                onAnimationEndCallback
            )
        }

        private fun animateView(
            view: View?,
            isVisible: Boolean,
            duration: Long? = DURATION_DEFAULT,
            delayMillis: Long? = 0L,
            @AnimRes animRes: Int,
            onAnimationEndCallback: OnAnimationFinishCallback? = null
        ): Animation? {
            if (view == null || view.context == null || duration == null || delayMillis == null) {
                view?.post { switchVisibility(view, isVisible) }
                return null
            }

            val anim = AnimationUtils.loadAnimation(
                view.context,
                animRes
            )
            view.postDelayed({
                view.clearAnimation()
                view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                anim.duration = duration
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {

                    }

                    override fun onAnimationEnd(animation: Animation) {
                        switchVisibility(view, isVisible)
                        onAnimationEndCallback?.invoke()
                    }

                    override fun onAnimationRepeat(animation: Animation) {

                    }
                })
                view.startAnimation(anim)
            }, delayMillis)
            return anim
        }

        private fun switchVisibility(view: View?, isVisible: Boolean) {
            view?.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE
        }
    }
}