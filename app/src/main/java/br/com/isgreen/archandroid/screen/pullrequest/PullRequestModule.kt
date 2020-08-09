package br.com.isgreen.archandroid.screen.pullrequest

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 08/07/2020.
 */

val pullRequestModule = module {
    viewModel { PullRequestViewModel(get(), get()) }
    factory<PullRequestContract.Repository> { PullRequestRepository(get()) }
}