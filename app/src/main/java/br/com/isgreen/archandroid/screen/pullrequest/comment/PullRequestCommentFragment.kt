package br.com.isgreen.archandroid.screen.pullrequest.comment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.data.model.comment.Comment
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.databinding.FragmentPullRequestCommentBinding
import br.com.isgreen.archandroid.extension.navigateForResult
import br.com.isgreen.archandroid.extension.showToast
import br.com.isgreen.archandroid.screen.pullrequest.comment.adder.PullRequestCommentAdderFragment
import br.com.isgreen.archandroid.screen.pullrequest.detail.PullRequestDetailFragmentDirections
import br.com.isgreen.archandroid.util.listener.OnRecyclerViewScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/17/2020.
 */

class PullRequestCommentFragment : BaseFragment<FragmentPullRequestCommentBinding>() {

    companion object {
        const val ARG_PULL_REQUEST = "argPullRequest"

        fun newInstance(pullRequest: PullRequest?): PullRequestCommentFragment {
            return PullRequestCommentFragment().apply {
                this.arguments = generateArgBundle(pullRequest)
            }
        }
    }

    override val module = pullRequestCommentModule
    override val screenLayout = R.layout.fragment_pull_request_comment
    override val viewModel: PullRequestCommentViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentPullRequestCommentBinding
        get() = FragmentPullRequestCommentBinding::inflate

    //region RecyclerView
    private val mAdapter: PullRequestCommentAdapter by lazy { PullRequestCommentAdapter() }
    private val mLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(context) }
    private val onRecyclerScrollListener =
        object : OnRecyclerViewScrollListener(mLayoutManager, DIRECTION_END) {
            override fun loadMore(page: Int) {
                val pullRequest = arguments?.getParcelable<PullRequest>(ARG_PULL_REQUEST)
                viewModel.fetchPullRequestComments(
                    isRefresh = false,
                    pullRequestId = pullRequest?.id,
                    repoFullName = pullRequest?.destination?.repository?.fullName
                )
            }
        }
    //endregion RecyclerView

    //region BaseFragment
    override fun initObservers() {
        viewModel.loadingMoreChanged.observe(this, { isLoading ->
            changeLoadingMore(isLoading)
        })
        viewModel.commentsCleared.observe(this, {
            mAdapter.clearData()
        })
        viewModel.commentsNotFound.observe(this, {
            showPlaceholderMessage(getString(R.string.no_data))
        })
        viewModel.commentsFetched.observe(this, { comments ->
            mAdapter.addData(comments)
        })
    }

    override fun initView() {
        mAdapter.onItemClickListener = { _, _, comment ->
            showCommentDetail(comment)
        }

        binding.rvPullRequestComment.let { recyclerView ->
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = mLayoutManager
            recyclerView.addOnScrollListener(onRecyclerScrollListener)
        }

        binding.pvPullRequestComment.onClickTryAgain = {
            fetchInitialData()
        }

        binding.btnAddComment.setOnClickListener {
            showCommentAdder()
        }
    }

    override fun fetchInitialData() {
        val pullRequest = arguments?.getParcelable<PullRequest>(ARG_PULL_REQUEST)

        viewModel.fetchPullRequestComments(
            isRefresh = true,
            pullRequestId = pullRequest?.id,
            repoFullName = pullRequest?.destination?.repository?.fullName
        )
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        changeLoading(isLoading)
    }

    override fun showError(message: String) {
        if (mAdapter.isEmpty) {
            binding.pvPullRequestComment.icon(R.drawable.ic_alert_triangle)
                .text(message)
                .show()
        } else {
            showToast(message)
        }
    }
    //endregion BaseFragment

    //region Local
    private fun generateArgBundle(pullRequest: PullRequest?): Bundle {
        return bundleOf(ARG_PULL_REQUEST to pullRequest)
    }

    private fun changeLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun changeLoadingMore(isLoading: Boolean) {
        onRecyclerScrollListener.isLoading = isLoading
        if (isLoading && !mAdapter.isLoading()) {
            mAdapter.showLoading(true)
            binding.rvPullRequestComment.smoothScrollToPosition(mAdapter.lastIndex)
        } else {
            mAdapter.hideLoading()
        }
    }

    private fun showPlaceholderMessage(message: String) {
        binding.pvPullRequestComment.icon(R.drawable.ic_alert_triangle)
            .hideTryAgain()
            .text(message)
            .show()
    }

    private fun showCommentAdder() {
        val pullRequest = arguments?.getParcelable<PullRequest>(ARG_PULL_REQUEST)
        val direction = PullRequestDetailFragmentDirections
            .actionPullRequestDetailFragmentToPullRequestCommentAdderFragment(pullRequest)
        navigateForResult<Comment>(
            directions = direction,
            key = PullRequestCommentAdderFragment.RESULT_KEY_PULL_REQUEST_COMMENT_SENT,
            onNavigationResult = { comment ->
                mAdapter.addItem(comment)
            }
        )
    }

    private fun showCommentDetail(comment: Comment) {

    }

    fun updateArguments(pullRequest: PullRequest?){
        arguments = generateArgBundle(pullRequest)
        fetchInitialData()
    }
    //endregion Local
}