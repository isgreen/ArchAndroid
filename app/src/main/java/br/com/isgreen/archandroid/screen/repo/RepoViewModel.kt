package br.com.isgreen.archandroid.screen.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.repository.Repo
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import br.com.isgreen.archandroid.screen.repo.datasource.RepoDataSourceFactory
import kotlinx.coroutines.launch

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

    private val repoDataSource = RepoDataSourceFactory(
        repository = repository,
        sort = null,
        role = ROLE_MEMBER,
        after = mAfter,
        scope = viewModelScope
    )

    private val pagedListConfig = PagedList.Config.Builder()
        .setInitialLoadSizeHint(5)
        .setEnablePlaceholders(false)
        .setPageSize(5 * 2)
        .build()

    val repos = LivePagedListBuilder(repoDataSource, pagedListConfig).build()

    override fun fetchRepos(isRefresh: Boolean) {
        if (isRefresh) {
            mAfter = null
            mHasMorePages = true
            mReposCleared.postValue(Unit)
        }

        if (mHasMorePages && !mIsLoading) {
            repoDataSource.reposDataSource.value?.invalidate()

//            viewModelScope.launch {
//                try {
//                    changeLoading(true)
//                    val repoResponse = repository.fetchRepos(null, ROLE_MEMBER, mAfter)
//                    mReposFetched.postValue(repoResponse.repos)
//                    changeLoading(false)
//                    getNextDate(repoResponse.next)
//                } catch (exception: Exception) {
//                    changeLoading(false)
//                    handleException(exception)
//                }
//            }
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

    private fun getNextDate(nextUrl: String?) {
        if (nextUrl != null) {
            mAfter = nextUrl.substring(nextUrl.indexOf("after=") + 6, nextUrl.indexOf("after=") + 36)
            mAfter = mAfter?.replace("%3A", ":")
        } else {
            mAfter = null
            mHasMorePages = false
        }
    }
}