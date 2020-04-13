package br.com.isgreen.archandroid.screen.repo

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.extension.showMessage
import br.com.isgreen.archandroid.util.listener.EndlessScrollListener
import br.com.isgreen.archandroid.util.listener.OnScrollCallback
import kotlinx.android.synthetic.main.fragment_repo.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 09/22/2019.
 */

class RepoFragment : BaseFragment() {

    override val module = repoModule
    override val screenLayout = R.layout.fragment_repo
    override val viewModel: RepoViewModel by viewModel()

    //region RecyclerView
    private val mAdapter: RepoAdapter by lazy { RepoAdapter() }
    private val mLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context)
    }
    private val onScroll = object : OnScrollCallback() {
        override fun onLoadMore(page: Int, totalItemsCount: Int) {
            viewModel.fetchRepos()
        }
    }
    private val mEndlessScroll: EndlessScrollListener by lazy {
        EndlessScrollListener(onScroll, mLayoutManager)
    }
    //endregion RecyclerView

    //region BaseFragment
    override fun initObservers() {
        viewModel.loadingMoreChanged.observe(this, Observer { isLoading ->
            changeLoadingMore(isLoading)
        })
        viewModel.reposFetched.observe(this, Observer { repos ->
            mAdapter.addData(repos)
        })
    }

    override fun initView() {
        rvRepo?.let { recyclerView ->
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = mLayoutManager
            recyclerView.addOnScrollListener(mEndlessScroll)
        }

        pvRepo?.onClickTryAgain = {
            fetchInitialData()
        }
    }

    override fun fetchInitialData() {
        viewModel.fetchRepos()
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        changeLoading(isLoading)
    }

    override fun showError(message: Any) {
        if (mAdapter.isEmpty) {
            pvRepo?.icon(R.drawable.ic_alert_triangle)
                ?.text(message)
                ?.show()
        } else {
            showMessage(message)
        }
    }
    //endregion BaseFragment

    //region Local
    private fun changeLoading(isLoading: Boolean) {
        pbRepo?.isVisible = isLoading
    }

    private fun changeLoadingMore(isLoading: Boolean) {
        if (isLoading)
            mAdapter.showLoading(true)
        else
            mAdapter.hideLoading()
    }
    //endregion Local

}