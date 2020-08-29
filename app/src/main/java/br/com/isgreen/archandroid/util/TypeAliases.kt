package br.com.isgreen.archandroid.util

import android.content.Intent
import android.view.View

/**
 * Created by Éverdes Soares on 03/31/2020.
 */

typealias OnAnimationFinishCallback = (() -> Unit)
typealias OnEventReceivedListener = ((code: Int, data: Any?) -> Unit)
typealias OnItemClickListener<T> = ((view: View, position: Int, data: T) -> Unit)
typealias OnInnerViewItemClickListener = ((view: View?, position: Int, data: Any?) -> Unit)
typealias OnActivityResultCallback = ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)
