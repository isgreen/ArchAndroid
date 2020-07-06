package br.com.isgreen.archandroid.screen.login

import br.com.isgreen.archandroid.base.BaseValidatorHelper
import br.com.isgreen.archandroid.validator.LoginValidatorHelper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 08/23/2019.
 */

val loginModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    factory<BaseValidatorHelper> { LoginValidatorHelper(get()) }
    factory<LoginContract.Repository> { LoginRepository(get(), get()) }
}