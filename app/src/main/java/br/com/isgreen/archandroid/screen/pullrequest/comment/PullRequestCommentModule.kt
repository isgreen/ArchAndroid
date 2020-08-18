package br.com.isgreen.archandroid.screen.pullrequest.comment

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 08/17/2020.
 */

val pullRequestCommentModule = module {
    viewModel { PullRequestCommentViewModel(get(), get()) }
    factory<PullRequestCommentContract.Repository> { PullRequestCommentRepository(get()) }
}