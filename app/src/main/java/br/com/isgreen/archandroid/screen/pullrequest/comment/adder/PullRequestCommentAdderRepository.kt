package br.com.isgreen.archandroid.screen.pullrequest.comment.adder

import br.com.isgreen.archandroid.data.model.comment.Comment
import br.com.isgreen.archandroid.data.model.comment.Content
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequestMessage
import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 01/06/2021.
 */

class PullRequestCommentAdderRepository(
    private val apiHelper: ApiHelper
) : PullRequestCommentAdderContract.Repository {

    //region Api
    override suspend fun sendPullRequestComment(
        message: String,
        workspace: String,
        repoSlug: String,
        pullRequestId: Int
    ): Comment {
        val content = Content(message)
        val pullRequestMessage = PullRequestMessage(content)
        return apiHelper.sendPullRequestComment(workspace, repoSlug, pullRequestId, pullRequestMessage)
    }
    //endregion Api

}