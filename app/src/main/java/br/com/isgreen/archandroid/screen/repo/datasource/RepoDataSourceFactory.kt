package br.com.isgreen.archandroid.screen.repo.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import br.com.isgreen.archandroid.data.model.repository.Repo
import br.com.isgreen.archandroid.screen.repo.RepoContract
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Éverdes Soares on 07/19/2020.
 */

class RepoDataSourceFactory(
    private val repository: RepoContract.Repository,
    private val sort: String?,
    private val role: String?,
    private val after: String?,
    private val scope: CoroutineScope
): DataSource.Factory<String, Repo>() {

    val reposDataSource = MutableLiveData<RepoDataSource>()

    override fun create(): DataSource<String, Repo> {
        val dataSource = RepoDataSource(repository, sort, role, after, scope)
        reposDataSource.postValue(dataSource)
        return dataSource
    }
}