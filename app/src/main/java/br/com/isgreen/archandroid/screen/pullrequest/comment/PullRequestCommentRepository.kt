package br.com.isgreen.archandroid.screen.pullrequest.comment

import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 08/17/2020.
 */

class PullRequestCommentRepository(
    private val apiHelper: ApiHelper
): PullRequestCommentContract.Repository {

    //region Api
    override suspend fun fetchComments(page: String?, pullRequestId: Int, repoFullName: String) =
        apiHelper.fetchPullRequestComments(page, pullRequestId, repoFullName)
    //endregion Api

}