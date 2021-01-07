package br.com.isgreen.archandroid.screen.pullrequest.comment.adder

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 01/06/2021.
 */

val pullRequestCommentAdderModule = module {
    viewModel { PullRequestCommentAdderViewModel(get(), get()) }
    factory<PullRequestCommentAdderContract.Repository> { PullRequestCommentAdderRepository(get()) }
}