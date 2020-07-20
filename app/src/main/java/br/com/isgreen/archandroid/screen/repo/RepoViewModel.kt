package br.com.isgreen.archandroid.screen.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.repository.Repo
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import br.com.isgreen.archandroid.util.DateUtil
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Éverdes Soares on 09/22/2019.
 */

class RepoViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: RepoContract.Repository
) : BaseViewModel(exceptionHandlerHelper), RepoContract.ViewModel {

    companion object {
        const val ROLE_MEMBER = "member"
    }

    override val reposCleared: LiveData<Unit>
        get() = mReposCleared
    override val reposFetched: LiveData<List<Repo>>
        get() = mReposFetched
    override val loadingMoreChanged: LiveData<Boolean>
        get() = mLoadingMoreChanged

    private val mReposCleared = MutableLiveData<Unit>()
    private val mReposFetched = MutableLiveData<List<Repo>>()
    private val mLoadingMoreChanged = MutableLiveData<Boolean>()

    private var mIsLoading = false
    private var mHasMorePages = true
    private var mAfter: String? = null

    override fun fetchRepos(isRefresh: Boolean) {
        if (isRefresh) {
            mAfter = null
            mHasMorePages = true
            mReposCleared.postValue(Unit)
        }

        if (mHasMorePages && !mIsLoading) {
            viewModelScope.launch {
                try {
                    changeLoading(true)
                    val repoResponse = repository.fetchRepos(null, ROLE_MEMBER, mAfter)
                    mReposFetched.postValue(repoResponse.repos)
                    changeLoading(false)
                    getNextDate(repoResponse.next, repoResponse.repos.last().createdOn)
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