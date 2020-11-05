package br.com.isgreen.archandroid.screen.pullrequest.merge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.merge.MergeStrategy
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import kotlinx.coroutines.launch

/**
 * Created by Éverdes Soares on 08/30/2020.
 */

class PullRequestMergeViewModel(
    exceptionHandlerHelper: ExceptionHandlerHelper,
    private val repository: PullRequestMergeContract.Repository
) : BaseViewModel(exceptionHandlerHelper), PullRequestMergeContract.ViewModel {

    override val pullRequestMerged: LiveData<Unit>
        get() = mPullRequestMerged
    override val mergeStrategiesFetched: LiveData<List<MergeStrategy>>
        get() = mMergeStrategiesFetched

    private val mPullRequestMerged = MutableLiveData<Unit>()
    private val mMergeStrategiesFetched = MutableLiveData<List<MergeStrategy>>()

    override fun fetchMergeStrategies() {
        viewModelScope.launch {
            val mergeStrategies = repository.fetchMergeStrategy()
            mMergeStrategiesFetched.postValue(mergeStrategies)
        }
    }

    override fun doMerge(
        pullRequestId: Int?,
        repoFullName: String?,
        mergeStrategyPosition: Int?,
        isCloseSourceBranch: Boolean?
    ) {
        defaultLaunch {
//            val mergeStrategies = mMergeStrategiesFetched.value
//            val mergeStrategyValue = if (mergeStrategyPosition != null && mergeStrategies != null)
//                mergeStrategies[mergeStrategyPosition].value
//            else
//                "merge_commit"
//
//            repository.doMerge(
//                pullRequestId = pullRequestId ?: 0,
//                repoFullName = repoFullName ?: "",
//                mergeStrategyValue = mergeStrategyValue,
//                isCloseSourceBranch = isCloseSourceBranch == true
//            )

            mPullRequestMerged.postValue(Unit)
        }
    }
}