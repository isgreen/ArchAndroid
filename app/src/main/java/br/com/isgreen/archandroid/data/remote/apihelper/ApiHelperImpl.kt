package br.com.isgreen.archandroid.data.remote.apihelper

import android.util.Base64
import br.com.isgreen.archandroid.BuildConfig
import br.com.isgreen.archandroid.data.remote.api.Api
import br.com.isgreen.archandroid.data.local.PreferencesHelper
import br.com.isgreen.archandroid.data.model.commit.FetchPullRequestCommitsResponse
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.pullrequest.FetchPullRequestsResponse
import br.com.isgreen.archandroid.data.model.repository.FetchReposResponse
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

                val newAuthorization = api.refreshToken(
                    BuildConfig.API_SECRET,
                    getBasicAuthorizationBase64(),
                    "refresh_token",
                    authorization.refreshToken
                )

                preferencesHelper.saveAuthorization(newAuthorization)
                preferencesHelper.saveLastAuthorizationTime(currentTime)
            }
        }
    }

    private fun getBasicAuthorizationBase64(): String {
        return "Basic " + Base64.encodeToString(
            "${BuildConfig.API_KEY}:${BuildConfig.API_SECRET}".toByteArray(),
            Base64.NO_WRAP
        )
    }

    //region Login
    override suspend fun doLogin(grantType: String, username: String, password: String) =
        api.doLogin(
            BuildConfig.API_SECRET,
            getBasicAuthorizationBase64(),
            grantType,
            username,
            password
        )
    //endregion Login

    //region User
    override suspend fun fetchUser(): User {
        checkTokenExpired()
        return api.fetchUser()
    }

    override suspend fun fetchUserRepos(userUuid: String): FetchReposResponse {
        checkTokenExpired()
        return api.fetchUserRepos(userUuid)
    }
    //endregion User

    //region Repositories
    override suspend fun fetchRepos(sort: String?, role: String?, after: String?): FetchReposResponse {
        checkTokenExpired()
        return api.fetchRepos(sort, role, after)
    }
    //endregion Repositories

    //region Pull Request
    override suspend fun fetchPullRequests(userUuid: String): FetchPullRequestsResponse {
        checkTokenExpired()
        return api.fetchPullRequests(userUuid)
    }

    override suspend fun fetchPullRequestCommits(
        pullRequestId: Int,
        repoFullName: String
    ): FetchPullRequestCommitsResponse {
        checkTokenExpired()
        return api.fetchPullRequestCommits(pullRequestId, repoFullName)
    }
    //endregion Pull Request
}