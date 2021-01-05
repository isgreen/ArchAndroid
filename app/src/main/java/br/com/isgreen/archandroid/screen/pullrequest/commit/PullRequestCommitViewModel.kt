package br.com.isgreen.archandroid.screen.pullrequest.commit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.commit.Commit
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    private var mNextRequestUrl: String? = null

    override fun fetchPullRequestCommits(
        isRefresh: Boolean,
        pullRequestId: Int?,
        repoFullName: String?
    ) {
        if (isRefresh) {
            mNextRequestUrl = null
            mCommitsCleared.postValue(Unit)
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
                        if (names.size == 2) {
                            val pullRequestResponse = repository.fetchCommits(
                                workspace = names[0],
                                repoSlug = names[1],
                                nextUrl = mNextRequestUrl,
                                pullRequestId = pullRequestId ?: 0
                            )

                            mCommitsFetched.postValue(pullRequestResponse.commits)
                            changeLoading(false)
                            mNextRequestUrl = pullRequestResponse.next
                        }
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