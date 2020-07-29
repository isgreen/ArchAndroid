package br.com.isgreen.archandroid.screen.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.repository.Repo
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper

/**
 * Created by Éverdes Soares on 07/22/2020.
 */

class UserViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: UserContract.Repository
) : BaseViewModel(exceptionHandlerHelper), UserContract.ViewModel {

    override val userFetched: LiveData<User>
        get() = mUserFetched
    override val reposFetched: LiveData<List<Repo>>
        get() = mReposFetched

    private val mUserFetched = MutableLiveData<User>()
    private val mReposFetched = MutableLiveData<List<Repo>>()

    override fun fetchUserAndRepos() {
        defaultLaunch {
            val user = repository.fetchUser()
            val reposResponse = repository.fetchUserRepos(user.username)
            mUserFetched.postValue(user)
            mReposFetched.postValue(reposResponse.repos)
        }
    }
}