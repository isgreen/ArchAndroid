package br.com.isgreen.archandroid.screen.pullrequest.merge

import br.com.isgreen.archandroid.helper.resource.ResourceHelper
import br.com.isgreen.archandroid.helper.resource.ResourceHelperImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 08/30/2020.
 */

val pullRequestMergeModule = module {
    viewModel { PullRequestMergeViewModel(get(), get()) }
    factory<ResourceHelper> { ResourceHelperImpl(get()) }
    factory<PullRequestMergeContract.Repository> { PullRequestMergeRepository(get(), get()) }
}