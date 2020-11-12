package br.com.isgreen.archandroid.data.remote.apihelper

import br.com.isgreen.archandroid.data.model.comment.FetchPullRequestCommentsResponse
import br.com.isgreen.archandroid.data.model.commit.FetchPullRequestCommitsResponse
import br.com.isgreen.archandroid.data.model.login.Authorization
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.merge.PullRequestMergeParameter
import br.com.isgreen.archandroid.data.model.pullrequest.FetchPullRequestsResponse
import br.com.isgreen.archandroid.data.model.repository.FetchReposResponse

/**
 * Created by Éverdes Soares on 08/05/2019.
 */

interface ApiHelper {

    //region Login
    suspend fun doLogin(grantType: String, username: String, password: String): Authorization
    //endregion Login

    //region User
    suspend fun fetchUser(): User
    suspend fun fetchUserRepos(userUuid: String): FetchReposResponse
    //endregion User

    //region Repositories
    suspend fun fetchRepos(sort: String?, role: String?, after: String?): FetchReposResponse
    //endregion Repositories

    //region Pull Request
    suspend fun fetchPullRequests(userUuid: String): FetchPullRequestsResponse

    suspend fun fetchPullRequestCommits(
        page: String?,
        pullRequestId: Int,
        repoFullName: String
    ): FetchPullRequestCommitsResponse

    suspend fun fetchPullRequestComments(
        page: String?,
        pullRequestId: Int,
        repoFullName: String
    ): FetchPullRequestCommentsResponse

    suspend fun doPullRequestsMerge(
        pullRequestId: Int,
        repoFullName: String,
        pullRequestMergeParameter: PullRequestMergeParameter
    )

    suspend fun doPullRequestsApprove(
        pullRequestId: Int,
        repoFullName: String
    )

    suspend fun doPullRequestsDecline(
        pullRequestId: Int,
        repoFullName: String
    )
    //endregion Pull Request

}