package br.com.isgreen.archandroid.screen.login

import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper
import br.com.isgreen.archandroid.data.local.PreferencesHelper
import br.com.isgreen.archandroid.data.model.login.Authorization

/**
 * Created by Éverdes Soares on 08/23/2019.
 */

class LoginRepository(
    private val apiHelper: ApiHelper,
    private val preferencesHelper: PreferencesHelper
) : LoginContract.Repository {

    override suspend fun saveAuthorization(authorization: Authorization) {
        preferencesHelper.saveAuthorization(authorization)
    }

    override suspend fun doLogin(grantType: String, username: String, password: String) =
        apiHelper.doLogin(grantType, username, password)

}