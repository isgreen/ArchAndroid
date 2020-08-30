package br.com.isgreen.archandroid.screen.pullrequest.merge

import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper

/**
 * Created by Éverdes Soares on 08/30/2020.
 */

class PullRequestMergeViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: PullRequestMergeContract.Repository
) : BaseViewModel(exceptionHandlerHelper), PullRequestMergeContract.ViewModel {

    override fun doMerge(pullRequestId: Int?, repoFullName: String?) {
        defaultLaunch {
            repository.doMerge(
                pullRequestId = pullRequestId ?: 0,
                repoFullName = repoFullName ?: ""
            )
        }
    }
}