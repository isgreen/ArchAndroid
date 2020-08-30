package br.com.isgreen.archandroid.screen.pullrequest.merge

import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper

/**
 * Created by Éverdes Soares on 08/30/2020.
 */

class PullRequestMergeRepository(
    private val apiHelper: ApiHelper
) : PullRequestMergeContract.Repository {

    //region Api
    override suspend fun doMerge(pullRequestId: Int, repoFullName: String) {

    }
    //endregion Api

}