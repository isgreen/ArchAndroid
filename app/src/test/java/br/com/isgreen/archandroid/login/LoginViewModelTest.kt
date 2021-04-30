package br.com.isgreen.archandroid.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.isgreen.archandroid.base.BaseValidatorHelper
import br.com.isgreen.archandroid.data.model.login.Authorization
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import br.com.isgreen.archandroid.screen.login.LoginContract
import br.com.isgreen.archandroid.screen.login.LoginViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Éverdes Soares on 04/27/2021.
 */

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    companion object {
        const val GRANT_TYPE_PASSWORD = "password"
        const val USERNAME = "user"
        const val PASSWORD = "123"
    }

    private lateinit var viewModel: LoginContract.ViewModel

    @MockK(relaxUnitFun = true)
    private lateinit var repository: LoginContract.Repository

    @MockK(relaxed = true)
    private lateinit var loadingObserver: Observer<Boolean>

    @MockK(relaxed = true)
    private lateinit var messageObserver: Observer<String>

    @MockK(relaxed = true)
    private lateinit var loginAuthorizedObserver: Observer<Boolean>

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun before() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        val validatorHelper = mockk<BaseValidatorHelper>(relaxed = true)
        val exceptionHelper = mockk<ExceptionHandlerHelper>(relaxed = true)
        viewModel = LoginViewModel(exceptionHelper, repository, validatorHelper)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
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

            coVerify { loginAuthorizedObserver.onChanged(true) }
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
}