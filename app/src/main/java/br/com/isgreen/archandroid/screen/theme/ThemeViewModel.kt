package br.com.isgreen.archandroid.screen.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.theme.Theme
import kotlinx.coroutines.delay

/**
 * Created by Éverdes Soares on 01/14/2020.
 */

class ThemeViewModel(
    private val repository: ThemeContract.Repository
) : BaseViewModel(), ThemeContract.ViewModel {

    override val themeChanged: LiveData<Int>
        get() = mThemeChanged
    override val currentThemeFetched: LiveData<Int>
        get() = mCurrentThemeFetched
    override val themesFetched: LiveData<List<Theme>>
        get() = mThemesFetched

    private val mThemeChanged = MutableLiveData<Int>()
    private val mCurrentThemeFetched = MutableLiveData<Int>()
    private val mThemesFetched = MutableLiveData<List<Theme>>()

    override fun fetchThemes() {
        defaultLaunch {
            val themes = repository.fetchThemes()
            mThemesFetched.postValue(themes)

            var currentThemeMode = repository.fetchCurrentThemeMode()
            if (currentThemeMode == 0) {
                currentThemeMode = AppCompatDelegate.MODE_NIGHT_NO
            }

            val position = themes.indexOfFirst { it.mode == currentThemeMode }
            mCurrentThemeFetched.postValue(position)
        }
    }

    override fun changeTheme(mode: Int) {
        defaultLaunch {
            repository.saveTheme(mode)
            delay(200)
            mThemeChanged.postValue(mode)
        }
    }
}