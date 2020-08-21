package br.com.isgreen.archandroid.extension

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import br.com.isgreen.archandroid.util.DateUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.text.NumberFormat

/**
 * Created Éverdes Soares on 08/07/2019.
 */

//region Context
fun Context.toastShort(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.toastShort(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun Context.toastLong(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun Context.toastLong(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
//endregion Context

//region View
fun View?.setColor(@ColorRes resIdFrom: Int, @ColorRes resIdTo: Int) {
    this?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val anim =
                ValueAnimator.ofArgb(
                    ContextCompat.getColor(context, resIdFrom),
                    ContextCompat.getColor(context, resIdTo)
                )

            anim.addUpdateListener { animation ->
                setBackgroundColor(animation.animatedValue as Int)
            }
            anim.start()
        } else {
            setBackgroundColor(ContextCompat.getColor(context, resIdTo))
        }
    }
}

fun View?.setColor(@ColorRes resId: Int) {
    this?.let {
        setBackgroundColor(ContextCompat.getColor(context, resId))
    }
}
//endregion View

//region TextView
fun AppCompatTextView?.setCurrency(value: Double?) {
    this?.text = value?.let { NumberFormat.getCurrencyInstance().format(value) } ?: run { "-" }
}

fun AppCompatTextView?.setDate(@StringRes formatRes: Int, date: String?) {
    this?.text = date?.let {
        val formattedDate = DateUtil.formatDate(
            it, DateUtil.DATE_TIME_FORMAT_API, DateUtil.DATE_FORMAT_LOCAL
        )

        this?.context?.resources?.getString(formatRes, formattedDate)
    } ?: run {
        ""
    }
}

fun AppCompatTextView?.setHtml(htmlText: String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this?.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        this?.text = Html.fromHtml(htmlText)
    }
}
//endregion TextView

//region ImageView
fun AppCompatImageView?.loadImageResource(@DrawableRes drawableRes: Int) {
    this?.let {
        Glide.with(this.context)
            .apply { RequestOptions.noAnimation() }
            .load(drawableRes)
            .into(this)
    }
}

fun AppCompatImageView?.loadImageRounded(
    urlImage: String?,
    @DrawableRes placeholderError: Int,
    @DimenRes placeholderErrorPadding: Int,
    onLoadingFinished: (success: Boolean) -> Unit = {}
) {
    this?.let {
        val url = urlImage ?: ""

        val requestListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                it.setPadding(placeholderErrorPadding)
                onLoadingFinished(false)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                it.setPadding(0)
                onLoadingFinished(true)
                return false
            }
        }

        Glide.with(it.context)
            .load(url)
            .apply(
                RequestOptions.circleCropTransform()
                    .centerCrop()
                    .optionalCircleCrop()
                    .error(placeholderError)
            )
            .listener(requestListener)
            .into(it)
    }
}
//endregion ImageView

//region RecyclerView
fun RecyclerView?.enableVerticalIndicators() {
    this?.let { recyclerView ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val indicators = View.SCROLL_INDICATOR_TOP or View.SCROLL_INDICATOR_BOTTOM
            recyclerView.setScrollIndicators(
                indicators,
                View.SCROLL_INDICATOR_TOP or View.SCROLL_INDICATOR_BOTTOM
            )
        }
    }
}
//endregion RecyclerView