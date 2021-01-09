package br.com.isgreen.archandroid.screen.login

import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.databinding.FragmentLoginBinding
import br.com.isgreen.archandroid.extension.hideKeyboard
import br.com.isgreen.archandroid.extension.loadImageResource
import br.com.isgreen.archandroid.extension.navigate
import br.com.isgreen.archandroid.extension.showToast
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/22/2019.
 */

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val module = loginModule
    override val screenLayout = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    //region BaseFragment
    override fun initObservers() {
        viewModel.loginAuthorized.observe(this, {
            showEvent()
        })
    }

    override fun initView() {
        binding.imgLogo.loadImageResource(R.drawable.logo_jetpack)
        binding.includeCard.btnLogin.setOnClickListener { doLogin() }
        binding.includeCard.edtPasswordLogin.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                doLogin()
            }

            true
        }

        KeyboardVisibilityEvent.setEventListener(activity) { isOpen ->
            binding.layoutRoot.setTransition(R.id.transitionEdit)
            if (isOpen) {
                binding.layoutRoot.transitionToEnd()
            } else {
                binding.layoutRoot.transitionToStart()
            }
        }
    }

    override fun fetchInitialData() {}

    override fun showError(message: String) {
        showToast(message)
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        changeLoading(isLoading)
    }
    //endregion BaseFragment

    //region Local
    private fun doLogin() {
        hideKeyboard()
        viewModel.doLogin(
            binding.includeCard.edtEmailLogin.text?.toString(),
            binding.includeCard.edtPasswordLogin.text?.toString()
        )
    }

    private fun changeLoading(isLoading: Boolean) {
        binding.includeCard.pbRepo.isVisible = isLoading
        binding.includeCard.btnLogin.isEnabled = !isLoading
        binding.includeCard.edtEmailLogin.isEnabled = !isLoading
        binding.includeCard.edtPasswordLogin.isEnabled = !isLoading
    }

    private fun showEvent() {
        val direction = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        navigate(direction, clearBackStack = true)
    }
    //endregion Local

}