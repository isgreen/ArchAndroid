package br.com.isgreen.archandroid.extension

/**
 * Created by Éverdes Soares on 08/24/2019.
 */

fun Int?.isNullOrEmpty() = this == null || this == 0

fun Double?.isNullOrEmpty() = this == null || this == 0.0
