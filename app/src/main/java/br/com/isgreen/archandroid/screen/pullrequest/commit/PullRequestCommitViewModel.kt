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

    companion object {
        const val ROLE_MEMBER = "member"
    }

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
    private var mAfter: String? = null

    override fun fetchPullRequestCommits(
        isRefresh: Boolean,
        pullRequestId: Int?,
        repoFullName: String?
    ) {
        if (isRefresh) {
            mAfter = null
            mHasMorePages = true
            mCommitsCleared.postValue(Unit)
        }

        if (mHasMorePages && !mIsLoading) {
            viewModelScope.launch {
                try {
                    changeLoading(true)
                    val pullRequestResponse = repository.fetchCommits(
                        pullRequestId = pullRequestId ?: 0,
                        repoFullName = repoFullName ?: ""
                        /*null, ROLE_MEMBER, mAfter*/
                    )
                    mCommitsFetched.postValue(pullRequestResponse.commits)
                    changeLoading(false)
//                    getNextDate(pullRequestResponse.next, pullRequestResponse.pullRequests.last().createdOn)
                } catch (exception: Exception) {
                    changeLoading(false)
                    handleException(exception)
                }
            }
        }
    }

    private fun changeLoading(isLoading: Boolean) {
        mIsLoading = isLoading
        if (mAfter.isNullOrEmpty()) {
            mLoadingChanged.postValue(isLoading)
        } else {
            mLoadingMoreChanged.postValue(isLoading)
        }
    }

    private fun getNextDate(nextUrl: String?, lastItemDate: String?) {
        if (nextUrl != null) {
            mAfter = DateUtil.increaseTime(
                dateAsString = lastItemDate,
                format = DateUtil.DATE_TIME_FORMAT_API,
                calendarTimeType = Calendar.SECOND,
                amountToIncrease = 1
            )
        } else {
            mAfter = null
            mHasMorePages = false
        }
    }
}