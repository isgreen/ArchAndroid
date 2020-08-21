package br.com.isgreen.archandroid.screen.pullrequest.comment

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.data.model.comment.Comment
import br.com.isgreen.archandroid.extension.showToast
import br.com.isgreen.archandroid.util.listener.OnRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_pull_request_comment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/17/2020.
 */

class PullRequestCommentFragment : BaseFragment() {

    companion object {
        const val ARG_REPO_FULL_NAME = "argRepoFullName"
        const val ARG_PULL_REQUEST_ID = "argPullRequestId"

        fun newInstance(pullRequestId: Int?, repoFullName: String?): PullRequestCommentFragment {
            return PullRequestCommentFragment().apply {
                this.arguments = bundleOf(
                    ARG_REPO_FULL_NAME to repoFullName,
                    ARG_PULL_REQUEST_ID to pullRequestId
                )
            }
        }
    }

    override val module = pullRequestCommentModule
    override val screenLayout = R.layout.fragment_pull_request_comment
    override val viewModel: PullRequestCommentViewModel by viewModel()

    //region RecyclerView
    private val mAdapter: PullRequestCommentAdapter by lazy { PullRequestCommentAdapter() }
    private val mLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(context) }
    private val onRecyclerScrollListener =
        object : OnRecyclerViewScrollListener(mLayoutManager, DIRECTION_END) {
            override fun loadMore(page: Int) {
                viewModel.fetchPullRequestComments(
                    isRefresh = false,
                    pullRequestId = arguments?.getInt(ARG_PULL_REQUEST_ID),
                    repoFullName = arguments?.getString(ARG_REPO_FULL_NAME)
                )
            }
        }
    //endregion RecyclerView

    //region BaseFragment
    override fun initObservers() {
        viewModel.loadingMoreChanged.observe(this, Observer { isLoading ->
            changeLoadingMore(isLoading)
        })
        viewModel.commentsCleared.observe(this, Observer {
            mAdapter.clearData()
        })
        viewModel.commentsFetched.observe(this, Observer { comments ->
            mAdapter.addData(comments)
        })
    }

    override fun initView() {
        mAdapter.onItemClickListener = { _, _, comment ->
            showCommentDetail(comment)
        }

        rvPullRequestComment?.let { recyclerView ->
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = mLayoutManager
            recyclerView.addOnScrollListener(onRecyclerScrollListener)
        }

        pvPullRequestComment?.onClickTryAgain = {
            fetchInitialData()
        }

        btnAddComment?.setOnClickListener {

        }
    }

    override fun fetchInitialData() {
        viewModel.fetchPullRequestComments(
            isRefresh = true,
            pullRequestId = arguments?.getInt(ARG_PULL_REQUEST_ID),
            repoFullName = arguments?.getString(ARG_REPO_FULL_NAME)
        )
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        changeLoading(isLoading)
    }

    override fun showError(message: String) {
        if (mAdapter.isEmpty) {
            pvPullRequestComment?.icon(R.drawable.ic_alert_triangle)
                ?.text(message)
                ?.show()
        } else {
            showToast(message)
        }
    }
    //endregion BaseFragment

    //region Local
    private fun changeLoading(isLoading: Boolean) {
        pbPullRequestComment?.isVisible = isLoading
    }

    private fun changeLoadingMore(isLoading: Boolean) {
        onRecyclerScrollListener.isLoading = isLoading
        if (isLoading && !mAdapter.isLoading()) {
            mAdapter.showLoading(true)
            rvPullRequestComment?.smoothScrollToPosition(mAdapter.lastIndex)
        } else {
            mAdapter.hideLoading()
        }
    }

    private fun showCommentDetail(comment: Comment) {

    }
    //endregion Local
}