package br.com.isgreen.archandroid.data.local

import br.com.isgreen.archandroid.data.model.login.Authorization

/**
 * Created by Éverdes Soares on 08/04/2019.
 */

interface PreferencesHelper {

    companion object {
        const val AUTHORIZATION = "authorization"
        const val LAST_AUTHORIZATION_TIME = "lastAuthorizationTime"
    }

    suspend fun clearData()

    fun getAuthorization(): Authorization?
    fun saveAuthorization(authorization: Authorization)

    fun getLastAuthorizationTime(): Long?
    fun saveLastAuthorizationTime(time: Long)

}