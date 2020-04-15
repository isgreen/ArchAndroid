package br.com.isgreen.archandroid.screen.theme

import androidx.lifecycle.LiveData
import br.com.isgreen.archandroid.base.BaseContract
import br.com.isgreen.archandroid.data.model.theme.Theme

/**
 * Created by Éverdes Soares on 01/14/2020.
 */

interface ThemeContract {

    interface ViewModel : BaseContract.ViewModel {
        val themesFetched: LiveData<List<Theme>>

        fun fetchThemes()
    }

    interface Repository {
        suspend fun fetchThemes(): List<Theme>
    }
}