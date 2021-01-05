package br.com.isgreen.archandroid.screen.pullrequest.comment

import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 08/17/2020.
 */

class PullRequestCommentRepository(
    private val apiHelper: ApiHelper
): PullRequestCommentContract.Repository {

    //region Api
    override suspend fun fetchComments(
        nextUrl: String?,
        repoSlug: String,
        workspace: String,
        pullRequestId: Int
    ) = apiHelper.fetchPullRequestComments(nextUrl, repoSlug, workspace, pullRequestId)
    //endregion Api

}