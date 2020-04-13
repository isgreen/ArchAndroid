package br.com.isgreen.archandroid.screen.more

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 04/12/2020.
 */

val moreModule = module {
    viewModel { MoreViewModel(get()) }
    factory<MoreContract.Repository> { MoreRepository(get()) }
}