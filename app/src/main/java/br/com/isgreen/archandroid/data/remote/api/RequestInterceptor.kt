package br.com.isgreen.archandroid.data.remote.api

import br.com.isgreen.archandroid.data.local.PreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Éverdes Soares on 08/04/2019.
 */

class RequestInterceptor(private val preferencesHelper: PreferencesHelper) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Content-Type", "application/json")
        requestBuilder.addHeader("Content-Type", "charset=utf-8")

        val authorization = preferencesHelper.getAuthorization()

        authorization?.accessToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer ${authorization.accessToken}")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}