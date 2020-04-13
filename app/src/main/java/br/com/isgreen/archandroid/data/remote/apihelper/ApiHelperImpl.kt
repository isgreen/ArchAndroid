package br.com.isgreen.archandroid.data.remote.apihelper

import br.com.isgreen.archandroid.data.remote.api.Api
import br.com.isgreen.archandroid.data.local.PreferencesHelper
import br.com.isgreen.archandroid.data.model.repository.FetchRepositoriesResponse
import java.util.*

/**
 * Created by Éverdes Soares on 08/05/2019.
 */

class ApiHelperImpl(
    private val api: Api,
    private val preferencesHelper: PreferencesHelper
) : ApiHelper {

    private suspend fun checkTokenExpired() {
        val authorization = preferencesHelper.getAuthorization()
        val lastAuthorizationTime = preferencesHelper.getLastAuthorizationTime()

        if (authorization?.expiresIn != null && lastAuthorizationTime != null) {
            val currentTime = Calendar.getInstance().timeInMillis
            val expiresInMillis = authorization.expiresIn * 1000

            if (currentTime - lastAuthorizationTime > expiresInMillis
                && authorization.refreshToken != null
            ) {
                authorization.accessToken = null
                preferencesHelper.saveAuthorization(authorization)

                val newAuthorization = api.refreshToken("refresh_token", authorization.refreshToken)

                preferencesHelper.saveAuthorization(newAuthorization)
                preferencesHelper.saveLastAuthorizationTime(currentTime)
            }
        }
    }

    //region Login
    override suspend fun doLogin(grantType: String, username: String, password: String) =
        api.doLogin(grantType, username, password)
    //endregion Login

    //region Repositories
    override suspend fun fetchRepos(sort: String?, role: String?, after: String?): FetchRepositoriesResponse {
        checkTokenExpired()
        return api.fetchRepos(sort, role, after)
    }
    //endregion Repositories

    override suspend fun fetchUser(username: String, password: String) =
        api.fetchUser(
//            "Basic " + Base64.encodeToString(
//                "$username:$password".toByteArray(), Base64.NO_WRAP
//            )
        )
}