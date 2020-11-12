package br.com.isgreen.archandroid.screen.pullrequest.merge

import br.com.isgreen.archandroid.data.model.merge.MergeStrategy
import br.com.isgreen.archandroid.data.model.merge.PullRequestMergeParameter
import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper
import br.com.isgreen.archandroid.helper.resource.ResourceHelper

/**
 * Created by Éverdes Soares on 08/30/2020.
 */

class PullRequestMergeRepository(
    private val apiHelper: ApiHelper,
    private val resourceHelper: ResourceHelper
) : PullRequestMergeContract.Repository {

    //region Resource
    override suspend fun fetchMergeStrategy(): List<MergeStrategy> =
        resourceHelper.fetchMergeStrategies()
    //endregion Resource

    //region Api
    override suspend fun doMerge(
        pullRequestId: Int,
        repoFullName: String,
        message: String,
        mergeStrategyValue: String,
        isCloseSourceBranch: Boolean
    ) {
        apiHelper.doPullRequestsMerge(
            pullRequestId = pullRequestId,
            repoFullName = repoFullName,
            pullRequestMergeParameter = PullRequestMergeParameter(
                type = "",
                message = message,
                mergeStrategy = mergeStrategyValue,
                closeSourceBranch = isCloseSourceBranch
            )
        )
    }
    //endregion Api

}