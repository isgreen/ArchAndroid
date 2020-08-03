package br.com.isgreen.archandroid.screen.pullrequest

import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 08/02/2020.
 */

class PullRequestRepository(
    private val apiHelper: ApiHelper
): PullRequestContract.Repository {

    //region Api
    override suspend fun fetchPullRequests() = apiHelper.fetchPullRequests()
    //endregion Api

}