package br.com.isgreen.archandroid.screen.pullrequest.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.comment.Comment
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Éverdes Soares on 08/17/2020.
 */

class PullRequestCommentViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: PullRequestCommentContract.Repository
) : BaseViewModel(exceptionHandlerHelper), PullRequestCommentContract.ViewModel {

    override val commentsCleared: LiveData<Unit>
        get() = mCommentsCleared
    override val commentsNotFound: LiveData<Unit>
        get() = mCommentsNotFound
    override val loadingMoreChanged: LiveData<Boolean>
        get() = mLoadingMoreChanged
    override val commentsFetched: LiveData<List<Comment>>
        get() = mCommentsFetched

    private val mCommentsCleared = MutableLiveData<Unit>()
    private val mCommentsNotFound = MutableLiveData<Unit>()
    private val mLoadingMoreChanged = MutableLiveData<Boolean>()
    private val mCommentsFetched = MutableLiveData<List<Comment>>()

    private var mIsLoading = false
    private var mNextRequestUrl: String? = null

    override fun fetchPullRequestComments(
        isRefresh: Boolean,
        pullRequestId: Int?,
        repoFullName: String?
    ) {
        if (isRefresh) {
            mNextRequestUrl = null
            mCommentsCleared.postValue(Unit)
        }

        if ((isRefresh || !isRefresh && mNextRequestUrl != null) && !mIsLoading) {
            viewModelScope.launch {
                if (repoFullName != null) {
                    if (isRefresh) {
                        delay(1000)
                    }

                    try {
                        changeLoading(true)

                        val names = repoFullName.split("/")
                        val pullRequestResponse = repository.fetchComments(
                            workspace = names[0],
                            repoSlug = names[1],
                            nextUrl = mNextRequestUrl,
                            pullRequestId = pullRequestId ?: 0
                        )

                        if (pullRequestResponse.comments.isNullOrEmpty()) {
                            mCommentsNotFound.postValue(Unit)
                        } else {
                            mCommentsFetched.postValue(pullRequestResponse.comments)
                        }

                        changeLoading(false)
                        mNextRequestUrl = pullRequestResponse.next
                    } catch (exception: Exception) {
                        changeLoading(false)
                        handleException(exception)
                    }
                }
            }
        }
    }

    private fun changeLoading(isLoading: Boolean) {
        mIsLoading = isLoading
        if (mNextRequestUrl.isNullOrEmpty()) {
            mLoadingChanged.postValue(isLoading)
        } else {
            mLoadingMoreChanged.postValue(isLoading)
        }
    }
}