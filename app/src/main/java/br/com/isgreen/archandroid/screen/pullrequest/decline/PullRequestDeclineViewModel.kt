package br.com.isgreen.archandroid.screen.pullrequest.decline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper

/**
 * Created by Éverdes Soares on 12/19/2020.
 */

class PullRequestDeclineViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: PullRequestDeclineContract.Repository
) : BaseViewModel(exceptionHandlerHelper), PullRequestDeclineContract.ViewModel {

    override val pullRequestDeclined: LiveData<Unit>
        get() = mPullRequestDeclined

    private val mPullRequestDeclined = MutableLiveData<Unit>()

    override fun doPullRequestDecline(
        pullRequestId: Int?,
        repoFullName: String?,
        message: String?
    ) {
        defaultLaunch {
            if (pullRequestId != null && repoFullName != null) {
                val names = repoFullName.split("/")

                if (names.size == 2) {
                    repository.doPullRequestDecline(
                        message = message,
                        workspace = names[0],
                        repoSlug = names[1],
                        pullRequestId = pullRequestId
                    )

                    mPullRequestDeclined.postValue(Unit)
                }
            }
        }
    }
}