package br.com.isgreen.archandroid.screen.pullrequest

import br.com.isgreen.archandroid.data.model.pullrequest.FetchPullRequestsResponse
import br.com.isgreen.archandroid.data.remote.api.ApiConstant
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

        val fullUrl = if (nextUrl == null) {
            val url = ApiConstant.FETCH_PULL_REQUESTS.replace("{user_uuid}", user.uuid ?: "")
            var query = ""

            states.forEach {
                query += "&state=$it"
            }

            query = query.replaceFirst("&", "")
            "$url?$query"
        } else {
            nextUrl
        }

        return apiHelper.fetchPullRequests(fullUrl)
    }

    override suspend fun doPullRequestApprove(
        workspace: String,
        repoSlug: String,
        pullRequestId: Int
    ) = apiHelper.doPullRequestApprove(workspace, repoSlug, pullRequestId)
    //endregion Api

}