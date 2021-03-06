package br.com.isgreen.archandroid.screen.theme

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.data.model.theme.Theme
import br.com.isgreen.archandroid.databinding.FragmentThemeBinding
import br.com.isgreen.archandroid.extension.appCompatActivity
import br.com.isgreen.archandroid.extension.popBackStack
import com.google.android.material.transition.platform.MaterialContainerTransform
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 01/14/2020.
 */

class ThemeFragment : BaseFragment<FragmentThemeBinding>() {

    override val module = themeModule
    override val screenLayout = R.layout.fragment_theme
    override val viewModel: ThemeViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentThemeBinding
        get() = FragmentThemeBinding::inflate

    private val mAdapter: ThemeAdapter by lazy { ThemeAdapter() }

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }
    //endregion Fragment

    //region BaseFragment
    override fun initView() {
        binding.includeToolbar.toolbar.Builder(appCompatActivity)
            .homeIcon(R.drawable.ic_back)
            .title(R.string.theme)
            .build()

        mAdapter.onItemClickListener = { _, position, theme ->
            mAdapter.setCheckedPosition(position)
            changeTheme(theme)
        }

        binding.rvTheme.adapter = mAdapter
    }

    override fun initObservers() {
        viewModel.themeChanged.observe(this, { theme ->
            setTheme(theme)
        })
        viewModel.themesFetched.observe(this, { themes ->
            mAdapter.setData(themes)
        })
        viewModel.currentThemeFetched.observe(this, { themePosition ->
            mAdapter.setCheckedPosition(themePosition)
        })
    }

    override fun fetchInitialData() {
        viewModel.fetchThemes()
    }

    override fun showError(message: String) {}

    override fun onLoadingChanged(isLoading: Boolean) {}
    //endregion BaseFragment

    //region Local
    private fun changeTheme(theme: Theme) {
        viewModel.changeTheme(theme.mode)
    }

    private fun setTheme(theme: Int) {
        AppCompatDelegate.setDefaultNightMode(theme)
        popBackStack()
    }
    //endregion Local
}