package br.com.isgreen.archandroid.extension

import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.isgreen.archandroid.base.BaseActivity

/**
 * Created by Éverdes Soares on 03/31/2020.
 */

fun Fragment.isAtLeastLollipop(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
}

fun Fragment.isAtLeastMarshmallow(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}

fun Fragment.isAtLeastNougat(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
}

fun Fragment.isAtLeastQ(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}

val Fragment.baseActivity get() = this.activity as? BaseActivity

val Fragment.appCompatActivity get() = this.activity as? AppCompatActivity

fun Fragment.showMessage(message: Any) {
    when (message) {
        is Int -> {
            context?.toastLong(message)
        }
        is CharSequence -> {
            context?.toastLong(message)
        }
        else -> {
            throw RuntimeException("Message must be Int or CharSequence.")
        }
    }
}

fun Fragment.hideKeyboard() {
    val inputMethodManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
}