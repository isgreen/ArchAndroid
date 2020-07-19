package br.com.isgreen.archandroid.screen.repo.datasource

import androidx.paging.PageKeyedDataSource
import br.com.isgreen.archandroid.data.model.repository.Repo
import br.com.isgreen.archandroid.screen.repo.RepoContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Éverdes Soares on 07/18/2020.
 */

class RepoDataSource(
    private val repository: RepoContract.Repository,
    private val sort: String?,
    private val role: String?,
    private val after: String?,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, Repo>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Repo>
    ) {
        fetchRepos(sort, role, after) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
        val page = params.key
        fetchRepos(sort, role, after) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
        val page = params.key
        fetchRepos(sort, role, after) {
            callback.onResult(it, page + 1)
        }
    }

    private fun fetchRepos(
        sort: String?,
        role: String?,
        after: String?,
        onResult: (List<Repo>) -> Unit
    ) {
        scope.launch {
            val response = repository.fetchRepos(sort, role, after)
            onResult.invoke(response.repos)
        }
    }
}