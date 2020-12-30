package br.com.isgreen.archandroid.screen.pullrequest.decline

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseDialogFragment
import br.com.isgreen.archandroid.extension.hideKeyboard
import br.com.isgreen.archandroid.extension.popUpTo
import br.com.isgreen.archandroid.extension.setNavigationResult
import br.com.isgreen.archandroid.extension.showToast
import kotlinx.android.synthetic.main.fragment_pull_request_decline.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 12/19/2020.
 */

class PullRequestDeclineFragment : BaseDialogFragment() {

    companion object {
        const val RESULT_KEY_PULL_REQUEST_DECLINED = "pullRequestDeclined"
    }

    override val module = pullRequestDeclineModule
    override val screenLayout = R.layout.fragment_pull_request_decline
    override val viewModel: PullRequestDeclineViewModel by viewModel()

    private val mArguments: PullRequestDeclineFragmentArgs by navArgs()

    //region BaseFragment
    override fun initObservers() {
        viewModel.pullRequestDeclined.observe(this, {
            // TODO: 29/12/20 atualizar o objeto PullRequest na tela de PullRequestDetailFragment e na (PullRequestFragment)
            setNavigationResult(
                key = RESULT_KEY_PULL_REQUEST_DECLINED,
                result = true,
                destinationId = R.id.pullRequestFragment
            )
            popUpTo(R.id.pullRequestFragment)
        })
    }

    override fun initView() {
        edtDeclineMessage?.requestFocus()

        btnDecline?.setOnClickListener {
            doDecline()
        }

        btnDeclineCancel?.setOnClickListener {
            dismiss()
        }
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
        btnDecline?.isEnabled = !isLoading
        btnDeclineCancel?.isEnabled = !isLoading
        edtDeclineMessage?.isEnabled = !isLoading
        pbPullRequestDecline?.isVisible = isLoading
    }

    private fun doDecline() {
        val pullRequest = mArguments.argPullRequest

        viewModel.doPullRequestDecline(
            pullRequestId = pullRequest?.id,
            message = edtDeclineMessage?.text?.toString(),
            repoFullName = pullRequest?.destination?.repository?.fullName
        )

        hideKeyboard()
    }
    //endregion Local
}