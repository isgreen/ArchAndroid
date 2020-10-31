package br.com.isgreen.archandroid.widget

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import br.com.isgreen.archandroid.R

/**
 * Created by Éverdes Soares on 07/15/2020.
 */

class FlexibleToolbar : Toolbar {

    private var mHomeIcon: Int = -1
    private var mBackground: Int? = null
    private var mTitleText: String = ""
    private var mTitleIconRes: Int? = null
    private var mDisplayHome: Boolean = true
    private var mTitleRes: Int = R.string.clear
    private var mTitleTextColor: Int? = null
    private var mHomeIconColor: Int? = Color.BLACK
    private var mTitleIconColor: Int? = Color.BLACK

    constructor (context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor (context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        if (!isInEditMode) {
            attrs?.let {
                val tpArray =
                    context?.obtainStyledAttributes(attrs, R.styleable.FlexibleToolbar)
                mTitleIconColor = tpArray?.getColor(
                    R.styleable.FlexibleToolbar_iconColor,
                    Color.BLACK
                )
                mHomeIconColor = tpArray?.getColor(
                    R.styleable.FlexibleToolbar_homeIconColor,
                    Color.BLACK
                )

                tpArray?.recycle()
            }

            val layout = LayoutInflater.from(context)
                .inflate(R.layout.toolbar_title, this, false) as ConstraintLayout

            val layoutParams = layout.layoutParams as? LayoutParams
            layoutParams?.gravity = Gravity.CENTER
            addView(layout, layoutParams)
        }
    }

    private fun getDrawableTinted(icon: Int, color: Int?): Drawable? {
        val drawable = ContextCompat.getDrawable(context, icon)

        color?.let { iconColor ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable?.mutate()
                drawable?.setTint(iconColor)
            } else {
                drawable?.setColorFilter(PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP))
            }
        }

        return drawable
    }

    private fun getColor(@AttrRes attrRes: Int): Int {
        val outTypedValue = TypedValue()
        context?.theme?.resolveAttribute(attrRes, outTypedValue, true)
        return outTypedValue.data
    }

    fun changeNavigationIcon(@DrawableRes icon: Int) {
        this.navigationIcon = ContextCompat.getDrawable(context, icon)
    }

    fun changeNavigationIconColor(@ColorRes colorRes: Int) {
        mHomeIconColor = ContextCompat.getColor(context, colorRes)
        val drawable = getDrawableTinted(mHomeIcon, mHomeIconColor)
        this.navigationIcon = drawable
    }

    fun changeToolbarIconColor(@ColorRes colorRes: Int) {
        mHomeIconColor = colorRes

        context?.let {
            mHomeIconColor?.let { color ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val drawable = ContextCompat.getDrawable(it, mHomeIcon)
                    drawable?.setTint(ContextCompat.getColor(it, color))
                    this.navigationIcon = drawable
                }
            }
        }
    }

    inner class Builder(private val activity: AppCompatActivity?) {

        fun title(title: Int): Builder {
            mTitleRes = title
            return this
        }

        fun title(title: String?): Builder {
            mTitleText = title ?: ""
            return this
        }

        fun titleIcon(@DrawableRes titleIconRes: Int): Builder {
            mTitleIconRes = titleIconRes
            return this
        }

        fun titleIconColor(@AttrRes attrRes: Int): Builder {
            mTitleIconColor = getColor(attrRes)
            return this
        }

        fun titleTextColor(@ColorRes textColorRes: Int): Builder {
            mTitleTextColor = textColorRes
            return this
        }

        fun homeIcon(@DrawableRes icon: Int): Builder {
            mHomeIcon = icon
            return this
        }

        fun homeIconColor(@ColorRes color: Int): Builder {
            mHomeIconColor = ContextCompat.getColor(context, color)
            return this
        }

        fun backgroundColor(@ColorRes colorRes: Int): Builder {
            mBackground = ContextCompat.getColor(context, colorRes)
            return this
        }

        fun backgroundAttrColor(@AttrRes attrRes: Int): Builder {
            val color = getColor(attrRes)
            mBackground = color
            return this
        }

        fun displayHome(displayHome: Boolean): Builder {
            mDisplayHome = displayHome
            return this
        }

        fun build() {
            activity?.let { activity ->
                activity.setSupportActionBar(this@FlexibleToolbar)

                activity.supportActionBar?.let { actionBar ->
                    if (mTitleRes != -1) {
                        actionBar.title = if (mTitleText != "")
                            mTitleText
                        else
                            activity.getString(mTitleRes)
                    }

                    actionBar.setDisplayShowHomeEnabled(mDisplayHome)
                    actionBar.setDisplayHomeAsUpEnabled(mDisplayHome)
//                actionBar.elevation = 0f

                    if (mHomeIcon != -1 && mDisplayHome) {
                        val drawable = getDrawableTinted(mHomeIcon, mHomeIconColor)
                        this@FlexibleToolbar.navigationIcon = drawable
                    }

                    mBackground?.let {
                        this@FlexibleToolbar.setBackgroundColor(it)
                    }

                    val centerTitle = this@FlexibleToolbar.findViewById<AppCompatTextView>(R.id.txtToolbarTitle)
                    centerTitle?.let { title ->
                        title.text = actionBar.title
                        actionBar.title = ""

                        mTitleTextColor?.let { titleTextColor ->
                            title.setTextColor(ContextCompat.getColor(activity, titleTextColor))
                        }
                    }

                    val centerTitleIcon = this@FlexibleToolbar.findViewById<AppCompatImageView>(R.id.imgTitleIcon)
                    centerTitleIcon?.let {
                        if (mTitleIconRes != null) {
                            val drawable = getDrawableTinted(mTitleIconRes!!, mTitleIconColor)
                            it.setImageDrawable(drawable)
                        } else {
                            centerTitleIcon.isVisible = false
                        }
                    }
                }
            }
        }
    }
}