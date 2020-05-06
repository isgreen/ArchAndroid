package br.com.isgreen.archandroid.screen.theme

import br.com.isgreen.archandroid.data.local.PreferencesHelper
import br.com.isgreen.archandroid.helper.resource.ResourceHelper

/**
 * Created by Éverdes Soares on 01/14/2020.
 */

class ThemeRepository(
    private val resourceHelper: ResourceHelper,
    private val preferencesHelper: PreferencesHelper
) : ThemeContract.Repository {

    //region Resource
    override suspend fun fetchThemes() = resourceHelper.fetchThemes()
    //endregion Resource

    //region Preferences
    override suspend fun saveTheme(mode: Int) = preferencesHelper.saveThemeMode(mode)
    override suspend fun fetchCurrentThemeMode() = preferencesHelper.getThemeMode()
    //endregion Preferences

}