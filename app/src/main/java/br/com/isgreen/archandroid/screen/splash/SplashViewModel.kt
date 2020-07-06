package br.com.isgreen.archandroid.screen.splash

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import kotlinx.coroutines.delay

/**
 * Created by Éverdes Soares on 09/19/2019.
 */

class SplashViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: SplashContract.Repository
) : BaseViewModel(exceptionHandlerHelper), SplashContract.ViewModel {

    override val themeFetched: LiveData<Int>
        get() = mThemeFetched
    override val isAuthenticated: LiveData<Unit>
        get() = mIsAuthenticated
    override val isNotAuthenticated: LiveData<Unit>
        get() = mIsNotAuthenticated

    private val mThemeFetched = MutableLiveData<Int>()
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

    override fun fetchCurrentTheme() {
        defaultLaunch {
            var theme = repository.fetchCurrentTheme()

            if (theme == 0) {
                theme = AppCompatDelegate.MODE_NIGHT_NO
            }

            mThemeFetched.postValue(theme)
        }
    }
}