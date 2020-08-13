package br.com.isgreen.archandroid.screen.pullrequest.commit

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 08/12/2020.
 */

val pullRequestCommitModule = module {
    viewModel { PullRequestCommitViewModel(get(), get()) }
    factory<PullRequestCommitContract.Repository> { PullRequestCommitRepository(get()) }
}