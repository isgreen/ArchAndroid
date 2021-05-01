package br.com.isgreen.archandroid.splash

import androidx.lifecycle.Observer
import br.com.isgreen.archandroid.base.BaseViewModelTest
import br.com.isgreen.archandroid.data.model.login.Authorization
import br.com.isgreen.archandroid.screen.splash.SplashContract
import br.com.isgreen.archandroid.screen.splash.SplashViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

/**
 * Created by Éverdes Soares on 04/30/2021.
 */

@ExperimentalCoroutinesApi
class SplashViewModelTest : BaseViewModelTest<SplashContract.ViewModel>() {

    override lateinit var viewModel: SplashContract.ViewModel

    @MockK(relaxUnitFun = true)
    private lateinit var repository: SplashContract.Repository

    @MockK(relaxed = true)
    private lateinit var themeFetched: Observer<Unit>

    @MockK(relaxed = true)
    private lateinit var isAuthenticatedObserver: Observer<Unit>

    @MockK(relaxed = true)
    private lateinit var isNotAuthenticated: Observer<Unit>

    @Before
    override fun before() {
        super.before()

        viewModel = SplashViewModel(exceptionHelper, repository)
    }

    @Test
    fun checkIsAuthenticated_success() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.message.observeForever(messageObserver)
        viewModel.isAuthenticated.observeForever(isAuthenticatedObserver)

        val authorization = mockk<Authorization>(relaxed = true)
        coEvery { repository.fetchAuthorization() } returns authorization

        viewModel.checkIsAuthenticated()

        runBlockingTest {
            coVerify { loadingObserver.onChanged(true) }
            coVerify { repository.fetchAuthorization() }
            coVerify { isAuthenticatedObserver.onChanged(Unit) }
            coVerify { loadingObserver.onChanged(false) }
        }
    }
}