package br.com.isgreen.archandroid.screen.pullrequest.commit

import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 08/12/2020.
 */

class PullRequestCommitRepository(
    private val apiHelper: ApiHelper
): PullRequestCommitContract.Repository {

    //region Api
    override suspend fun fetchCommits(
        nextUrl: String?,
        repoSlug: String,
        workspace: String,
        pullRequestId: Int,
    ) = apiHelper.fetchPullRequestCommits(nextUrl, repoSlug, workspace, pullRequestId)
    //endregion Api

}