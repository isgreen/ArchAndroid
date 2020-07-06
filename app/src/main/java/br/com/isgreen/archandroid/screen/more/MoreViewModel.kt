package br.com.isgreen.archandroid.screen.more

import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper

/**
 * Created by Éverdes Soares on 04/12/2020.
 */

class MoreViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: MoreContract.Repository
) : BaseViewModel(exceptionHandlerHelper), MoreContract.ViewModel {

    override fun logout() {
        defaultLaunch {
            repository.logout()
            mRedirect.postValue(R.id.splashFragment)
        }
    }
}