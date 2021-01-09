package br.com.isgreen.archandroid.screen.splash

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.databinding.FragmentSplashBinding
import br.com.isgreen.archandroid.extension.TransitionAnimation
import br.com.isgreen.archandroid.extension.loadImageResource
import br.com.isgreen.archandroid.extension.navigate
import kotlinx.android.synthetic.main.fragment_splash.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/22/2019.
 */

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override val module = splashModule
    override val viewModel: SplashViewModel by viewModel()
    override val screenLayout = R.layout.fragment_splash
    override val bindingInflater: (LayoutInflater) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    //region BaseFragment
    override fun initObservers() {
        viewModel.isAuthenticated.observe(this, {
            showHome()
        })
        viewModel.isNotAuthenticated.observe(this, {
            showLogin()
        })
        viewModel.themeFetched.observe(this, { theme ->
            AppCompatDelegate.setDefaultNightMode(theme)
            viewModel.checkIsAuthenticated()
        })
    }

    override fun initView() {
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
        navigate(
            directions = direction,
            clearBackStack = true,
            animation = TransitionAnimation.FADE
        )
    }

    private fun showHome() {
        val direction = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        navigate(directions = direction, clearBackStack = true)
    }
    //region Local
}