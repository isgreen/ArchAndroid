package br.com.isgreen.archandroid.util

import android.content.Intent
import android.view.View

/**
 * Created by Éverdes Soares on 03/31/2020.
 */

typealias OnAnimationFinishCallback = (() -> Unit)
typealias OnItemClickListener<T> = ((View, Int, T) -> Unit)
typealias OnEventReceivedListener = ((code: Int, data: Any?) -> Unit)
typealias OnActivityResultCallback = ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)
