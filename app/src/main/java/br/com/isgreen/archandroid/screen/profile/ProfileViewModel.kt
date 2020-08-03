package br.com.isgreen.archandroid.screen.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper

/**
 * Created by Éverdes Soares on 07/22/2020.
 */

class ProfileViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: ProfileContract.Repository
) : BaseViewModel(exceptionHandlerHelper), ProfileContract.ViewModel {

    override val userFetched: LiveData<User>
        get() = mUserFetched

    private val mUserFetched = MutableLiveData<User>()

    override fun fetchUser() {
        defaultLaunch {
            val user = repository.fetchUser()
            mUserFetched.postValue(user)
        }
    }

    override fun logout() {
        defaultLaunch {
            repository.logout()
            mRedirect.postValue(R.id.splashFragment)
        }
    }
}