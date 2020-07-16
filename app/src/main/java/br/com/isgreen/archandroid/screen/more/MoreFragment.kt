package br.com.isgreen.archandroid.screen.more

import android.os.Bundle
import android.view.View
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.extension.appCompatActivity
import com.google.android.material.transition.platform.Hold
import kotlinx.android.synthetic.main.appbar_and_toolbar.*
import kotlinx.android.synthetic.main.fragment_more.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 04/12/2020.
 */

class MoreFragment : BaseFragment() {

    override val module = moreModule
    override val screenLayout = R.layout.fragment_more
    override val viewModel: MoreViewModel by viewModel()

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
    override fun initView() {
        toolbar?.Builder(appCompatActivity)
            ?.titleIcon(R.drawable.ic_android)
            ?.displayHome(false)
            ?.title(R.string.app_name)
            ?.build()

        txtTheme?.setOnClickListener { showTheme() }
        txtLogout?.setOnClickListener { logout() }
        txtProfile?.setOnClickListener {  }
    }

    override fun initObservers() {}

    override fun fetchInitialData() {}

    override fun showError(message: String) {}

    override fun onLoadingChanged(isLoading: Boolean) {}
    //endregion BaseFragment

    //region Local
    private fun showTheme() {
        val direction = MoreFragmentDirections.actionMoreFragmentToThemeFragment()
        navigate(directions = direction, sharedElements =  txtTheme to getString(R.string.shared_element_theme))
        hideNavigationBottom()
    }

    private fun logout() {
        viewModel.logout()
    }
    //endregion Local

}