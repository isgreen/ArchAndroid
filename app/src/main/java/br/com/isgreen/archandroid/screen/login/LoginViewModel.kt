package br.com.isgreen.archandroid.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import br.com.isgreen.archandroid.validator.LoginValidator

/**
 * Created by Éverdes Soares on 08/23/2019.
 */

class LoginViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: LoginContract.Repository
) : BaseViewModel(exceptionHandlerHelper), LoginContract.ViewModel {

    companion object {
        const val GRANT_TYPE_PASSWORD = "password"
    }

    override val loginAuthorized: LiveData<Unit>
        get() = mLoginAuthorized

    private val mLoginAuthorized = MutableLiveData<Unit>()

    override fun doLogin(username: String?, password: String?) {
        defaultLaunch(LoginValidator(), username, password) {
            val authorization = repository.doLogin(GRANT_TYPE_PASSWORD, username!!, password!!)
            repository.saveAuthorization(authorization)
            mLoginAuthorized.postValue(Unit)
        }
    }

}