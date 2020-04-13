package br.com.isgreen.archandroid.screen.login

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.login.Authorization

/**
 * Created by Éverdes Soares on 08/23/2019.
 */

interface LoginContract {

    interface View

    interface ViewModel : BaseContract.ViewModel {
        val loginAuthorized: LiveData<Unit>

        fun doLogin(username: String?, password: String?)
    }

    interface Repository {
        suspend fun saveAuthorization(authorization: Authorization)
        suspend fun doLogin(grantType: String, username: String, password: String): Authorization
    }

}