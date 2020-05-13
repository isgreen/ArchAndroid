package br.com.isgreen.archandroid.helper.exception

import androidx.annotation.StringRes
import br.com.isgreen.archandroid.data.model.error.ErrorMessage

/**
 * Created by Éverdes Soares on 05/12/2020.
 */

interface ExceptionHandlerHelper {

    fun getErrorMessage(
        exception: Throwable,
        readApiMessage: Boolean? = true,
        @StringRes defaultMessageRes: Int? = null
    ): ErrorMessage

}