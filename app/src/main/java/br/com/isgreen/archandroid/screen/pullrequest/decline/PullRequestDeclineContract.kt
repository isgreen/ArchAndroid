package br.com.isgreen.archandroid.screen.pullrequest.decline

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest

/**
 * Created by Éverdes Soares on 18/19/2020.
 */

interface PullRequestDeclineContract {

    interface ViewModel : BaseContract.ViewModel {
        val pullRequestDeclined: LiveData<PullRequest>

        fun doPullRequestDecline(
            pullRequestId: Int?,
            repoFullName: String?,
            message: String?
        )
    }

    interface Repository {
        suspend fun doPullRequestDecline(
            workspace: String,
            repoSlug: String,
            pullRequestId: Int,
            message: String?
        ): PullRequest
    }
}