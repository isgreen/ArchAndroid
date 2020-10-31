package br.com.isgreen.archandroid.base

import org.koin.core.module.Module

/**
 * Created by Éverdes Soares on 10/18/2020.
 */

interface FragmentCompat {

    val module: Module?
    val screenLayout: Int
    val viewModel: BaseViewModel?

    fun initView()
    fun initObservers()
    fun fetchInitialData()
    fun showError(message: String)
    fun onLoadingChanged(isLoading: Boolean)
    
}