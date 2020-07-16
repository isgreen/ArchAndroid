package br.com.isgreen.archandroid.screen.splash

import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.extension.loadImageResource
import kotlinx.android.synthetic.main.fragment_splash.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/22/2019.
 */

class SplashFragment : BaseFragment() {

    override val module = splashModule
    override val viewModel: SplashViewModel by viewModel()
    override val screenLayout = R.layout.fragment_splash

    //region BaseFragment
    override fun initObservers() {
        viewModel.isAuthenticated.observe(this, Observer {
            showHome()
        })
        viewModel.isNotAuthenticated.observe(this, Observer {
            showLogin()
        })
        viewModel.themeFetched.observe(this, Observer { theme ->
            AppCompatDelegate.setDefaultNightMode(theme)
            viewModel.checkIsAuthenticated()
        })
    }

    override fun initView() {
        changeStatusBarColor(Color.WHITE)
        imgLogo?.loadImageResource(R.drawable.logo_jetpack)
    }

    override fun fetchInitialData() {
        viewModel.fetchCurrentTheme()
//        viewModel.checkIsAuthenticated()
    }

    override fun onLoadingChanged(isLoading: Boolean) {}

    override fun showError(message: String) {}
    //endregion BaseFragment

    //region Local
    private fun showLogin() {
        val direction = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        navigate(directions = direction, clearBackStack =  true, animation = TransitionAnimation.FADE)
    }

    private fun showHome() {
        val direction = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        navigate(directions = direction, clearBackStack = true)
    }
    //region Local
}