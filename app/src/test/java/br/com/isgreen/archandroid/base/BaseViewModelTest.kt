package br.com.isgreen.archandroid.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import br.com.isgreen.archandroid.screen.splash.SplashViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

/**
 * Created by Éverdes Soares on 04/30/2021.
 */

@ExperimentalCoroutinesApi
abstract class BaseViewModelTest<VM : BaseContract.ViewModel> {

    abstract var viewModel: VM

    @MockK(relaxed = true)
    protected lateinit var loadingObserver: Observer<Boolean>

    @MockK(relaxed = true)
    protected lateinit var messageObserver: Observer<String>

    @MockK(relaxed = true)
    protected lateinit var exceptionHelper: ExceptionHandlerHelper

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    open fun before() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}