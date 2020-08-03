package br.com.isgreen.archandroid.screen.profile

import br.com.isgreen.archandroid.data.local.PreferencesHelper
import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 07/22/2020.
 */

class ProfileRepository(
    private val apiHelper: ApiHelper,
    private val preferencesHelper: PreferencesHelper
) : ProfileContract.Repository {

    //region Api
    override suspend fun fetchUser() = apiHelper.fetchUser()
    override suspend fun fetchUserRepos(userUuid: String) = apiHelper.fetchUserRepos(userUuid)
    //endregion Api

    //region Preferences
    override suspend fun logout() = preferencesHelper.clearData()
    //endregion Preferences

}