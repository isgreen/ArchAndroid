package br.com.isgreen.archandroid.screen.splash

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.login.Authorization

/**
 * Created by Éverdes Soares on 09/19/2019.
 */

interface SplashContract {

    interface ViewModel : BaseContract.ViewModel {
        val themeFetched: LiveData<Int>
        val isAuthenticated: LiveData<Unit>
        val isNotAuthenticated: LiveData<Unit>

        fun fetchCurrentTheme()
        fun checkIsAuthenticated()
    }

    interface Repository {
//        suspend fun clearAuthentication()
        suspend fun fetchCurrentTheme(): Int
        suspend fun fetchAuthorization(): Authorization?
    }
}