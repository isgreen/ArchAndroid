package br.com.isgreen.archandroid.splash

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import br.com.isgreen.archandroid.base.BaseViewModelTest
import br.com.isgreen.archandroid.data.model.login.Authorization
import br.com.isgreen.archandroid.screen.splash.SplashContract
import br.com.isgreen.archandroid.screen.splash.SplashViewModel
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
 * Created by Éverdes Soares on 04/30/2021.
 */

@ExperimentalCoroutinesApi
class SplashViewModelTest : BaseViewModelTest<SplashContract.ViewModel>() {

    override lateinit var viewModel: SplashContract.ViewModel

    @MockK(relaxUnitFun = true)
    private lateinit var repository: SplashContract.Repository

    @MockK(relaxed = true)
    private lateinit var themeFetchedObserver: Observer<Int>

    @MockK(relaxed = true)
    private lateinit var isAuthenticatedObserver: Observer<Unit>

    @MockK(relaxed = true)
    private lateinit var isNotAuthenticatedObserver: Observer<Unit>

    @Before
    override fun before() {
        super.before()

        MockKAnnotations.init(this)
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

    @Test
    fun checkIsAuthenticated_fail() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.message.observeForever(messageObserver)
        viewModel.isNotAuthenticated.observeForever(isNotAuthenticatedObserver)

        val authorization = null
        coEvery { repository.fetchAuthorization() } returns authorization

        viewModel.checkIsAuthenticated()

        runBlockingTest {
            coVerify { loadingObserver.onChanged(true) }
            coVerify { repository.fetchAuthorization() }
            coVerify { isNotAuthenticatedObserver.onChanged(Unit) }
            coVerify { loadingObserver.onChanged(false) }
        }
    }

    @Test
    fun fetchCurrentTheme_themeIsZero() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.message.observeForever(messageObserver)
        viewModel.themeFetched.observeForever(themeFetchedObserver)

        val theme = 0
        coEvery { repository.fetchCurrentTheme() } returns theme

        viewModel.fetchCurrentTheme()

        runBlockingTest {
            coVerify { loadingObserver.onChanged(true) }
            coVerify { repository.fetchCurrentTheme() }
            coVerify { themeFetchedObserver.onChanged(AppCompatDelegate.MODE_NIGHT_NO) }
            coVerify { loadingObserver.onChanged(false) }
        }
    }

    @Test
    fun fetchCurrentTheme_themeIsNotZero() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.message.observeForever(messageObserver)
        viewModel.themeFetched.observeForever(themeFetchedObserver)

        val theme = AppCompatDelegate.MODE_NIGHT_NO
        coEvery { repository.fetchCurrentTheme() } returns theme

        viewModel.fetchCurrentTheme()

        runBlockingTest {
            coVerify { loadingObserver.onChanged(true) }
            coVerify { repository.fetchCurrentTheme() }
            coVerify { themeFetchedObserver.onChanged(theme) }
            coVerify { loadingObserver.onChanged(false) }
        }
    }
}