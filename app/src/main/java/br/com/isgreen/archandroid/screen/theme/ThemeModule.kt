package br.com.isgreen.archandroid.screen.theme

import br.com.isgreen.archandroid.helper.resource.ResourceHelper
import br.com.isgreen.archandroid.helper.resource.ResourceHelperImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 01/14/2020.
 */

val themeModule = module {
    viewModel { ThemeViewModel(get(), get()) }
    factory<ResourceHelper> { ResourceHelperImpl(get()) }
    factory<ThemeContract.Repository> { ThemeRepository(get(), get()) }
}