package br.com.isgreen.archandroid.screen.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.repository.Repo
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
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

    override val reposCleared: LiveData<Unit> = MutableLiveData()
    override val reposNotFound: LiveData<Unit> = MutableLiveData()
    override val reposFetched: LiveData<List<Repo>> = MutableLiveData()
    override val loadingMoreChanged: LiveData<Boolean> = MutableLiveData()

    private var mIsLoading = false
    private var mNextRequestUrl: String? = null

    override fun fetchRepos(isInitialRequest: Boolean) {
        if (isInitialRequest) {
            mNextRequestUrl = null
            reposCleared.postValue(Unit)
        }

        val isRequestingMoreData = !isInitialRequest && mNextRequestUrl != null

        if ((isInitialRequest || isRequestingMoreData) && !mIsLoading) {
            viewModelScope.launch {
                try {
                    changeLoading(true)
                    val repoResponse = repository.fetchRepos(mNextRequestUrl, null, ROLE_MEMBER)
                    val repos = repoResponse.repos

                    if (repos.isNullOrEmpty()) {
                        reposNotFound.postValue(Unit)
                    } else {
                        reposFetched.postValue(repoResponse.repos)
                    }

                    changeLoading(false)
                    mNextRequestUrl = repoResponse.next
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
            loadingMoreChanged.postValue(isLoading)
        }
    }
}