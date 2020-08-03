package br.com.isgreen.archandroid.screen.profile

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 07/22/2020.
 */

val profileModule = module {
    viewModel { ProfileViewModel(get(), get()) }
    factory<ProfileContract.Repository> { ProfileRepository(get(), get()) }
}