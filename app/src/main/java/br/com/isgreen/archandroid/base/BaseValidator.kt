package br.com.isgreen.archandroid.base

/**
 * Created by Éverdes Soares on 08/26/2019.
 */

interface BaseValidator {

    companion object {
        const val NO_ERROR = -1
    }

    fun validate(vararg anys: Any?): Int
}