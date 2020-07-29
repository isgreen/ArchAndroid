package br.com.isgreen.archandroid.screen.user

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.repository.FetchReposResponse
import br.com.isgreen.archandroid.data.model.repository.Repo

/**
 * Created by Éverdes Soares on 07/22/2020.
 */

interface UserContract {

    interface ViewModel : BaseContract.ViewModel {
        val userFetched: LiveData<User>
        val reposFetched: LiveData<List<Repo>>

        fun fetchUserAndRepos()
    }

    interface Repository {
        suspend fun fetchUser(): User
        suspend fun fetchUserRepos(username: String): FetchReposResponse
    }
}