package br.com.isgreen.archandroid.screen.user

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 07/22/2020.
 */

val userModule = module {
    viewModel { UserViewModel(get(), get()) }
    factory<UserContract.Repository> { UserRepository(get()) }
}