package br.com.isgreen.archandroid.screen.theme

import br.com.isgreen.archandroid.helper.resource.ResourceHelper

/**
 * Created by Éverdes Soares on 01/14/2020.
 */

class ThemeRepository(
    private val resourceHelper: ResourceHelper
) : ThemeContract.Repository {

    //region Resource
    override suspend fun fetchThemes() = resourceHelper.fetchThemes()
    //endregion Resource

}