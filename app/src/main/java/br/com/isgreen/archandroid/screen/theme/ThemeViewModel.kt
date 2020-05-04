package br.com.isgreen.archandroid.screen.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.theme.Theme

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

    override fun fetchThemes(isSystemAtLeastQ: Boolean) {
        defaultLaunch {
            var themes = repository.fetchThemes()
            if (isSystemAtLeastQ){
                mThemesFetched.postValue(themes)
            } else {
                themes = themes.filterNot { it.mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM }
                mThemesFetched.postValue(themes)
            }

            val currentThemeMode = repository.fetchCurrentThemeMode()
            val position = themes.indexOfFirst { it.mode == currentThemeMode }
            mCurrentThemeFetched.postValue(position)
        }
    }

    override fun changeTheme(mode: Int) {
        defaultLaunch {
            repository.saveTheme(mode)
            mThemeChanged.postValue(mode)
        }
    }
}