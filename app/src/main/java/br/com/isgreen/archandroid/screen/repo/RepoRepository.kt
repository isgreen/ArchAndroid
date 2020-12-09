package br.com.isgreen.archandroid.screen.repo

import br.com.isgreen.archandroid.data.model.repository.FetchReposResponse
import br.com.isgreen.archandroid.data.remote.api.ApiConstant
import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 09/22/2019.
 */

class RepoRepository(
    private val apiHelper: ApiHelper
) : RepoContract.Repository {

    override suspend fun fetchRepos(
        nextUrl: String?,
        sort: String?,
        role: String?
    ): FetchReposResponse {
        val fullUrl = if (nextUrl == null) {
            val url = ApiConstant.FETCH_REPOS
            val query = "role=$role"//"sort=$sort&role=$role"
            "$url?$query"
        } else {
            nextUrl
        }

        return apiHelper.fetchRepos(fullUrl)
    }

}