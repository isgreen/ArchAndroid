package br.com.isgreen.archandroid.data.remote.apihelper

import android.util.Base64
import br.com.isgreen.archandroid.BuildConfig
import br.com.isgreen.archandroid.data.local.PreferencesHelper
import br.com.isgreen.archandroid.data.model.comment.FetchPullRequestCommentsResponse
import br.com.isgreen.archandroid.data.model.commit.FetchPullRequestCommitsResponse
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.merge.PullRequestMergeParameter
import br.com.isgreen.archandroid.data.model.pullrequest.FetchPullRequestsResponse
import br.com.isgreen.archandroid.data.model.repository.FetchReposResponse
import br.com.isgreen.archandroid.data.remote.api.Api
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
    override suspend fun fetchRepos(url: String): FetchReposResponse {
        checkTokenExpired()
        return api.fetchRepos(url)
    }
    //endregion Repositories

    //region Pull Request
    override suspend fun fetchPullRequests(url: String): FetchPullRequestsResponse {
        checkTokenExpired()
        return api.fetchPullRequests(url)
    }

    override suspend fun fetchPullRequestCommits(
        page: String?,
        pullRequestId: Int,
        repoFullName: String
    ): FetchPullRequestCommitsResponse {
        checkTokenExpired()
        return api.fetchPullRequestCommits(pullRequestId, repoFullName, page)
    }

    override suspend fun fetchPullRequestComments(
        page: String?,
        pullRequestId: Int,
        repoFullName: String
    ): FetchPullRequestCommentsResponse {
        checkTokenExpired()
        return api.fetchPullRequestComments(pullRequestId, repoFullName, page)
    }

    override suspend fun doPullRequestMerge(
        pullRequestId: Int,
        repoFullName: String,
        pullRequestMergeParameter: PullRequestMergeParameter
    ) {
        checkTokenExpired()
        api.doPullRequestsMerge(pullRequestId, repoFullName, pullRequestMergeParameter)
    }

    override suspend fun doPullRequestApprove(
        workspace: String,
        repoSlug: String,
        pullRequestId: Int
    ) {
        checkTokenExpired()
        api.doPullRequestApprove(workspace, repoSlug, pullRequestId)
    }

    override suspend fun doPullRequestDecline(
        workspace: String,
        repoSlug: String,
        pullRequestId: Int
    ) {
        checkTokenExpired()
        api.doPullRequestDecline(workspace, repoSlug, pullRequestId)
    }
    //endregion Pull Request
}