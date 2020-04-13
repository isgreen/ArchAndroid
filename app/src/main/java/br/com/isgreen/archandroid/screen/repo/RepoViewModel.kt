package br.com.isgreen.archandroid.screen.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.repository.Repo

/**
 * Created by Éverdes Soares on 09/22/2019.
 */

class RepoViewModel(
    private val repository: RepoContract.Repository
) : BaseViewModel(), RepoContract.ViewModel {

    companion object {
        const val ROLE_MEMBER = "member"
    }

    override val reposFetched: LiveData<List<Repo>>
        get() = mReposFetched
    override val loadingMoreChanged: LiveData<Boolean>
        get() = mLoadingMoreChanged

    private val mReposFetched = MutableLiveData<List<Repo>>()
    private val mLoadingMoreChanged = MutableLiveData<Boolean>()

    private var mAfter: String? = null
    private var mHasMorePages = true

    override fun fetchRepos() {
        if (mHasMorePages) {
            defaultLaunch {
                changeLoading(true)
                val repoResponse = repository.fetchRepos(null, ROLE_MEMBER, mAfter)
                mReposFetched.postValue(repoResponse.repos)
                changeLoading(false)
                getNextDate(repoResponse.next)
            }
        }
    }

    private fun changeLoading(isLoading: Boolean) {
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