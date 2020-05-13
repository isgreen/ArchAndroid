package br.com.isgreen.archandroid.screen.splash

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 09/19/2019.
 */

val splashModule = module {
    viewModel { SplashViewModel(get(), get()) }
    factory<SplashContract.Repository> { SplashRepository(get()) }
}