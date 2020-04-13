package br.com.isgreen.archandroid.screen.login

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.extension.hideKeyboard
import br.com.isgreen.archandroid.extension.loadImageResource
import br.com.isgreen.archandroid.extension.showMessage
import kotlinx.android.synthetic.main.fragment_login.layoutRoot
import kotlinx.android.synthetic.main.fragment_login_content.*
import kotlinx.android.synthetic.main.fragment_login_start.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/22/2019.
 */

class LoginFragment : BaseFragment(), LoginContract.View {

    override val module = loginModule
    override val screenLayout = R.layout.fragment_login_start
    override val viewModel: LoginViewModel by viewModel()

    //region BaseFragment
    override fun initObservers() {
        viewModel.loginAuthorized.observe(this, Observer {
            showEvent()
        })
    }

    override fun initView() {
        imgLogo?.loadImageResource(R.drawable.logo_jetpack)
        btnLogin?.setOnClickListener { doLogin() }

        KeyboardVisibilityEvent.setEventListener(activity) { isOpen ->
            if (isOpen) {
                changeLayout(layoutRoot, R.layout.fragment_login_editing)
            } else {
                changeLayout(layoutRoot, R.layout.fragment_login)
            }
        }

        lifecycleScope.launch {
            delay(1000)
            changeLayout(layoutRoot, R.layout.fragment_login)
        }
    }

    override fun fetchInitialData() {

    }

    override fun showError(message: Any) {
        showMessage(message)
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
        navigate(direction, null, true)
    }
    //endregion Local

}