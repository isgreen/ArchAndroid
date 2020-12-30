package br.com.isgreen.archandroid.screen.pullrequest.decline

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract

/**
 * Created by Éverdes Soares on 18/19/2020.
 */

interface PullRequestDeclineContract {

    interface ViewModel : BaseContract.ViewModel {
        val pullRequestDeclined: LiveData<Unit>

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
        )
    }
}