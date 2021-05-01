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

    override val themeFetched: LiveData<Int> = MutableLiveData()
    override val isAuthenticated: LiveData<Unit> = MutableLiveData()
    override val isNotAuthenticated: LiveData<Unit> = MutableLiveData()

    override fun checkIsAuthenticated() {
        defaultLaunch {
//            delay(1000)
            val authorization = repository.fetchAuthorization()

            if (authorization != null) {
                isAuthenticated.call()
            } else {
                isNotAuthenticated.call()
            }
        }
    }

    override fun fetchCurrentTheme() {
        defaultLaunch {
            var theme = repository.fetchCurrentTheme()

            if (theme == 0) {
                theme = AppCompatDelegate.MODE_NIGHT_NO
            }

            themeFetched.postValue(theme)
        }
    }
}