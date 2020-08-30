package br.com.isgreen.archandroid.screen.pullrequest.merge

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 08/30/2020.
 */

val pullRequestMergeModule = module {
    viewModel { PullRequestMergeViewModel(get(), get()) }
    factory<PullRequestMergeContract.Repository> { PullRequestMergeRepository(get()) }
}