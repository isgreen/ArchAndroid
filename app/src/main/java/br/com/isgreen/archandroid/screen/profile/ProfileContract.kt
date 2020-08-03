package br.com.isgreen.archandroid.screen.profile

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.repository.FetchReposResponse

/**
 * Created by Éverdes Soares on 07/22/2020.
 */

interface ProfileContract {

    interface ViewModel : BaseContract.ViewModel {
        val userFetched: LiveData<User>

        fun logout()
        fun fetchUser()
    }

    interface Repository {
        suspend fun logout()
        suspend fun fetchUser(): User
        @Deprecated("Move to RepoFragment")
        suspend fun fetchUserRepos(userUuid: String): FetchReposResponse
    }
}