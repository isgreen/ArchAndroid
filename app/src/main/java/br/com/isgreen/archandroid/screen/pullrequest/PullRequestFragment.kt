package br.com.isgreen.archandroid.screen.pullrequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.databinding.FragmentPullRequestBinding
import br.com.isgreen.archandroid.extension.*
import br.com.isgreen.archandroid.screen.pullrequest.decline.PullRequestDeclineFragment
import br.com.isgreen.archandroid.screen.pullrequest.detail.PullRequestDetailFragment
import br.com.isgreen.archandroid.screen.pullrequest.merge.PullRequestMergeFragment
import br.com.isgreen.archandroid.util.listener.OnRecyclerViewScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/07/2020.
 */

class PullRequestFragment : BaseFragment<FragmentPullRequestBinding>() {

    override val module = pullRequestModule
    override val screenLayout = R.layout.fragment_pull_request
    override val viewModel: PullRequestViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentPullRequestBinding
        get() = FragmentPullRequestBinding::inflate

    //region RecyclerView
    private val mAdapter: PullRequestAdapter by lazy { PullRequestAdapter() }
    private val mLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(context) }
    private val onRecyclerScrollListener =
        object : OnRecyclerViewScrollListener(mLayoutManager, DIRECTION_END) {
            override fun loadMore(page: Int) {
                viewModel.fetchPullRequests()
            }
        }
    //endregion RecyclerView

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        exitTransition = Hold()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showNavigationBottom()
    }
    //endregion Fragment

    //region BaseFragment
    override fun initObservers() {
        viewModel.loadingMoreChanged.observe(this, { isLoading ->
            changeLoadingMore(isLoading)
        })
        viewModel.pullRequestsCleared.observe(this, {
            mAdapter.clearData()
        })
        viewModel.pullRequestsNotFound.observe(this, {
            showPlaceholderMessage(getString(R.string.no_data))
        })
        viewModel.pullRequestsFetched.observe(this, { pullRequests ->
            setDataInList(pullRequests)
        })
    }

    override fun initView() {
        binding.includeToolbar.toolbar.Builder(appCompatActivity)
            .titleIcon(R.drawable.ic_android)
            .displayHome(false)
            .title(R.string.app_name)
            .build()

        mAdapter.apply {
            onItemClickListener = { view, _, pullRequest ->
                showPullRequestDetail(view, pullRequest)
            }
            onInnerViewItemClickListener = { view, _, pullRequest ->
                showMenu(view, pullRequest as PullRequest)
            }
        }

        binding.rvPullRequest.let { recyclerView ->
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = mLayoutManager
            recyclerView.addOnScrollListener(onRecyclerScrollListener)
        }

        binding.placeholderView.onClickTryAgain = {
            fetchInitialData()
        }
    }

    override fun fetchInitialData() {
        viewModel.fetchPullRequests(isInitialRequest = true)
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        changeLoading(isLoading)
    }

    override fun showError(message: String) {
        if (mAdapter.isEmpty) {
            showPlaceholderMessage(message)
        } else {
            showToast(message)
        }
    }
    //endregion BaseFragment

    //region Local
    private fun changeLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun changeLoadingMore(isLoading: Boolean) {
        onRecyclerScrollListener.isLoading = isLoading
        if (isLoading && !mAdapter.isLoading()) {
            mAdapter.showLoading(true)
            binding.rvPullRequest.smoothScrollToPosition(mAdapter.lastIndex)
        } else {
            mAdapter.hideLoading()
        }
    }

    private fun setDataInList(pullRequests: List<PullRequest>) {
        binding.placeholderView.hide()
        mAdapter.addData(pullRequests)
    }

    private fun showPlaceholderMessage(message: String) {
        binding.placeholderView.icon(R.drawable.ic_alert_triangle)
            .hideTryAgain()
            .text(message)
            .show()
    }

    private fun showMenu(view: View?, pullRequest: PullRequest) {
        showPopupMenu(
            anchorView = view,
            menuRes = R.menu.menu_pull_request,
            onMenuItemClickListener = {
                when (it.itemId) {
                    R.id.menu_item_decline -> {
                        showPullRequestDecline(pullRequest)
                        true
                    }
                    R.id.menu_item_merge -> {
                        showPullRequestMerge(pullRequest)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        )
    }

    private fun showPullRequestMerge(pullRequest: PullRequest) {
        val direction = PullRequestFragmentDirections
            .actionPullRequestFragmentToPullRequestMergeFragment(pullRequest)
        navigateForResult<PullRequest>(
            directions = direction,
            key = PullRequestMergeFragment.RESULT_KEY_PULL_REQUEST_MERGED,
            onNavigationResult = { pullRequestUpdated ->
                mAdapter.setItem(mAdapter.lastItemClickedPosition, pullRequestUpdated)
            }
        )
    }

    private fun showPullRequestDecline(pullRequest: PullRequest) {
        val direction = PullRequestFragmentDirections
            .actionPullRequestFragmentToPullRequestDeclineFragment(pullRequest)
        navigateForResult<PullRequest>(
            directions = direction,
            key = PullRequestDeclineFragment.RESULT_KEY_PULL_REQUEST_DECLINED,
            onNavigationResult = { pullRequestUpdated ->
                mAdapter.setItem(mAdapter.lastItemClickedPosition, pullRequestUpdated)
            }
        )
    }

    private fun showPullRequestDetail(view: View, pullRequest: PullRequest) {
        val direction = PullRequestFragmentDirections
            .actionPullRequestFragmentToPullRequestDetailFragment(pullRequest)

        setNavigationResultObserver<PullRequest>(
            key = PullRequestDetailFragment.RESULT_KEY_PULL_REQUEST_UPDATED,
            onNavigationResult = { pullRequestUpdated ->
                mAdapter.setItem(mAdapter.lastItemClickedPosition, pullRequestUpdated)
            }
        )

        navigate(directions = direction, sharedElements = view to getString(R.string.shared_element_pull_request))
        hideNavigationBottom()
    }
    //endregion Local
}