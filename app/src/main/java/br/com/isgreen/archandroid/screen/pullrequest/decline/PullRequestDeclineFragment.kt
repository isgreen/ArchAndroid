package br.com.isgreen.archandroid.screen.pullrequest.decline

import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseDialogFragment
import br.com.isgreen.archandroid.databinding.FragmentPullRequestDeclineBinding
import br.com.isgreen.archandroid.extension.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 12/19/2020.
 */

class PullRequestDeclineFragment : BaseDialogFragment<FragmentPullRequestDeclineBinding>() {

    companion object {
        const val RESULT_KEY_PULL_REQUEST_DECLINED = "pullRequestDeclined"
    }

    override val module = pullRequestDeclineModule
    override val screenLayout = R.layout.fragment_pull_request_decline
    override val viewModel: PullRequestDeclineViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentPullRequestDeclineBinding
        get() = FragmentPullRequestDeclineBinding::inflate

    private val mArguments: PullRequestDeclineFragmentArgs by navArgs()

    //region BaseFragment
    override fun initObservers() {
        viewModel.pullRequestDeclined.observe(this, { pullRequest ->
            // TODO: 29/12/20 atualizar o objeto PullRequest na tela de PullRequestDetailFragment e na (PullRequestFragment)
            setNavigationResult(key = RESULT_KEY_PULL_REQUEST_DECLINED, result = pullRequest)
            popBackStack()
        })
    }

    override fun initView() {
        binding.edtDeclineMessage.requestFocus()
        binding.btnDecline.setOnClickListener { doDecline() }
        binding.btnDeclineCancel.setOnClickListener { dismiss() }
    }

    override fun fetchInitialData() {}

    override fun onLoadingChanged(isLoading: Boolean) {
        changeLoading(isLoading)
    }

    override fun showError(message: String) {
        showToast(message)
    }
    //endregion BaseFragment

    //region Local
    private fun changeLoading(isLoading: Boolean) {
        binding.btnDecline.isEnabled = !isLoading
        binding.btnDeclineCancel.isEnabled = !isLoading
        binding.edtDeclineMessage.isEnabled = !isLoading
        binding.pbPullRequestDecline.isVisible = isLoading
    }

    private fun doDecline() {
        val pullRequest = mArguments.argPullRequest

        viewModel.doPullRequestDecline(
            pullRequestId = pullRequest?.id,
            message = binding.edtDeclineMessage.text?.toString(),
            repoFullName = pullRequest?.destination?.repository?.fullName
        )

        hideKeyboard()
    }
    //endregion Local
}