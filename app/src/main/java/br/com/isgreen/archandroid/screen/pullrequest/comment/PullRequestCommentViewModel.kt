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
    private var mHasMorePages = true
    private var mPage: String? = null

    override fun fetchPullRequestComments(
        isRefresh: Boolean,
        pullRequestId: Int?,
        repoFullName: String?
    ) {
        if (isRefresh) {
            mPage = null
            mHasMorePages = true
            mCommentsCleared.postValue(Unit)
        }

        if (mHasMorePages && !mIsLoading) {
            viewModelScope.launch {
                delay(1000)
                try {
                    changeLoading(true)
                    val pullRequestResponse = repository.fetchComments(
                        mPage,
                        pullRequestId = pullRequestId ?: 0,
                        repoFullName = repoFullName ?: ""
                    )

                    if (pullRequestResponse.comments.isNullOrEmpty()) {
                        mCommentsNotFound.postValue(Unit)
                    } else {
                        mCommentsFetched.postValue(pullRequestResponse.comments)
                    }

                    changeLoading(false)
                    getNextPage(pullRequestResponse.next)
                } catch (exception: Exception) {
                    changeLoading(false)
                    handleException(exception)
                }
            }
        }
    }

    private fun changeLoading(isLoading: Boolean) {
        mIsLoading = isLoading
        if (mPage.isNullOrEmpty()) {
            mLoadingChanged.postValue(isLoading)
        } else {
            mLoadingMoreChanged.postValue(isLoading)
        }
    }

    private fun getNextPage(nextUrl: String?) {
        if (nextUrl != null) {
            mPage = nextUrl.substring(nextUrl.indexOf("page=") + 5)
        } else {
            mPage = null
            mHasMorePages = false
        }
    }
}