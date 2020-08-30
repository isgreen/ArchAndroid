package br.com.isgreen.archandroid.screen.pullrequest.merge

import br.com.isgreen.archandroid.base.BaseContract

/**
 * Created by Éverdes Soares on 08/30/2020.
 */

interface PullRequestMergeContract {

    interface ViewModel : BaseContract.ViewModel {
        fun doMerge(
            pullRequestId: Int?,
            repoFullName: String?
        )
    }

    interface Repository {
        suspend fun doMerge(
            pullRequestId: Int,
            repoFullName: String
        )
    }
}