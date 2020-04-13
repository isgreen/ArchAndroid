package br.com.isgreen.archandroid.screen.more

import br.com.isgreen.archandroid.base.BaseContract

/**
 * Created by Éverdes Soares on 04/12/2020.
 */

interface MoreContract {

    interface ViewModel : BaseContract.ViewModel {
        fun logout()
    }

    interface Repository {
        suspend fun logout()
    }
}