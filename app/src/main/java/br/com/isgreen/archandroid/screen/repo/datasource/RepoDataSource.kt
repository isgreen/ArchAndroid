package br.com.isgreen.archandroid.screen.repo.datasource

import androidx.paging.ItemKeyedDataSource
import androidx.paging.PageKeyedDataSource
import br.com.isgreen.archandroid.data.model.repository.Repo
import br.com.isgreen.archandroid.screen.repo.RepoContract
import br.com.isgreen.archandroid.util.DateUtil
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
) : ItemKeyedDataSource<String, Repo>() {

//    override fun loadInitial(
//        params: LoadInitialParams<Int>,
//        callback: LoadInitialCallback<Int, Repo>
//    ) {
//        fetchRepos(sort, role, after) {
//            callback.onResult(it, null, 2)
//        }
//    }
//
//    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
//        val page = params.key
//        fetchRepos(sort, role, after) {
//            callback.onResult(it, page + 1)
//        }
//    }
//
//    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
//        val page = params.key
//        fetchRepos(sort, role, after) {
//            callback.onResult(it, page + 1)
//        }
//    }
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<Repo>) {
        fetchRepos(sort, role, params.requestedInitialKey) { repos ->
            callback.onResult(repos)
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<Repo>) {
        fetchRepos(sort, role, params.key) { repos ->
            callback.onResult(repos)
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<Repo>) {}

    override fun getKey(item: Repo) = DateUtil.increaseTime(item.createdOn, 1) ?: ""

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