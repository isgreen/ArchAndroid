package br.com.isgreen.archandroid.base

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.data.model.error.ErrorMessage
import br.com.isgreen.archandroid.helper.exception.ExceptionHandlerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

/**
 * Created by Éverdes Soares on 08/23/2019.
 */

abstract class BaseViewModel(
    private val exceptionHandlerHelper: ExceptionHandlerHelper
) : ViewModel(), BaseContract.ViewModel {

    override val loading: LiveData<Boolean>
        get() = mLoadingChanged
    override val message: LiveData<Any>
        get() = mMessage
    val redirect: LiveData<Int>
        get() = mRedirect

    protected val mMessage = MutableLiveData<Any>()
    protected val mLoadingChanged = MutableLiveData<Boolean>()
    protected val mRedirect = MutableLiveData<@IdRes Int>()

    protected fun defaultLaunch(
        validator: BaseValidator? = null,
        vararg anys: Any?,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch {
            try {
                mLoadingChanged.postValue(true)

                validator?.let {
                    val validation = validator.validate(*anys)
                    if (validation != BaseValidator.NO_ERROR) {
                        mLoadingChanged.postValue(false)
                        mMessage.postValue(validation)
                        return@launch
                    }
                }

                block.invoke(this)
            } catch (exception: Exception) {
                handleException(exception)
            } finally {
                mLoadingChanged.postValue(false)
            }
        }
    }

    protected fun handleException(exception: Exception) {
        val errorMessage = exceptionHandlerHelper.getErrorMessage(exception)
        callAction(errorMessage)
    }

    private fun callAction(errorMessage: ErrorMessage) {
        // to force user to login
        if (errorMessage.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            mRedirect.postValue(R.id.loginFragment)
        } else {
            mMessage.postValue(errorMessage.message)
        }
    }
}