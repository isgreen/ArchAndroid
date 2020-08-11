package br.com.isgreen.archandroid.screen.pullrequest.overview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.os.bundleOf
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.extension.loadImageRounded
import br.com.isgreen.archandroid.extension.setDate
import br.com.isgreen.archandroid.extension.showToast
import com.google.android.material.transition.platform.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_pull_request_overview.*
import kotlinx.android.synthetic.main.fragment_pull_request_overview_author.*
import org.koin.core.module.Module

/**
 * Created by Éverdes Soares on 08/10/2020.
 */

class PullRequestOverviewFragment : BaseFragment() {

    companion object {
        const val ARG_PULL_REQUEST = "argPullRequest"

        fun newInstance(pullRequest: PullRequest?): PullRequestOverviewFragment {
            return PullRequestOverviewFragment().apply {
                this.arguments = bundleOf(ARG_PULL_REQUEST to pullRequest)
            }
        }
    }

    override val module: Module? = null
    override val viewModel: BaseViewModel? = null
    override val screenLayout = R.layout.fragment_pull_request_overview

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }
    //endregion Fragment

    //region BaseFragment
    override fun initObservers() {}

    override fun initView() {
        setDataInView()
    }

    override fun fetchInitialData() {}

    override fun onLoadingChanged(isLoading: Boolean) {}

    override fun showError(message: String) {
        showToast(message)
    }
    //endregion BaseFragment

    //region Local
    @SuppressLint("SetTextI18n")
    private fun setDataInView() {
        val pullRequest = arguments?.getParcelable<PullRequest>(ARG_PULL_REQUEST)
        val author = pullRequest?.author

        txtAuthorNickname?.text = author?.nickname
        txtPullRequestAuthor?.text = author?.displayName
        imgUser?.loadImageRounded(
            author?.links?.avatar?.href,
            R.drawable.ic_user, R.dimen.margin_small
        )

        txtPullRequestDescription?.text = pullRequest?.description
        txtRepositoryName?.text = pullRequest?.destination?.repository?.name
        txtPullRequestCreatedDate?.setDate(R.string.created_on, pullRequest?.createdOn?.substring(0, 19))
        txtPullRequestBranches?.text = "${pullRequest?.source?.branch?.name} > ${pullRequest?.destination?.branch?.name}"
        imgRepositoryIcon?.loadImageRounded(
            pullRequest?.destination?.repository?.links?.avatar?.href,
            R.drawable.ic_user, R.dimen.margin_small
        )
    }
    //endregion Local
}