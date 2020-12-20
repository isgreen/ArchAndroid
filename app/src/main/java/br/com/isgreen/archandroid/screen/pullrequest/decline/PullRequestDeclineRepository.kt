package br.com.isgreen.archandroid.screen.pullrequest.decline

import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 12/19/2020.
 */

class PullRequestDeclineRepository(
    private val apiHelper: ApiHelper
) : PullRequestDeclineContract.Repository {

    //region Api
    override suspend fun doPullRequestDecline(
        workspace: String,
        repoSlug: String,
        pullRequestId: Int
    ) = apiHelper.doPullRequestDecline(workspace, repoSlug, pullRequestId)
    //endregion Api

}