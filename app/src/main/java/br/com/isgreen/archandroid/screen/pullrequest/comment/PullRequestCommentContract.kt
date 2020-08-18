package br.com.isgreen.archandroid.screen.pullrequest.comment

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.comment.Comment
import br.com.isgreen.archandroid.data.model.comment.FetchPullRequestCommentsResponse

/**
 * Created by Éverdes Soares on 08/17/2020.
 */

interface PullRequestCommentContract {

    interface ViewModel : BaseContract.ViewModel {
        val commentsCleared: LiveData<Unit>
        val loadingMoreChanged: LiveData<Boolean>
        val commentsFetched: LiveData<List<Comment>>

        fun fetchPullRequestComments(
            isRefresh: Boolean = false,
            pullRequestId: Int?,
            repoFullName: String?
        )
    }

    interface Repository {
        suspend fun fetchComments(
            page: String?,
            pullRequestId: Int,
            repoFullName: String
        ): FetchPullRequestCommentsResponse
    }
}