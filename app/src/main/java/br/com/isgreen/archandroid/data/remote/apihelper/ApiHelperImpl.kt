package br.com.isgreen.archandroid.data.remote.apihelper

import android.util.Base64
import br.com.isgreen.archandroid.BuildConfig
import br.com.isgreen.archandroid.data.local.PreferencesHelper
import br.com.isgreen.archandroid.data.model.comment.Comment
import br.com.isgreen.archandroid.data.model.comment.FetchPullRequestCommentsResponse
import br.com.isgreen.archandroid.data.model.commit.FetchPullRequestCommitsResponse
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.merge.PullRequestMergeParameter
import br.com.isgreen.archandroid.data.model.pullrequest.FetchPullRequestsResponse
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequestMessage
import br.com.isgreen.archandroid.data.model.repository.FetchReposResponse
import br.com.isgreen.archandroid.data.remote.api.Api
import br.com.isgreen.archandroid.data.remote.api.ApiConstant
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
    override suspend fun fetchPullRequests(
        nextUrl: String?,
        userUuid: String,
        states: List<String>
    ): FetchPullRequestsResponse {
        checkTokenExpired()

        val fullUrl = if (nextUrl == null) {
            val url = ApiConstant.FETCH_PULL_REQUESTS.replace("{user_uuid}", userUuid)
            var query = ""

            states.forEach {
                query += "&state=$it"
            }

            query = query.replaceFirst("&", "")
            "$url?$query"
        } else {
            nextUrl
        }

        return api.fetchPullRequests(fullUrl)
    }

    override suspend fun fetchPullRequestCommits(
        nextUrl: String?,
        repoSlug: String,
        workspace: String,
        pullRequestId: Int,
    ): FetchPullRequestCommitsResponse {
        checkTokenExpired()

        val fullUrl = if (nextUrl == null) {
            val url = ApiConstant.FETCH_PULL_REQUEST_COMMITS
                .replace("{workspace}", workspace)
                .replace("{repo_slug}", repoSlug)
                .replace("{pull_request_id}", pullRequestId.toString())

            url
        } else {
            nextUrl
        }

        return api.fetchPullRequestCommits(fullUrl)
    }

    override suspend fun fetchPullRequestComments(
        nextUrl: String?,
        repoSlug: String,
        workspace: String,
        pullRequestId: Int
    ): FetchPullRequestCommentsResponse {
        checkTokenExpired()

        val fullUrl = if (nextUrl == null) {
            val url = ApiConstant.FETCH_PULL_REQUEST_COMMENTS
                .replace("{workspace}", workspace)
                .replace("{repo_slug}", repoSlug)
                .replace("{pull_request_id}", pullRequestId.toString())

            url
        } else {
            nextUrl
        }

        return api.fetchPullRequestComments(fullUrl)
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
    ): PullRequest {
        checkTokenExpired()
        return api.doPullRequestDecline(workspace, repoSlug, pullRequestId)
    }

    override suspend fun sendPullRequestComment(
        workspace: String,
        repoSlug: String,
        pullRequestId: Int,
        pullRequestMessage: PullRequestMessage
    ): Comment {
        checkTokenExpired()
        return api.sendPullRequestComment(pullRequestMessage, workspace, repoSlug, pullRequestId)
    }
    //endregion Pull Request
}