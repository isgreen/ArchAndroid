package br.com.isgreen.archandroid.screen.repo

import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 09/22/2019.
 */

class RepoRepository(
    private val apiHelper: ApiHelper
) : RepoContract.Repository {

    override suspend fun fetchRepos(sort: String?, role: String?, after: String?) =
        apiHelper.fetchRepos(sort, role, after)

}