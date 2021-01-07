package br.com.isgreen.archandroid.screen.pullrequest.comment.adder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.comment.Comment
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper

/**
 * Created by Éverdes Soares on 01/06/2021.
 */

class PullRequestCommentAdderViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: PullRequestCommentAdderContract.Repository
) : BaseViewModel(exceptionHandlerHelper), PullRequestCommentAdderContract.ViewModel {

    override val pullRequestCommentSent: LiveData<Comment>
        get() = mPullRequestCommentSent

    private val mPullRequestCommentSent = MutableLiveData<Comment>()

    override fun sendPullRequestComment(
        message: String?,
        pullRequestId: Int?,
        repoFullName: String?
    ) {
        defaultLaunch {
            if (message != null && pullRequestId != null && repoFullName != null) {
                val names = repoFullName.split("/")

                if (names.size == 2) {
                    val declinedPullRequest = repository.sendPullRequestComment(
                        message = message,
                        workspace = names[0],
                        repoSlug = names[1],
                        pullRequestId = pullRequestId
                    )

                    mPullRequestCommentSent.postValue(declinedPullRequest)
                }
            }
        }
    }
}