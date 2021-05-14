package br.com.isgreen.archandroid.repo

import androidx.lifecycle.Observer
import br.com.isgreen.archandroid.base.BaseViewModelTest
import br.com.isgreen.archandroid.data.model.repository.FetchReposResponse
import br.com.isgreen.archandroid.data.model.repository.Repo
import br.com.isgreen.archandroid.screen.repo.RepoContract
import br.com.isgreen.archandroid.screen.repo.RepoViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Created by Éverdes Soares on 05/11/2021.
 */

@ExperimentalCoroutinesApi
class RepoViewModelTest : BaseViewModelTest<RepoContract.ViewModel>() {

    override lateinit var viewModel: RepoContract.ViewModel

    @MockK(relaxUnitFun = true)
    private lateinit var repository: RepoContract.Repository

    @MockK(relaxed = true)
    private lateinit var loadingMoreChanged: Observer<Boolean>

    @MockK(relaxed = true)
    private lateinit var reposClearedObserver: Observer<Unit>

    @MockK(relaxed = true)
    private lateinit var reposNotFoundObserver: Observer<Unit>

    @MockK(relaxed = true)
    private lateinit var reposFetchedObserver: Observer<List<Repo>>

    @Before
    override fun before() {
        super.before()

        MockKAnnotations.init(this)
        viewModel = RepoViewModel(exceptionHelper, repository)
    }

    @Test
    fun fetchRepos_isInitialRequest_reposFetched_success() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.reposCleared.observeForever(reposClearedObserver)
        viewModel.reposFetched.observeForever(reposFetchedObserver)

        val isInitialRequest = true
        val nextRequestUrl = null

        val repo = mockk<Repo>()
        val response = FetchReposResponse(null, 1, listOf(repo))
        coEvery { repository.fetchRepos(nextRequestUrl, null, RepoViewModel.ROLE_MEMBER) } returns response

        viewModel.fetchRepos(isInitialRequest)

        runBlockingTest {
            assertEquals(true, isInitialRequest)
            assertNull(nextRequestUrl)

            coVerify { reposClearedObserver.onChanged(Unit) }
            coVerify { loadingObserver.onChanged(true) }
            coVerify { repository.fetchRepos(nextRequestUrl, null, RepoViewModel.ROLE_MEMBER) }
            coVerify { reposFetchedObserver.onChanged(response.repos) }
            coVerify { loadingObserver.onChanged(false) }

            assertNull(nextRequestUrl)
        }
    }

    @Test
    fun fetchRepos_isInitialRequest_reposNotFound_success() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.reposCleared.observeForever(reposClearedObserver)
        viewModel.reposNotFound.observeForever(reposNotFoundObserver)

        val isInitialRequest = true
        val nextRequestUrl = null

        val response = mockk<FetchReposResponse>(relaxed = true)
        coEvery { repository.fetchRepos(nextRequestUrl, null, RepoViewModel.ROLE_MEMBER) } returns response

        viewModel.fetchRepos(isInitialRequest)

        runBlockingTest {
            assertEquals(true, isInitialRequest)
            assertNull(nextRequestUrl)

            coVerify { reposClearedObserver.onChanged(Unit) }
            coVerify { loadingObserver.onChanged(true) }
            coVerify { repository.fetchRepos(nextRequestUrl, null, RepoViewModel.ROLE_MEMBER) }
            coVerify { reposNotFoundObserver.onChanged(Unit) }
            coVerify { loadingObserver.onChanged(false) }

            assertNull(nextRequestUrl)
        }
    }

    @Test
    fun fetchRepos_isInitialRequest_fail() {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.message.observeForever(messageObserver)
        viewModel.reposCleared.observeForever(reposClearedObserver)

        val message = ""
        val isInitialRequest = true
        val nextRequestUrl = null

        val exception = mockk<Exception>()
        coEvery { repository.fetchRepos(nextRequestUrl, null, RepoViewModel.ROLE_MEMBER) } throws exception

        viewModel.fetchRepos(isInitialRequest)

        runBlockingTest {
            assertEquals(true, isInitialRequest)
            assertNull(nextRequestUrl)

            coVerify { reposClearedObserver.onChanged(Unit) }
            coVerify { loadingObserver.onChanged(true) }
            coVerify { repository.fetchRepos(nextRequestUrl, null, RepoViewModel.ROLE_MEMBER) }
            coVerify { loadingObserver.onChanged(false) }
            coVerify { messageObserver.onChanged(message) }

            assertNull(nextRequestUrl)
        }
    }
}