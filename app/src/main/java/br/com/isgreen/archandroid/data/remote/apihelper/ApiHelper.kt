package br.com.isgreen.archandroid.data.remote.apihelper

import br.com.isgreen.archandroid.data.model.login.Authorization
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.repository.FetchRepositoriesResponse

/**
 * Created by Éverdes Soares on 08/05/2019.
 */

interface ApiHelper {

    //region Login
    suspend fun doLogin(grantType: String, username: String, password: String): Authorization
    //endregion Login

    //region Repositories
    suspend fun fetchRepos(sort: String?, role: String?, after: String?): FetchRepositoriesResponse
    //endregion Repositories

    suspend fun fetchUser(username: String, password: String): User

}