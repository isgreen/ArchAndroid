package br.com.isgreen.archandroid.screen.pullrequest.comment.adder

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.comment.Comment
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest

/**
 * Created by Éverdes Soares on 01/06/2021.
 */

interface PullRequestCommentAdderContract {

    interface ViewModel : BaseContract.ViewModel {
        val pullRequestCommentSent: LiveData<Comment>

        fun sendPullRequestComment(
            message: String?,
            pullRequestId: Int?,
            repoFullName: String?,
        )
    }

    interface Repository {
        suspend fun sendPullRequestComment(
            message: String,
            workspace: String,
            repoSlug: String,
            pullRequestId: Int
        ): Comment
    }
}