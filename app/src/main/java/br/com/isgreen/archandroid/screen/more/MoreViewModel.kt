package br.com.isgreen.archandroid.screen.more

import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseViewModel

/**
 * Created by Éverdes Soares on 04/12/2020.
 */

class MoreViewModel(
    private val repository: MoreContract.Repository
) : BaseViewModel(), MoreContract.ViewModel {

    override fun logout() {
        defaultLaunch {
            repository.logout()
            mRedirect.postValue(R.id.loginFragment)
        }
    }
}