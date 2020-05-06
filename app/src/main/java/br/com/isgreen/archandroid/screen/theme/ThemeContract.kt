package br.com.isgreen.archandroid.screen.theme

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.theme.Theme

/**
 * Created by Éverdes Soares on 01/14/2020.
 */

interface ThemeContract {

    interface ViewModel : BaseContract.ViewModel {
        val themeChanged: LiveData<Int>
        val currentThemeFetched: LiveData<Int>
        val themesFetched: LiveData<List<Theme>>

        fun changeTheme(mode: Int)
        fun fetchThemes(isSystemAtLeastQ: Boolean)
    }

    interface Repository {
        suspend fun saveTheme(mode: Int)
        suspend fun fetchCurrentThemeMode(): Int
        suspend fun fetchThemes(): List<Theme>
        suspend fun fetchCurrentThemeMode(): Int
    }
}