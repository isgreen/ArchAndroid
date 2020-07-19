package br.com.isgreen.archandroid.screen.repo

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.repository.FetchReposResponse
import br.com.isgreen.archandroid.data.model.repository.Repo

/**
 * Created by Éverdes Soares on 09/22/2019.
 */

interface RepoContract {

    interface ViewModel : BaseContract.ViewModel {
        val reposCleared: LiveData<Unit>
        val reposFetched: LiveData<List<Repo>>
        val loadingMoreChanged: LiveData<Boolean>

        fun fetchRepos(isRefresh: Boolean = false)
    }

    interface Repository {
        suspend fun fetchRepos(sort: String?, role: String?, after: String?): FetchReposResponse
    }

}