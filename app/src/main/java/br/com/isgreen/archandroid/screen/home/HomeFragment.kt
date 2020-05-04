package br.com.isgreen.archandroid.screen.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.extension.setupWithNavController
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.module.Module

/**
 * Created by Éverdes Soares on 03/31/2020.
 */

class HomeFragment : BaseFragment() {

    override val module: Module? = null
    override val screenLayout = R.layout.fragment_home
    override val viewModel: BaseViewModel? = null

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }
    //endregion Fragment

    //region BaseFragment
    override fun initObservers() {}

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
    }

    override fun fetchInitialData() {}

    override fun onLoadingChanged(isLoading: Boolean) {}

    override fun showError(message: Any) {}

    override fun onEventReceived(code: Int, data: Any?) {
        if (code == 456 && data != null) {
            val themeMode = data as Int
            changeTheme(themeMode)
        }
    }
    //endregion BaseFragment

    //region Local
    private fun changeTheme(themeMode: Int) {
        lifecycleScope.launch {
            delay(400)
            AppCompatDelegate.setDefaultNightMode(themeMode)
        }
    }
    //endregion Local
}