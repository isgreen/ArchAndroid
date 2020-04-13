package br.com.isgreen.archandroid.screen.more

import br.com.isgreen.archandroid.data.local.PreferencesHelper

/**
 * Created by Éverdes Soares on 04/12/2020.
 */

class MoreRepository(
    private val preferencesHelper: PreferencesHelper
) : MoreContract.Repository {

    //region Preferences
    override suspend fun logout() = preferencesHelper.clearData()
    //endregion Preferences
}