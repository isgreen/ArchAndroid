package br.com.isgreen.archandroid.screen.pullrequest.overview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.databinding.FragmentPullRequestOverviewBinding
import br.com.isgreen.archandroid.extension.*
import org.koin.core.module.Module

/**
 * Created by Éverdes Soares on 08/10/2020.
 */

class PullRequestOverviewFragment : BaseFragment<FragmentPullRequestOverviewBinding>() {

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
    override val bindingInflater: (LayoutInflater) -> FragmentPullRequestOverviewBinding
        get() = FragmentPullRequestOverviewBinding::inflate

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
        lifecycleScope.launchWhenResumed {
            val pullRequest = arguments?.getParcelable<PullRequest>(ARG_PULL_REQUEST)
            val author = pullRequest?.author

            binding.includeAuthor.txtAuthorNickname.text = author?.nickname
            binding.includeAuthor.txtPullRequestAuthor.text = author?.displayName
            binding.includeAuthor.imgUser.loadImageRounded(
                author?.links?.avatar?.href,
                R.drawable.ic_user, R.dimen.margin_small
            )

            val color = pullRequest?.stateColor ?: R.color.red
            binding.includeDestination.txtPullRequestStatus.putTextColor(color)
            binding.includeDestination.txtPullRequestStatus.setBackgroundDrawableColor(color)
            binding.includeDestination.txtPullRequestStatus.text = pullRequest?.state?.value
            binding.includeDestination.txtRepositoryName.text = pullRequest?.destination?.repository?.name
            binding.includeDestination.txtPullRequestCreatedDate.setDate(R.string.created_on, pullRequest?.createdOn?.substring(0, 19))
            binding.includeDestination.txtPullRequestBranches.text =
                "${pullRequest?.source?.branch?.name} > ${pullRequest?.destination?.branch?.name}"
            binding.includeDestination.imgRepositoryIcon.loadImageRounded(
                pullRequest?.destination?.repository?.links?.avatar?.href,
                R.drawable.ic_user, R.dimen.margin_small
            )

            binding.txtPullRequestDescription.text = pullRequest?.description
            binding.txtDescriptionTitle.isVisible = !pullRequest?.description.isNullOrEmpty()
        }
    }

    fun updatePullRequest(pullRequest: PullRequest){
        arguments = bundleOf(ARG_PULL_REQUEST to pullRequest)
        setDataInView()
    }
    //endregion Local
}