package br.com.isgreen.archandroid.login

import androidx.lifecycle.Observer
import br.com.isgreen.archandroid.base.BaseValidatorHelper
import br.com.isgreen.archandroid.base.BaseViewModelTest
import br.com.isgreen.archandroid.data.model.login.Authorization
import br.com.isgreen.archandroid.screen.login.LoginContract
import br.com.isgreen.archandroid.screen.login.LoginViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

/**
 * Created by Éverdes Soares on 04/27/2021.
 */

@ExperimentalCoroutinesApi
class LoginViewModelTest : BaseViewModelTest<LoginContract.ViewModel>() {

    companion object {
        const val GRANT_TYPE_PASSWORD = "password"
        const val USERNAME = "user"
        const val PASSWORD = "123"
    }

    override lateinit var viewModel: LoginContract.ViewModel

    @MockK(relaxUnitFun = true)
    private lateinit var repository: LoginContract.Repository

    @MockK(relaxed = true)
    private lateinit var loginAuthorizedObserver: Observer<Unit>

    @Before
    override fun before() {
        super.before()
        MockKAnnotations.init(this)

        val validatorHelper = mockk<BaseValidatorHelper>(relaxed = true)
        viewModel = LoginViewModel(exceptionHelper, repository, validatorHelper)
    }

    @Test
    fun login_success() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.loginAuthorized.observeForever(loginAuthorizedObserver)

        val authorization = mockk<Authorization>(relaxed = true)
        coEvery { repository.doLogin(GRANT_TYPE_PASSWORD, USERNAME, PASSWORD) } returns authorization
        coEvery { repository.saveAuthorization(authorization) } returns Unit

        viewModel.doLogin(USERNAME, PASSWORD)

        runBlockingTest {
            coVerify { loadingObserver.onChanged(true) }

            coVerify { repository.doLogin(GRANT_TYPE_PASSWORD, USERNAME, PASSWORD) }
            coVerify { repository.saveAuthorization(authorization) }

            coVerify { loginAuthorizedObserver.onChanged(Unit) }
            coVerify { loadingObserver.onChanged(false) }
        }
    }

    @Test
    fun login_username_isNull() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.message.observeForever(messageObserver)

        val message = ""

        viewModel.doLogin(null, PASSWORD)

        runBlockingTest {
            coVerify { loadingObserver.onChanged(true) }
            coVerify { messageObserver.onChanged(message) }
            coVerify { loadingObserver.onChanged(false) }
        }
    }

    @Test
    fun login_password_isNull() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.message.observeForever(messageObserver)

        val message = ""

        viewModel.doLogin(USERNAME, null)

        runBlockingTest {
            coVerify { loadingObserver.onChanged(true) }
            coVerify { messageObserver.onChanged(message) }
            coVerify { loadingObserver.onChanged(false) }
        }
    }

    @Test
    fun login_usernameAndPassword_isNull() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.message.observeForever(messageObserver)

        val message = ""

        viewModel.doLogin(USERNAME, null)

        runBlockingTest {
            coVerify { loadingObserver.onChanged(true) }
            coVerify { messageObserver.onChanged(message) }
            coVerify { loadingObserver.onChanged(false) }
        }
    }

    @Test
    fun login_fail() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.message.observeForever(messageObserver)

        val message = ""

        val exception = mockk<Exception>()
        coEvery { repository.doLogin(GRANT_TYPE_PASSWORD, USERNAME, PASSWORD) } throws exception

        viewModel.doLogin(USERNAME, PASSWORD)

        runBlockingTest {
            coVerify { loadingObserver.onChanged(true) }
            coVerify { messageObserver.onChanged(message) }
            coVerify { loadingObserver.onChanged(false) }
        }
    }
}