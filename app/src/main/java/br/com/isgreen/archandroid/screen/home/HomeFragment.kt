package br.com.isgreen.archandroid.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.databinding.FragmentHomeBinding
import br.com.isgreen.archandroid.extension.baseActivity
import br.com.isgreen.archandroid.extension.setupWithNavController
import org.koin.core.module.Module

/**
 * Created by Éverdes Soares on 03/31/2020.
 */

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val module: Module? = null
    override val viewModel: BaseViewModel? = null
    override val screenLayout = R.layout.fragment_home
    override val bindingInflater: (LayoutInflater) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }
    //endregion Fragment

    //region BaseFragment
    override fun initView() {
        val navGraphs = listOf(
            R.navigation.repo_nav_graph,
            R.navigation.pull_request_nav_graph,
            R.navigation.profile_nav_graph
        )
        binding.navBottom.setupWithNavController(
            navGraphIds = navGraphs,
            fragmentManager = childFragmentManager,
            containerId = R.id.navHostContainer
        )
        baseActivity?.changeNavigationVisibilityListener = { isVisible ->
            binding.navBottom.isVisible = isVisible
            binding.vwDividerBottom.isVisible = isVisible
        }
    }

    override fun initObservers() {}

    override fun fetchInitialData() {}

    override fun onLoadingChanged(isLoading: Boolean) {}

    override fun showError(message: String) {}
    //endregion BaseFragment
}