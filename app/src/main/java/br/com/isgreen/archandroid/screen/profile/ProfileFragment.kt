package br.com.isgreen.archandroid.screen.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.extension.appCompatActivity
import br.com.isgreen.archandroid.extension.loadImageRounded
import br.com.isgreen.archandroid.extension.showToast
import com.google.android.material.transition.platform.Hold
import kotlinx.android.synthetic.main.appbar_and_toolbar.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 07/22/2020.
 */

class ProfileFragment : BaseFragment() {

    override val module = profileModule
    override val screenLayout = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by viewModel()

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
//        sharedElementEnterTransition = MaterialContainerTransform()
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

        txtLogout?.setOnClickListener { logout() }
        txtTheme?.setOnClickListener { showTheme() }
    }

    override fun initObservers() {
        viewModel.userFetched.observe(this, Observer { user ->
            setDataInView(user)
        })
    }

    override fun fetchInitialData() {
        viewModel.fetchUser()
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

    private fun showTheme() {
        val direction = ProfileFragmentDirections.actionProfileFragmentToThemeFragment()
        navigate(directions = direction, sharedElements = txtTheme to getString(R.string.shared_element_theme))
        hideNavigationBottom()
    }

    private fun logout() {
        viewModel.logout()
    }
    //endregion Local
}