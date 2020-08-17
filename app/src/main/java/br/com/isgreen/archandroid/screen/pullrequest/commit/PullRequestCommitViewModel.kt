package br.com.isgreen.archandroid.screen.pullrequest.commit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.commit.Commit
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import br.com.isgreen.archandroid.util.DateUtil
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Éverdes Soares on 08/12/2020.
 */

class PullRequestCommitViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: PullRequestCommitContract.Repository
) : BaseViewModel(exceptionHandlerHelper), PullRequestCommitContract.ViewModel {

    override val commitsCleared: LiveData<Unit>
        get() = mCommitsCleared
    override val commitsFetched: LiveData<List<Commit>>
        get() = mCommitsFetched
    override val loadingMoreChanged: LiveData<Boolean>
        get() = mLoadingMoreChanged

    private val mCommitsCleared = MutableLiveData<Unit>()
    private val mLoadingMoreChanged = MutableLiveData<Boolean>()
    private val mCommitsFetched = MutableLiveData<List<Commit>>()

    private var mIsLoading = false
    private var mHasMorePages = true
    private var mPage: String? = null

    override fun fetchPullRequestCommits(
        isRefresh: Boolean,
        pullRequestId: Int?,
        repoFullName: String?
    ) {
        if (isRefresh) {
            mPage = null
            mHasMorePages = true
            mCommitsCleared.postValue(Unit)
        }

        if (mHasMorePages && !mIsLoading) {
            viewModelScope.launch {
                try {
                    changeLoading(true)
                    val pullRequestResponse = repository.fetchCommits(
                        mPage,
                        pullRequestId = pullRequestId ?: 0,
                        repoFullName = repoFullName ?: ""
                    )
                    mCommitsFetched.postValue(pullRequestResponse.commits)
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