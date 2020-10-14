package br.com.isgreen.archandroid.screen.pullrequest

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.pullrequest.FetchPullRequestsResponse
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest

/**
 * Created by Éverdes Soares on 08/02/2020.
 */

interface PullRequestContract {

    interface ViewModel : BaseContract.ViewModel {
        val pullRequestsCleared: LiveData<Unit>
        val pullRequestsNotFound: LiveData<Unit>
        val loadingMoreChanged: LiveData<Boolean>
        val pullRequestsFetched: LiveData<List<PullRequest>>

        fun fetchPullRequests(isRefresh: Boolean = false)
    }

    interface Repository {
        suspend fun fetchPullRequests(): FetchPullRequestsResponse
    }

}