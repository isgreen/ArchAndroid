package br.com.isgreen.archandroid.widget

import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import br.com.isgreen.archandroid.R

/**
 * Created by Éverdes Soares on 09/22/2019
 */

class NavigationView : ConstraintLayout {

    private var mSelectedViewId: Int = 0

    private var mContainer: ViewGroup? = null
    private var mOnNavigationItemSelected: OnNavigationItemSelectedListener? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (!isInEditMode) {
            if (attrs != null) {
                val tpArray = context.obtainStyledAttributes(
                    attrs, R.styleable.NavigationView
                )
                val layout = tpArray.getResourceId(
                    R.styleable.NavigationView_layout, 0
                )

                tpArray.recycle()

                if (layout == 0) {
                    throw RuntimeException(
                        "No layout reference, please include attribute "
                                + "'app:layout' in XML, to use this component"
                    )
                }

                val navContainer = LayoutInflater.from(context).inflate(
                    layout, this,
                    true
                ) as ViewGroup

                mContainer = navContainer//.getChildAt(0) as ViewGroup
                (mContainer?.getChildAt(0) as? ViewGroup)?.getChildAt(0)?.id?.let { id ->
                    mSelectedViewId = id
                    changeViewTint(mContainer)
                }
                setChildrenListener(mContainer)
            }
        }
    }

    private fun setChildrenListener(container: ViewGroup?) {
        container?.let { cont ->
            if (cont.childCount > 0 && cont.getChildAt(0) is ViewGroup) {
                setChildrenListener(cont.getChildAt(0) as ViewGroup)
            } else {
                for (i in 0 until cont.childCount) {
                    val child = cont.getChildAt(i)
                    child?.setOnClickListener { this.onClick(it) }
                }
            }
        }
    }

    fun onClick(view: View) {
        mSelectedViewId = view.id

        mOnNavigationItemSelected?.onNavigationItemSelected(view)

        changeViewTint(mContainer)
    }

    private fun changeViewTint(container: ViewGroup?) {
        container?.let { cont ->
            if (cont.childCount > 0 && cont.getChildAt(0) is ViewGroup) {
                changeViewTint(cont.getChildAt(0) as ViewGroup)
            } else {
                for (i in 0 until cont.childCount) {
                    val child = cont.getChildAt(i)

                    child?.let {
                        if (child is AppCompatTextView) {

                            if (child.id == mSelectedViewId) {
                                child.isSelected = true
//                        child.setTextColor(ContextCompat.getColor(context, android.R.color.white))
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            child.compoundDrawableTintList = ColorStateList()
//                        }
                            } else {
                                child.isSelected = false
                            }
                        }
                    }

//            if (child is ImageView) {
//                //                boolean isChecked = child.getTag() != null && (boolean) child.getTag();
//                child.setColorFilter(
//                    ContextCompat.getColor(
//                        context,
//                        if (child.getId() == mSelectedViewId)
//                            R.color.accent else android.R.color.white
//                    ), PorterDuff.Mode.SRC_IN
//                )
//            }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeAnimatedBackground(view: View) {
        val anim = ValueAnimator.ofArgb(
            ContextCompat.getColor(context, R.color.colorAccent),
            ContextCompat.getColor(context, R.color.colorAccentDark)
        )
        anim.addUpdateListener { animation ->
            view.setBackgroundColor(animation.animatedValue as Int)
        }
        anim.start()
    }

//    fun animateItems() {
//        animateItems(R.anim.layout_anim_scale_open)
//    }

    fun animateItems(@AnimRes animRes: Int) {
        val controller = AnimationUtils.loadLayoutAnimation(context, animRes)

        mContainer?.layoutAnimation = controller
        mContainer?.scheduleLayoutAnimation()
        mContainer?.clearAnimation()
    }

    fun setOnNavigationItemSelected(onNavigationItemSelected: OnNavigationItemSelectedListener) {
        this.mOnNavigationItemSelected = onNavigationItemSelected
    }

    fun setSelectedItemId(itemId: Int) {
        mSelectedViewId = itemId
        changeViewTint(mContainer)

        if (mContainer != null) {
            mOnNavigationItemSelected.let {
                it?.onNavigationItemSelected(mContainer!!.findViewById(itemId))
            }
        }
    }

    val selectedViewId get() = mSelectedViewId

    fun setItemDrawable(itemId: Int, @DrawableRes drawableRes: Int) {
        val imageView = mContainer!!.findViewById<ImageView>(itemId)

        imageView?.setImageResource(drawableRes)
    }

    interface OnNavigationItemSelectedListener {

        fun onNavigationItemSelected(item: View)
    }
}