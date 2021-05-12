package br.com.isgreen.archandroid.repo

import androidx.lifecycle.Observer
import br.com.isgreen.archandroid.base.BaseViewModelTest
import br.com.isgreen.archandroid.screen.repo.RepoContract
import br.com.isgreen.archandroid.screen.repo.RepoViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private lateinit var reposCleared: Observer<Unit>

    @Before
    override fun before() {
        super.before()

        MockKAnnotations.init(this)
        viewModel = RepoViewModel(exceptionHelper, repository)
    }

    @Test
    fun fetchRepos_isInitialRequest_success() {
        val nextRequestUrl = null

    }
}