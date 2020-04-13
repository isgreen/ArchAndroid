package br.com.isgreen.archandroid.validator

import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseValidator
import br.com.isgreen.archandroid.base.BaseValidator.Companion.NO_ERROR

/**
 * Created by Éverdes Soares on 08/24/2019
 */

class LoginValidator : BaseValidator {

    override fun validate(vararg anys: Any?): Int {
        val username = anys[0] as? String
        val password = anys[1] as? String

        return when {
            username.isNullOrEmpty() ->
                R.string.type_your_username
            password.isNullOrEmpty() ->
                R.string.type_your_password
            else -> NO_ERROR
        }
    }

}