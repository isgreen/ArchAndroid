package br.com.isgreen.archandroid.screen.pullrequest

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.extension.*
import br.com.isgreen.archandroid.screen.pullrequest.merge.PullRequestMergeFragment
import br.com.isgreen.archandroid.util.listener.OnRecyclerViewScrollListener
import kotlinx.android.synthetic.main.appbar_and_toolbar.*
import kotlinx.android.synthetic.main.fragment_pull_request.*
import kotlinx.android.synthetic.main.fragment_pull_request_item.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/07/2020.
 */

class PullRequestFragment : BaseFragment() {

    override val module = pullRequestModule
    override val screenLayout = R.layout.fragment_pull_request
    override val viewModel: PullRequestViewModel by viewModel()

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
            mAdapter.addData(pullRequests)
        })
    }

    override fun initView() {
        toolbar?.Builder(appCompatActivity)
            ?.titleIcon(R.drawable.ic_android)
            ?.displayHome(false)
            ?.title(R.string.app_name)
            ?.build()

        mAdapter.apply {
            onItemClickListener = { _, _, pullRequest ->
                showPullRequestDetail(pullRequest)
            }
            onInnerViewItemClickListener = { view, _, pullRequest ->
                showMenu(view, pullRequest as PullRequest)
            }
        }

        rvPullRequest?.let { recyclerView ->
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = mLayoutManager
            recyclerView.addOnScrollListener(onRecyclerScrollListener)
        }

        pvPullRequest?.onClickTryAgain = {
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
        pbPullRequest?.isVisible = isLoading
    }

    private fun changeLoadingMore(isLoading: Boolean) {
        onRecyclerScrollListener.isLoading = isLoading
        if (isLoading && !mAdapter.isLoading()) {
            mAdapter.showLoading(true)
            rvPullRequest?.smoothScrollToPosition(mAdapter.lastIndex)
        } else {
            mAdapter.hideLoading()
        }
    }

    private fun showPlaceholderMessage(message: String) {
        pvPullRequest?.icon(R.drawable.ic_alert_triangle)
            ?.hideTryAgain()
            ?.text(message)
            ?.show()
    }

    private fun showMenu(view: View?, pullRequest: PullRequest) {
        showPopupMenu(
            anchorView = view,
            menuRes = R.menu.menu_pull_request,
            onMenuItemClickListener = {
                when (it.itemId) {
                    R.id.menu_item_approve -> {
                        doPullRequestApprove(pullRequest)
                        true
                    }
                    R.id.menu_item_decline -> {
                        doPullRequestDecline(pullRequest)
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

    private fun doPullRequestApprove(pullRequest: PullRequest) {
        viewModel.doPullRequestApprove(
            pullRequestId = pullRequest.id,
            repoFullName = pullRequest.destination?.repository?.fullName
        )
    }

    private fun doPullRequestDecline(pullRequest: PullRequest) {
        viewModel.doPullRequestDecline(
            pullRequestId = pullRequest.id,
            repoFullName = pullRequest.destination?.repository?.fullName
        )
    }

    private fun showPullRequestMerge(pullRequest: PullRequest) {
        val direction = PullRequestFragmentDirections
            .actionPullRequestFragmentToPullRequestMergeFragment(pullRequest)
        navigateForResult<Boolean>(
            directions = direction,
            key = PullRequestMergeFragment.RESULT_KEY_PULL_REQUEST_MERGED,
            onNavigationResult = {
                fetchInitialData()
            }
        )
    }

    private fun showPullRequestDetail(pullRequest: PullRequest) {
        val direction = PullRequestFragmentDirections
            .actionPullRequestFragmentToPullRequestDetailFragment(pullRequest)

        setNavigationResultObserver<Boolean>(
            key = PullRequestMergeFragment.RESULT_KEY_PULL_REQUEST_MERGED,
            onNavigationResult = {
                fetchInitialData()
            }
        )

        navigate(directions = direction, sharedElements = clPullRequest to getString(R.string.shared_element_pull_request))
        hideNavigationBottom()
    }
    //endregion Local
}