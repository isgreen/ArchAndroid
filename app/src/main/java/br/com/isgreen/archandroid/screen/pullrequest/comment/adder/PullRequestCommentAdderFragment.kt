package br.com.isgreen.archandroid.screen.pullrequest.comment.adder

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseDialogFragment
import br.com.isgreen.archandroid.extension.*
import kotlinx.android.synthetic.main.fragment_pull_request_comment_adder.*
import kotlinx.android.synthetic.main.fragment_pull_request_decline.*
import kotlinx.android.synthetic.main.fragment_pull_request_decline.edtDeclineMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 12/19/2020.
 */

class PullRequestCommentAdderFragment : BaseDialogFragment() {

    companion object {
        const val RESULT_KEY_PULL_REQUEST_COMMENT_SENT = "pullRequestCommentSent"
    }

    override val module = pullRequestCommentAdderModule
    override val screenLayout = R.layout.fragment_pull_request_comment_adder
    override val viewModel: PullRequestCommentAdderViewModel by viewModel()

    private val mArguments: PullRequestCommentAdderFragmentArgs by navArgs()

    //region BaseFragment
    override fun initObservers() {
        viewModel.pullRequestCommentSent.observe(this, { comment ->
            setNavigationResult(key = RESULT_KEY_PULL_REQUEST_COMMENT_SENT, result = comment)
            popBackStack()
        })
    }

    override fun initView() {
        edtCommentMessage?.requestFocus()

        btnCommentSave?.setOnClickListener {
            sendComment()
        }

        btnCommentCancel?.setOnClickListener {
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

    private fun sendComment() {
        val pullRequest = mArguments.argPullRequest

        viewModel.sendPullRequestComment(
            pullRequestId = pullRequest?.id,
            message = edtCommentMessage?.text?.toString(),
            repoFullName = pullRequest?.destination?.repository?.fullName
        )

        hideKeyboard()
    }
    //endregion Local
}