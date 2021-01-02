package br.com.isgreen.archandroid.screen.pullrequest.decline

import br.com.isgreen.archandroid.data.model.comment.Content
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequestMessage
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
        pullRequestId: Int,
        message: String?
    ): PullRequest {
        val declinedPullRequest = apiHelper.doPullRequestDecline(workspace, repoSlug, pullRequestId)

        if (!message.isNullOrEmpty()) {
            val content = Content(message)
            val pullRequestMessage = PullRequestMessage(content)
            apiHelper.sendPullRequestComment(workspace, repoSlug, pullRequestId, pullRequestMessage)
        }

        return declinedPullRequest
    }
    //endregion Api

}