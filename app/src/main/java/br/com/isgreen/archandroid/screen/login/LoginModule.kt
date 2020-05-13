package br.com.isgreen.archandroid.screen.login

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 08/23/2019.
 */

val loginModule = module {
    viewModel { LoginViewModel(get(), get()) }
    factory<LoginContract.Repository> { LoginRepository(get(), get()) }
}