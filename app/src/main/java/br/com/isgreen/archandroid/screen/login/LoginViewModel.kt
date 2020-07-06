package br.com.isgreen.archandroid.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.isgreen.archandroid.base.BaseValidatorHelper
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper

/**
 * Created by Éverdes Soares on 08/23/2019.
 */

class LoginViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: LoginContract.Repository,
    private val validatorHelper: BaseValidatorHelper
) : BaseViewModel(exceptionHandlerHelper), LoginContract.ViewModel {

    companion object {
        const val GRANT_TYPE_PASSWORD = "password"
    }

    override val loginAuthorized: LiveData<Unit>
        get() = mLoginAuthorized

    private val mLoginAuthorized = MutableLiveData<Unit>()

    override fun doLogin(username: String?, password: String?) {
        defaultLaunch(validatorHelper, username, password) {
            val authorization = repository.doLogin(GRANT_TYPE_PASSWORD, username!!, password!!)
            repository.saveAuthorization(authorization)
            mLoginAuthorized.postValue(Unit)
        }
    }

}