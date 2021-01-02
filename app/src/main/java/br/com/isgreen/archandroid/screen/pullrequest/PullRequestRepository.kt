package br.com.isgreen.archandroid.screen.pullrequest

import br.com.isgreen.archandroid.data.model.pullrequest.FetchPullRequestsResponse
import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 08/02/2020.
 */

class PullRequestRepository(
    private val apiHelper: ApiHelper
) : PullRequestContract.Repository {

    //region Api
    override suspend fun fetchPullRequests(nextUrl: String?, states: List<String>): FetchPullRequestsResponse {
        // TODO: 08/08/20 save and fetch user (or only uuid) from preferences
        val user = apiHelper.fetchUser()
        return apiHelper.fetchPullRequests(nextUrl, user.uuid, states)
    }

    override suspend fun doPullRequestApprove(
        workspace: String,
        repoSlug: String,
        pullRequestId: Int
    ) = apiHelper.doPullRequestApprove(workspace, repoSlug, pullRequestId)
    //endregion Api

}