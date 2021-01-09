package br.com.isgreen.archandroid.screen.pullrequest.comment.adder

import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseDialogFragment
import br.com.isgreen.archandroid.databinding.FragmentPullRequestCommentAdderBinding
import br.com.isgreen.archandroid.extension.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 12/19/2020.
 */

class PullRequestCommentAdderFragment : BaseDialogFragment<FragmentPullRequestCommentAdderBinding>() {

    companion object {
        const val RESULT_KEY_PULL_REQUEST_COMMENT_SENT = "pullRequestCommentSent"
    }

    override val module = pullRequestCommentAdderModule
    override val screenLayout = R.layout.fragment_pull_request_comment_adder
    override val viewModel: PullRequestCommentAdderViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentPullRequestCommentAdderBinding
        get() = FragmentPullRequestCommentAdderBinding::inflate

    private val mArguments: PullRequestCommentAdderFragmentArgs by navArgs()

    //region BaseFragment
    override fun initObservers() {
        viewModel.pullRequestCommentSent.observe(this, { comment ->
            setNavigationResult(key = RESULT_KEY_PULL_REQUEST_COMMENT_SENT, result = comment)
            popBackStack()
        })
    }

    override fun initView() {
        binding.edtCommentMessage.requestFocus()
        binding.btnCommentSave.setOnClickListener { sendComment() }
        binding.btnCommentCancel.setOnClickListener { dismiss() }
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
        binding.progressBar.isVisible = isLoading
        binding.btnCommentSave.isEnabled = !isLoading
        binding.btnCommentCancel.isEnabled = !isLoading
        binding.edtCommentMessage.isEnabled = !isLoading
    }

    private fun sendComment() {
        val pullRequest = mArguments.argPullRequest

        viewModel.sendPullRequestComment(
            pullRequestId = pullRequest?.id,
            message = binding.edtCommentMessage.text?.toString(),
            repoFullName = pullRequest?.destination?.repository?.fullName
        )

        hideKeyboard()
    }
    //endregion Local
}