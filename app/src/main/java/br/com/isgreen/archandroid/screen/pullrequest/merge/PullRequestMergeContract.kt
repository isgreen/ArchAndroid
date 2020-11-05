package br.com.isgreen.archandroid.screen.pullrequest.merge

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.merge.MergeStrategy

/**
 * Created by Éverdes Soares on 08/30/2020.
 */

interface PullRequestMergeContract {

    interface ViewModel : BaseContract.ViewModel {
        val pullRequestMerged: LiveData<Unit>
        val mergeStrategiesFetched: LiveData<List<MergeStrategy>>

        fun fetchMergeStrategies()
        fun doMerge(
            pullRequestId: Int?,
            repoFullName: String?,
            mergeStrategyPosition: Int?,
            isCloseSourceBranch: Boolean?
        )
    }

    interface Repository {
        suspend fun fetchMergeStrategy(): List<MergeStrategy>
        suspend fun doMerge(
            pullRequestId: Int,
            repoFullName: String,
            mergeStrategyValue: String,
            isCloseSourceBranch: Boolean
        )
    }
}