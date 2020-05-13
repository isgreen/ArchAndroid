package br.com.isgreen.archandroid.di

import br.com.isgreen.archandroid.data.remote.api.Api
import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelper
import br.com.isgreen.archandroid.data.remote.apihelper.ApiHelperImpl
import br.com.isgreen.archandroid.data.local.PreferencesHelperImpl
import br.com.isgreen.archandroid.data.local.PreferencesHelper
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelperImpl
import org.koin.dsl.module

/**
 * Created by Éverdes Soares on 08/04/2019.
 */

val appModule = module {
    single { Api.Factory.create(get()) }
    single<ApiHelper> { ApiHelperImpl(get(), get()) }
    single<PreferencesHelper> { PreferencesHelperImpl(get()) }
    single<ExceptionHandlerHelper> { ExceptionHandlerHelperImpl(get()) }
}