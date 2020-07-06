package br.com.isgreen.archandroid.data.model.error

/**
 * Created by Éverdes Soares on 04/12/2020.
 */

data class ErrorMessage(
    val message: String,
    val code: Int = -1
)