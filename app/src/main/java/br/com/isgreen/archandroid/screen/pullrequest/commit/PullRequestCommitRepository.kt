package br.com.isgreen.archandroid.screen.pullrequest.commit

import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 08/12/2020.
 */

class PullRequestCommitRepository(
    private val apiHelper: ApiHelper
): PullRequestCommitContract.Repository {

    //region Api
    override suspend fun fetchCommits(pullRequestId: Int, repoFullName: String) =
        apiHelper.fetchPullRequestCommits(pullRequestId, repoFullName)
    //endregion Api

}