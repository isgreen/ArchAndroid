package br.com.isgreen.archandroid.screen.pullrequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import kotlinx.coroutines.launch

/**
 * Created by Éverdes Soares on 08/03/2020.
 */

class PullRequestViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: PullRequestContract.Repository
) : BaseViewModel(exceptionHandlerHelper), PullRequestContract.ViewModel {

    override val pullRequestsCleared: LiveData<Unit>
        get() = mPullRequestsCleared
    override val pullRequestsNotFound: LiveData<Unit>
        get() = mPullRequestsNotFound
    override val loadingMoreChanged: LiveData<Boolean>
        get() = mLoadingMoreChanged
    override val pullRequestsFetched: LiveData<List<PullRequest>>
        get() = mPullRequestsFetched

    private val mPullRequestsCleared = MutableLiveData<Unit>()
    private val mPullRequestsNotFound = MutableLiveData<Unit>()
    private val mLoadingMoreChanged = MutableLiveData<Boolean>()
    private val mPullRequestsFetched = MutableLiveData<List<PullRequest>>()

    private var mIsLoading = false
    private var mNextRequestUrl: String? = null

    override fun fetchPullRequests(isInitialRequest: Boolean) {
        if (isInitialRequest) {
            mNextRequestUrl = null
            mPullRequestsCleared.postValue(Unit)
        }

        if ((isInitialRequest || !isInitialRequest && mNextRequestUrl != null) && !mIsLoading) {
            viewModelScope.launch {
                try {
                    changeLoading(true)
                    val states = listOf("OPEN", "DECLINED")
                    val pullRequestResponse = repository.fetchPullRequests(mNextRequestUrl, states)
                    val pullRequests = pullRequestResponse.pullRequests

                    if (pullRequests.isNullOrEmpty()) {
                        mPullRequestsNotFound.postValue(Unit)
                    } else {
                        mPullRequestsFetched.postValue(pullRequestResponse.pullRequests)
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

    private fun changeLoading(isLoading: Boolean) {
        mIsLoading = isLoading
        if (mNextRequestUrl.isNullOrEmpty()) {
            mLoadingChanged.postValue(isLoading)
        } else {
            mLoadingMoreChanged.postValue(isLoading)
        }
    }
}