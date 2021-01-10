package br.com.isgreen.archandroid.extension

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog

/**
 * Created by Éverdes Soares on 01/09/2021.
 */

fun isAtLeastLollipop(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
}

fun isAtLeastMarshmallow(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}

fun isAtLeastNougat(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
}

fun isAtLeastOreo(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}

fun isAtLeastPie(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
}

fun isAtLeastQ(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}

fun isAtLeastR(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
}

//region Context
fun Context.toastShort(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.toastShort(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun Context.toastLong(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun Context.toastLong(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_LONG).show()

fun Context.showSimpleAlert(message: String) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, null)
        .create()
        .show()
}

fun Context?.showDialogList(
    @StringRes titleRes: Int,
    items: List<String?>,
    listener: DialogInterface.OnClickListener
) {
    this?.let {
        val adapter = ArrayAdapter(
            it,
            android.R.layout.simple_list_item_1,
            items.toTypedArray()
        )

        AlertDialog.Builder(it)
            .setTitle(titleRes)
            .setAdapter(adapter, listener)
            .create()
            .show()
    }
}
//endregion Context