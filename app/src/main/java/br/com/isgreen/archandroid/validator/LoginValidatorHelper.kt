package br.com.isgreen.archandroid.validator

import android.content.Context
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseValidatorHelper

/**
 * Created by Éverdes Soares on 08/24/2019
 */

class LoginValidatorHelper(private val context: Context) : BaseValidatorHelper {

    override fun validate(vararg anys: Any?): String? {
        val username = anys[0] as? String
        val password = anys[1] as? String

        return when {
            username.isNullOrEmpty() ->
                context.getString(R.string.type_your_username)
            password.isNullOrEmpty() ->
                context.getString(R.string.type_your_password)
            else -> null
        }
    }

}