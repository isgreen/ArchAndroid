package br.com.isgreen.archandroid.screen.pullrequest.merge

import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseDialogFragment
import br.com.isgreen.archandroid.data.model.merge.MergeStrategy
import br.com.isgreen.archandroid.extension.appCompatActivity
import br.com.isgreen.archandroid.extension.showToast
import kotlinx.android.synthetic.main.appbar_and_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/30/2020.
 */

class PullRequestMergeFragment : BaseDialogFragment() {

//    companion object {
//        const val ARG_REPO_FULL_NAME = "argRepoFullName"
//        const val ARG_PULL_REQUEST_ID = "argPullRequestId"
//
//        fun newInstance(pullRequestId: Int?, repoFullName: String?): PullRequestMergeFragment {
//            return PullRequestMergeFragment().apply {
//                this.arguments = bundleOf(
//                    ARG_REPO_FULL_NAME to repoFullName,
//                    ARG_PULL_REQUEST_ID to pullRequestId
//                )
//            }
//        }
//    }

    override val module = pullRequestMergeModule
    override val screenLayout = R.layout.fragment_pull_request_merge
    override val viewModel: PullRequestMergeViewModel by viewModel()

    //region BaseFragment
    override fun initObservers() {
        viewModel.mergeStrategiesFetched.observe(this, { mergeStrategies ->
            setDataInSpinner(mergeStrategies)
        })
    }

    override fun initView() {
        toolbar?.Builder(appCompatActivity)
            ?.homeIcon(R.drawable.ic_back)
            ?.title(R.string.merge)
            ?.build()
    }

    override fun fetchInitialData() {

    }

    override fun onLoadingChanged(isLoading: Boolean) {
        changeLoading(isLoading)
    }

    override fun showError(message: String) {
        showToast(message)
    }
    //endregion BaseFragment

    //region Local
    private fun changeLoading(isLoading: Boolean) {

    }

    private fun setDataInSpinner(mergeStrategies: List<MergeStrategy>) {

    }
    //endregion Local
}