package br.com.isgreen.archandroid.screen.user

import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 07/22/2020.
 */

class UserRepository(
    private val apiHelper: ApiHelper
) : UserContract.Repository {

    override suspend fun fetchUser() = apiHelper.fetchUser()

}