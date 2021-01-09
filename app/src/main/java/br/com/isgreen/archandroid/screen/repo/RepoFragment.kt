package br.com.isgreen.archandroid.screen.repo

import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.databinding.FragmentRepoBinding
import br.com.isgreen.archandroid.extension.appCompatActivity
import br.com.isgreen.archandroid.extension.showToast
import br.com.isgreen.archandroid.util.listener.OnRecyclerViewScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 09/22/2019.
 */

class RepoFragment : BaseFragment<FragmentRepoBinding>() {

    override val module = repoModule
    override val screenLayout = R.layout.fragment_repo
    override val viewModel: RepoViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentRepoBinding
        get() = FragmentRepoBinding::inflate

    //region RecyclerView
    private val mAdapter: RepoAdapter by lazy { RepoAdapter() }
    private val mLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(context) }
    private val onRecyclerScrollListener = object : OnRecyclerViewScrollListener(mLayoutManager, DIRECTION_END) {
        override fun loadMore(page: Int) {
            viewModel.fetchRepos()
        }
    }
    //endregion RecyclerView

    //region BaseFragment
    override fun initObservers() {
        viewModel.loadingMoreChanged.observe(this, { isLoading ->
            changeLoadingMore(isLoading)
        })
        viewModel.reposCleared.observe(this, {
            mAdapter.clearData()
        })
        viewModel.reposNotFound.observe(this, {
            showPlaceholderMessage(getString(R.string.no_data))
        })
        viewModel.reposFetched.observe(this, { repos ->
            mAdapter.addData(repos)
        })
    }

    override fun initView() {
        binding.includeToolbar.toolbar.Builder(appCompatActivity)
            .titleIcon(R.drawable.ic_android)
            .displayHome(false)
            .title(R.string.app_name)
            .build()

        binding.rvRepo.let { recyclerView ->
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = mLayoutManager
            recyclerView.addOnScrollListener(onRecyclerScrollListener)
        }

        binding.pvRepo.onClickTryAgain = {
            fetchInitialData()
        }
    }

    override fun fetchInitialData() {
        viewModel.fetchRepos(isInitialRequest = true)
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
        binding.pbRepo.isVisible = isLoading
    }

    private fun changeLoadingMore(isLoading: Boolean) {
        onRecyclerScrollListener.isLoading = isLoading
        if (isLoading && !mAdapter.isLoading()) {
            mAdapter.showLoading(true)
            binding.rvRepo.smoothScrollToPosition(mAdapter.lastIndex)
        } else {
            mAdapter.hideLoading()
        }
    }

    private fun showPlaceholderMessage(message: String) {
        binding.pvRepo.icon(R.drawable.ic_alert_triangle)
            .text(message)
            .show()
    }
    //endregion Local
}