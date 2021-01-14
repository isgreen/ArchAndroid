package br.com.isgreen.archandroid.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import br.com.isgreen.archandroid.R

/**
 * Created by Éverdes Soares on 04/10/2020.
 */

typealias OnClickTryAgain = (() -> Unit)?

class PlaceholderView : LinearLayout {

    private var mOnClickTryAgain: OnClickTryAgain = null

    private var mTxtFailed: AppCompatTextView? = null
    private var mBtnTryAgain: AppCompatButton? = null
    private var mIconFailed: AppCompatImageView? = null

    constructor (context: Context) : super(context) {
        this.initView(context)
    }

    constructor (context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        this.initView(context)
    }

    private fun initView(context: Context) {
        visibility = View.GONE

        val inflater = LayoutInflater.from(context).inflate(
            R.layout.placeholder_view,
            this,
            false
        )

        inflater?.let {
            mTxtFailed = inflater.findViewById(R.id.txtFailed)
            mIconFailed = inflater.findViewById(R.id.imgFailed)
            mBtnTryAgain = inflater.findViewById(R.id.btnTryAgain)

            mBtnTryAgain?.setOnClickListener {
                if (this.visibility == View.VISIBLE) {
                    mOnClickTryAgain?.invoke()
                    this.hide()
                }
            }

            addView(inflater)
        }
    }

    fun icon(resId: Int): PlaceholderView {
        this.mIconFailed?.setImageResource(resId)
        return this
    }

    fun text(resId: Int): PlaceholderView {
        this.mTxtFailed?.text = context.getString(resId)
        return this
    }

    fun text(message: String): PlaceholderView {
        this.mTxtFailed?.text = message
        return this
    }

    fun text(message: Any): PlaceholderView {
        if (message is Int) {
            this.mTxtFailed?.text = context.getString(message)
        } else if (message is CharSequence) {
            this.mTxtFailed?.text = message
        }
        return this
    }

    fun showTryAgain(): PlaceholderView {
        this.visibleTryAgain()
        return this
    }

    fun hideTryAgain(): PlaceholderView {
        this.goneTryAgain()
        return this
    }

    fun hideIcon(): PlaceholderView {
        this.mIconFailed?.visibility = View.GONE
        return this
    }

    fun setTextColor(@ColorRes color: Int): PlaceholderView {
        this.mTxtFailed?.setTextColor(ContextCompat.getColor(context, color))
        return this
    }

    fun setIconColor(@ColorRes color: Int): PlaceholderView {
        this.mIconFailed?.setBackgroundColor(ContextCompat.getColor(context, color))
        return this
    }

    fun setStartTextGravity(): PlaceholderView {
        this.mTxtFailed?.gravity = Gravity.START
        return this
    }

    private fun visibleTryAgain() {
        this.mBtnTryAgain?.visibility = View.VISIBLE
    }

    private fun goneTryAgain() {
        this.mBtnTryAgain?.visibility = View.GONE
    }

    fun show() {
        clearAnimation()
        visibility = View.VISIBLE
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.translate_fade_in))
    }

    fun hide() {
        if (this.visibility == View.VISIBLE) {
            clearAnimation()
            this.visibility = View.GONE
        }
    }

    var onClickTryAgain: OnClickTryAgain = null
        set(value) {
            mOnClickTryAgain = value
            field = value
        }
}