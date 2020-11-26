package br.com.isgreen.archandroid.screen.pullrequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import br.com.isgreen.archandroid.util.DateUtil
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Éverdes Soares on 08/03/2020.
 */

class PullRequestViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: PullRequestContract.Repository
) : BaseViewModel(exceptionHandlerHelper), PullRequestContract.ViewModel {

    companion object {
        const val ROLE_MEMBER = "member"
    }

    override val pullRequestsCleared: LiveData<Unit>
        get() = mPullRequestsCleared
    override val pullRequestApproved: LiveData<Unit>
        get() = mPullRequestApproved
    override val pullRequestDeclined: LiveData<Unit>
        get() = mPullRequestDeclined
    override val pullRequestsNotFound: LiveData<Unit>
        get() = mPullRequestsNotFound
    override val loadingMoreChanged: LiveData<Boolean>
        get() = mLoadingMoreChanged
    override val pullRequestsFetched: LiveData<List<PullRequest>>
        get() = mPullRequestsFetched

    private val mPullRequestsCleared = MutableLiveData<Unit>()
    private val mPullRequestApproved = MutableLiveData<Unit>()
    private val mPullRequestDeclined = MutableLiveData<Unit>()
    private val mPullRequestsNotFound = MutableLiveData<Unit>()
    private val mLoadingMoreChanged = MutableLiveData<Boolean>()
    private val mPullRequestsFetched = MutableLiveData<List<PullRequest>>()

    private var mIsLoading = false
    private var mHasMorePages = true
    private var mAfter: String? = null

    override fun fetchPullRequests(isRefresh: Boolean) {
        if (isRefresh) {
            mAfter = null
            mHasMorePages = true
            mPullRequestsCleared.postValue(Unit)
        }

        if (mHasMorePages && !mIsLoading) {
            viewModelScope.launch {
                try {
                    changeLoading(true)
                    val state = "OPEN,DECLINED"
                    val pullRequestResponse = repository.fetchPullRequests(state/*null, ROLE_MEMBER, mAfter*/)
                    val pullRequests = pullRequestResponse.pullRequests

                    if (pullRequests.isNullOrEmpty()) {
                        mPullRequestsNotFound.postValue(Unit)
                    } else {
                        mPullRequestsFetched.postValue(pullRequestResponse.pullRequests)
                    }

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

    override fun doPullRequestApprove(pullRequestId: Int?, repoFullName: String?) {
        defaultLaunch {
            if (pullRequestId != null && repoFullName != null) {
                val names = repoFullName.split("/")

                if (names.size == 2) {
                    repository.doPullRequestApprove(
                        workspace = names[0],
                        repoSlug = names[1],
                        pullRequestId = pullRequestId
                    )
                    mPullRequestApproved.postValue(Unit)
                }
            }
        }
    }

    override fun doPullRequestDecline(pullRequestId: Int?, repoFullName: String?) {
        defaultLaunch {
            if (pullRequestId != null && repoFullName != null) {
                val names = repoFullName.split("/")

                if (names.size == 2) {
                    repository.doPullRequestDecline(
                        workspace = names[0],
                        repoSlug = names[1],
                        pullRequestId = pullRequestId
                    )

                    // TODO: 20/11/20 After implement screen and endpoint to send message

                    mPullRequestDeclined.postValue(Unit)
                }
            }
        }
    }
}