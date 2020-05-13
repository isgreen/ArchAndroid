package br.com.isgreen.archandroid.screen.home

import android.os.Bundle
import androidx.core.view.isVisible
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.extension.baseActivity
import br.com.isgreen.archandroid.extension.setupWithNavController
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.core.module.Module

/**
 * Created by Éverdes Soares on 03/31/2020.
 */

class HomeFragment : BaseFragment() {

    override val module: Module? = null
    override val viewModel: BaseViewModel? = null
    override val screenLayout = R.layout.fragment_home

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }
    //endregion Fragment

    //region BaseFragment
    override fun initView() {
        val navGraphs = listOf(
            R.navigation.recent_nav_graph,
            R.navigation.repo_nav_graph,
            R.navigation.more_nav_graph
        )
        navBottom?.setupWithNavController(
            navGraphIds = navGraphs,
            fragmentManager = childFragmentManager,
            containerId = R.id.navHostContainer
        )
        baseActivity?.changeNavigationVisibilityListener = { isVisible ->
            navBottom?.isVisible = isVisible
            vwDividerBottom?.isVisible = isVisible
        }
    }

    override fun initObservers() {}

    override fun fetchInitialData() {}

    override fun onLoadingChanged(isLoading: Boolean) {}

    override fun showError(message: Any) {}
    //endregion BaseFragment
}