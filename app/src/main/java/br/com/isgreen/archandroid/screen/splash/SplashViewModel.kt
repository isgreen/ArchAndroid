package br.com.isgreen.archandroid.screen.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.isgreen.archandroid.base.BaseViewModel
import kotlinx.coroutines.delay

/**
 * Created by Éverdes Soares on 09/19/2019.
 */

class SplashViewModel(
    private val repository: SplashContract.Repository
) : BaseViewModel(), SplashContract.ViewModel {

    override val message: LiveData<Int>
        get() = super.mMessage
    override val loading: LiveData<Boolean>
        get() = super.mLoadingChanged
    override val isAuthenticated: LiveData<Unit>
        get() = mIsAuthenticated
    override val isNotAuthenticated: LiveData<Unit>
        get() = mIsNotAuthenticated

    private val mIsAuthenticated = MutableLiveData<Unit>()
    private val mIsNotAuthenticated = MutableLiveData<Unit>()

    override fun checkIsAuthenticated() {
        defaultLaunch {
            delay(1000)
            val authorization = repository.fetchAuthorization()

            if (authorization != null) {
                mIsAuthenticated.postValue(Unit)
            } else {
                mIsNotAuthenticated.postValue(Unit)
            }
        }
    }
}