package br.com.isgreen.archandroid.screen.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.databinding.FragmentProfileBinding
import br.com.isgreen.archandroid.extension.appCompatActivity
import br.com.isgreen.archandroid.extension.loadImageRounded
import br.com.isgreen.archandroid.extension.navigate
import br.com.isgreen.archandroid.extension.showToast
import com.google.android.material.transition.platform.Hold
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 07/22/2020.
 */

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val module = profileModule
    override val screenLayout = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentProfileBinding
        get() = FragmentProfileBinding::inflate

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
        binding.includeToolbar.toolbar.Builder(appCompatActivity)
            .titleIcon(R.drawable.ic_android)
            .displayHome(false)
            .title(R.string.app_name)
            .build()

        binding.txtLogout.setOnClickListener { logout() }
        binding.txtTheme.setOnClickListener { showTheme() }
    }

    override fun initObservers() {
        viewModel.userFetched.observe(this, { user ->
            setDataInView(user)
        })
    }

    override fun fetchInitialData() {
        viewModel.fetchUser()
    }

    override fun showError(message: String) {
        if (binding.nsvUser.isVisible)
            showToast(message)
        else
            binding.pvUser.text(message).show()
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        binding.pbUser.isVisible = isLoading
    }
    //endregion BaseFragment

    //region Local
    private fun setDataInView(user: User) {
        binding.nsvUser.isVisible = true
        binding.txtNickname.text = user.nickname
        binding.txtDisplayName.text = user.displayName
        binding.imgUser.loadImageRounded(user.links?.avatar?.href, R.drawable.ic_user, R.dimen.margin_default)
    }

    private fun showTheme() {
        val direction = ProfileFragmentDirections.actionProfileFragmentToThemeFragment()
        navigate(directions = direction, sharedElements = binding.txtTheme to getString(R.string.shared_element_theme))
        hideNavigationBottom()
    }

    private fun logout() {
        viewModel.logout()
    }
    //endregion Local
}