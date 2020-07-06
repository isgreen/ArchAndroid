package br.com.isgreen.archandroid.data.local

import android.content.Context
import androidx.preference.PreferenceManager
import br.com.isgreen.archandroid.data.model.login.Authorization
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by Éverdes Soares on 08/23/2019.
 */

class PreferencesHelperImpl(private val context: Context) : PreferencesHelper {

    override suspend fun clearData() {
        return suspendCancellableCoroutine { continuation ->
            clearAll()
            continuation.resume(Unit)
        }
    }

    //region Authorization
    override fun getAuthorization(): Authorization? {
        val json = getString(PreferencesHelper.AUTHORIZATION)

        json?.let {
            return Gson().fromJson(json, Authorization::class.java)
        }

        return null
    }

    override fun saveAuthorization(authorization: Authorization) {
        putString(PreferencesHelper.AUTHORIZATION, Gson().toJson(authorization))
    }

    override fun getLastAuthorizationTime() = getLong(PreferencesHelper.LAST_AUTHORIZATION_TIME)

    override fun saveLastAuthorizationTime(time: Long) {
        putLong(PreferencesHelper.LAST_AUTHORIZATION_TIME, time)
    }
    //endregion Authorization

    //region Theme
    override fun getThemeMode() = getInt(PreferencesHelper.THEME_MODE)

    override fun saveThemeMode(mode: Int) = putInt(PreferencesHelper.THEME_MODE, mode)
    //endregion Theme

    //region Methods Preferences
    private fun putString(key: String, value: String?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(key, null)
    }

    private fun putInt(key: String, value: Int) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String): Int {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getInt(key, 0)
    }

    private fun putLong(key: String, value: Long) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    private fun getLong(key: String): Long {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getLong(key, 0L)
    }

    private fun putBoolean(key: String, value: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun getBoolean(key: String): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean(key, false)
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    private fun remove(key: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.edit().remove(key).apply()
    }

    private fun clearAll(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.edit().clear().commit()
    }
    //endregion
}