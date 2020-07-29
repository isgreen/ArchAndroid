package br.com.isgreen.archandroid.screen.user

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.extension.appCompatActivity
import br.com.isgreen.archandroid.extension.loadImageRounded
import br.com.isgreen.archandroid.extension.showToast
import br.com.isgreen.archandroid.screen.repo.RepoAdapter
import br.com.isgreen.archandroid.util.listener.OnRecyclerViewScrollListener
import com.google.android.material.transition.platform.MaterialContainerTransform
import kotlinx.android.synthetic.main.appbar_and_toolbar.*
import kotlinx.android.synthetic.main.fragment_user.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 07/22/2020.
 */

class UserFragment : BaseFragment() {

    override val module = userModule
    override val screenLayout = R.layout.fragment_user
    override val viewModel: UserViewModel by viewModel()

    //region RecyclerView
    private val mAdapter: RepoAdapter by lazy { RepoAdapter() }
    private val mLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(context) }
    private val onRecyclerScrollListener = object : OnRecyclerViewScrollListener(mLayoutManager, DIRECTION_END) {
        override fun loadMore(page: Int) {
//            viewModel.fetchRepos()
        }
    }
    //endregion RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    //region BaseFragment
    override fun initView() {
        toolbar?.Builder(appCompatActivity)
            ?.homeIcon(R.drawable.ic_back)
            ?.title(R.string.profile)
            ?.build()

        rvRepoUser?.let { recyclerView ->
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = mLayoutManager
            recyclerView.addOnScrollListener(onRecyclerScrollListener)
        }

        pvUser?.onClickTryAgain = {
            fetchInitialData()
        }
    }

    override fun initObservers() {
        viewModel.userFetched.observe(this, Observer { user ->
            setDataInView(user)
        })
        viewModel.reposFetched.observe(this, Observer { repos ->
            mAdapter.addData(repos)
        })
    }

    override fun fetchInitialData() {
        viewModel.fetchUserAndRepos()
    }

    override fun showError(message: String) {
        if (nsvUser?.isVisible == true)
            showToast(message)
        else
            pvUser?.text(message)?.show()
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        pbUser?.isVisible = isLoading
    }
    //endregion BaseFragment

    //region Local
    private fun setDataInView(user: User) {
        nsvUser.isVisible = true
        txtNickname?.text = user.nickname
        txtDisplayName?.text = user.displayName
        imgUser?.loadImageRounded(user.links.avatar?.href, R.drawable.ic_user, R.dimen.margin_default)
    }
    //endregion Local
}