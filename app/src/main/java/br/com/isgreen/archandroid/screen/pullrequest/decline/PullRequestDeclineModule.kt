package br.com.isgreen.archandroid.screen.pullrequest.decline

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 12/19/2020.
 */

val pullRequestDeclineModule = module {
    viewModel { PullRequestDeclineViewModel(get(), get()) }
    factory<PullRequestDeclineContract.Repository> { PullRequestDeclineRepository(get()) }
}