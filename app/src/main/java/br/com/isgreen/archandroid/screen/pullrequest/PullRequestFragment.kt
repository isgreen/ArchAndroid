package br.com.isgreen.archandroid.screen.pullrequest

import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.extension.appCompatActivity
import br.com.isgreen.archandroid.extension.showToast
import br.com.isgreen.archandroid.util.listener.OnRecyclerViewScrollListener
import com.google.android.material.transition.platform.Hold
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
        exitTransition = Hold()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showNavigationBottom()
    }
    //endregion Fragment

    //region BaseFragment
    override fun initObservers() {
        viewModel.loadingMoreChanged.observe(this, Observer { isLoading ->
            changeLoadingMore(isLoading)
        })
        viewModel.pullRequestsCleared.observe(this, Observer {
            mAdapter.clearData()
        })
        viewModel.pullRequestsFetched.observe(this, Observer { pullRequests ->
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
        viewModel.fetchPullRequests(isRefresh = true)
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        changeLoading(isLoading)
    }

    override fun showError(message: String) {
        if (mAdapter.isEmpty) {
            pvPullRequest?.icon(R.drawable.ic_alert_triangle)
                ?.text(message)
                ?.show()
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

    private fun showMenu(view: View?, pullRequest: PullRequest) {
        val popup = PopupMenu(context, view)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_approve -> {
                    // TODO: 26/08/20 code to approve
                    true
                }
                R.id.menu_item_decline -> {
                    // TODO: 26/08/20 code to decline
                    true
                }
                R.id.menu_item_merge -> {
                    // TODO: 26/08/20 code to merge
                    true
                }
                else -> {
                    false
                }
            }
        }
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_pull_request_detail, popup.menu)
        popup.show()
    }

    private fun showPullRequestDetail(pullRequest: PullRequest) {
        val direction = PullRequestFragmentDirections
            .actionPullRequestFragmentToPullRequestDetailFragment(pullRequest)
        navigate(directions = direction, sharedElements = clPullRequest to getString(R.string.shared_element_pull_request))
        hideNavigationBottom()
    }
    //endregion Local
}