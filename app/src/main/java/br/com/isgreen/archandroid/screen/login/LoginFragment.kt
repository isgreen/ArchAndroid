package br.com.isgreen.archandroid.screen.login

import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.extension.hideKeyboard
import br.com.isgreen.archandroid.extension.loadImageResource
import br.com.isgreen.archandroid.extension.navigate
import br.com.isgreen.archandroid.extension.showToast
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login_content.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/22/2019.
 */

class LoginFragment : BaseFragment(), LoginContract.View {

    override val module = loginModule
    override val screenLayout = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModel()

    //region BaseFragment
    override fun initObservers() {
        viewModel.loginAuthorized.observe(this, {
            showEvent()
        })
    }

    override fun initView() {
        imgLogo?.loadImageResource(R.drawable.logo_jetpack)
        btnLogin?.setOnClickListener { doLogin() }
        edtPasswordLogin?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                doLogin()
            }

            true
        }

        KeyboardVisibilityEvent.setEventListener(activity) { isOpen ->
            layoutRoot?.setTransition(R.id.transitionEdit)
            if (isOpen) {
                layoutRoot?.transitionToEnd()
            } else {
                layoutRoot?.transitionToStart()
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
            edtEmailLogin?.text?.toString(),
            edtPasswordLogin?.text?.toString()
        )
    }

    private fun changeLoading(isLoading: Boolean) {
        btnLogin?.isEnabled = !isLoading
        pbRepo?.isVisible = isLoading
        edtEmailLogin?.isEnabled = !isLoading
        edtPasswordLogin?.isEnabled = !isLoading
    }

    private fun showEvent() {
        val direction = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        navigate(direction, clearBackStack = true)
    }
    //endregion Local

}