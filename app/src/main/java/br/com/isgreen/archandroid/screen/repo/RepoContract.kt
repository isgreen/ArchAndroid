package br.com.isgreen.archandroid.screen.repo

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.repository.FetchRepositoriesResponse
import br.com.isgreen.archandroid.data.model.repository.Repo

/**
 * Created by Éverdes Soares on 09/22/2019.
 */

interface RepoContract {

    interface ViewModel : BaseContract.ViewModel {
        val reposFetched: LiveData<List<Repo>>
        val loadingMoreChanged: LiveData<Boolean>

        fun fetchRepos()
    }

    interface Repository {
        suspend fun fetchRepos(sort: String?, role: String?, after: String?): FetchRepositoriesResponse
    }

}