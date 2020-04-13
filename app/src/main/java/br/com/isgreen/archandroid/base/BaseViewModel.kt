package br.com.isgreen.archandroid.base

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.isgreen.archandroid.R
import kotlinx.coroutines.*
import org.universal.bibliafiel_comentada.helper.exception.ExceptionHelper
import java.net.HttpURLConnection

/**
 * Created by Éverdes Soares on 08/23/2019.
 */

abstract class BaseViewModel : ViewModel(), BaseContract.ViewModel {

    override val loading: LiveData<Boolean>
        get() = mLoadingChanged
    override val message: LiveData<Int>
        get() = mMessage
    val redirect: LiveData<Int>
        get() = mRedirect

    protected val mMessage = MutableLiveData<Int>()
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
                // to force user to login
                if (ExceptionHelper.getCode(exception) == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    mRedirect.postValue(R.id.loginFragment)
                } else {
                    mMessage.postValue(R.string.there_was_fail_try_again)
                }
            } finally {
                mLoadingChanged.postValue(false)
            }
        }
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
//        viewModelScope.coroutineContext.cancel()
    }

}