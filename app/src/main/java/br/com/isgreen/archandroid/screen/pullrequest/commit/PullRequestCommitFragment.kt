package br.com.isgreen.archandroid.screen.pullrequest.commit

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.data.model.commit.Commit
import br.com.isgreen.archandroid.databinding.FragmentPullRequestCommitBinding
import br.com.isgreen.archandroid.extension.showToast
import br.com.isgreen.archandroid.util.listener.OnRecyclerViewScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/12/2020.
 */

class PullRequestCommitFragment : BaseFragment<FragmentPullRequestCommitBinding>() {

    companion object {
        const val ARG_REPO_FULL_NAME = "argRepoFullName"
        const val ARG_PULL_REQUEST_ID = "argPullRequestId"

        fun newInstance(pullRequestId: Int?, repoFullName: String?): PullRequestCommitFragment {
            return PullRequestCommitFragment().apply {
                this.arguments = generateArgBundle(pullRequestId, repoFullName)
            }
        }
    }

    override val module = pullRequestCommitModule
    override val screenLayout = R.layout.fragment_pull_request_commit
    override val viewModel: PullRequestCommitViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentPullRequestCommitBinding
        get() = FragmentPullRequestCommitBinding::inflate

    //region RecyclerView
    private val mAdapter: PullRequestCommitAdapter by lazy { PullRequestCommitAdapter() }
    private val mLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(context) }
    private val onRecyclerScrollListener =
        object : OnRecyclerViewScrollListener(mLayoutManager, DIRECTION_END) {
            override fun loadMore(page: Int) {
                viewModel.fetchPullRequestCommits(
                    isRefresh = false,
                    pullRequestId = arguments?.getInt(ARG_PULL_REQUEST_ID),
                    repoFullName = arguments?.getString(ARG_REPO_FULL_NAME)
                )
            }
        }
    //endregion RecyclerView

    //region BaseFragment
    override fun initObservers() {
        viewModel.loadingMoreChanged.observe(this, { isLoading ->
            changeLoadingMore(isLoading)
        })
        viewModel.commitsCleared.observe(this, {
            mAdapter.clearData()
        })
        viewModel.commitsFetched.observe(this, { commits ->
            setDataInList(commits)
        })
    }

    override fun initView() {
        mAdapter.onItemClickListener = { _, _, commit ->
            showCommitDetail(commit)
        }

        binding.rvPullRequestCommit.let { recyclerView ->
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = mLayoutManager
            recyclerView.addOnScrollListener(onRecyclerScrollListener)
        }

        binding.placeholderView.onClickTryAgain = {
            fetchInitialData()
        }
    }

    override fun fetchInitialData() {
        viewModel.fetchPullRequestCommits(
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
            binding.placeholderView.icon(R.drawable.ic_alert_triangle)
                .text(message)
                .show()
        } else {
            showToast(message)
        }
    }
    //endregion BaseFragment

    //region Local
    private fun generateArgBundle(pullRequestId: Int?, repoFullName: String?): Bundle {
        return bundleOf(
            ARG_REPO_FULL_NAME to repoFullName,
            ARG_PULL_REQUEST_ID to pullRequestId
        )
    }

    private fun changeLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun changeLoadingMore(isLoading: Boolean) {
        onRecyclerScrollListener.isLoading = isLoading
        if (isLoading && !mAdapter.isLoading()) {
            mAdapter.showLoading(true)
            binding.rvPullRequestCommit.smoothScrollToPosition(mAdapter.lastIndex)
        } else {
            mAdapter.hideLoading()
        }
    }

    private fun setDataInList(commits: List<Commit>) {
        binding.placeholderView.hide()
        mAdapter.addData(commits)
    }

    private fun showCommitDetail(commit: Commit) {

    }

    fun updateArguments(pullRequestId: Int?, repoFullName: String?){
        arguments = generateArgBundle(pullRequestId, repoFullName)
        fetchInitialData()
    }
    //endregion Local
}