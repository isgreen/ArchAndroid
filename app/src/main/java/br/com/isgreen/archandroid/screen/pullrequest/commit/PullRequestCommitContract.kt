package br.com.isgreen.archandroid.screen.pullrequest.commit

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.commit.Commit
import br.com.isgreen.archandroid.data.model.commit.FetchPullRequestCommitsResponse

/**
 * Created by Éverdes Soares on 08/12/2020.
 */

interface PullRequestCommitContract {

    interface ViewModel : BaseContract.ViewModel {
        val commitsCleared: LiveData<Unit>
        val loadingMoreChanged: LiveData<Boolean>
        val commitsFetched: LiveData<List<Commit>>

        fun fetchPullRequestCommits(
            isRefresh: Boolean = false,
            pullRequestId: Int?,
            repoFullName: String?
        )
    }

    interface Repository {
        suspend fun fetchCommits(
            nextUrl: String?,
            repoSlug: String,
            workspace: String,
            pullRequestId: Int,
        ): FetchPullRequestCommitsResponse
    }
}