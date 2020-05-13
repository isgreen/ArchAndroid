package br.com.isgreen.archandroid.screen.splash

import br.com.isgreen.archandroid.data.local.PreferencesHelper

/**
 * Created by Éverdes Soares on 09/19/2019.
 */

class SplashRepository(
    private val preferencesHelper: PreferencesHelper
) : SplashContract.Repository {

    //region Preferences
    override suspend fun fetchCurrentTheme() = preferencesHelper.getThemeMode()
    override suspend fun fetchAuthorization() = preferencesHelper.getAuthorization()
//    override suspend fun clearAuthentication() = preferencesHelper.clearAuthentication()
    //endregion Preferences

}