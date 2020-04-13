package br.com.isgreen.archandroid.screen.repo

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 09/22/2019.
 */

val repoModule = module {
    viewModel { RepoViewModel(get()) }
    factory<RepoContract.Repository> { RepoRepository(get()) }
}