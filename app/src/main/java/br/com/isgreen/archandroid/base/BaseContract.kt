package br.com.isgreen.archandroid.base

import androidx.lifecycle.LiveData

/**
 * Created by Éverdes Soares on 08/23/2019.
 */

interface BaseContract {

    interface ViewModel {

        val message: LiveData<Any>
        val loading: LiveData<Boolean>

    }

}