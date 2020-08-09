package br.com.isgreen.archandroid.screen.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.data.model.theme.Theme
import br.com.isgreen.archandroid.extension.appCompatActivity
import com.google.android.material.transition.platform.MaterialContainerTransform
import kotlinx.android.synthetic.main.appbar_and_toolbar.*
import kotlinx.android.synthetic.main.fragment_theme.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 01/14/2020.
 */

class ThemeFragment : BaseFragment() {

    override val module = themeModule
    override val screenLayout = R.layout.fragment_theme
    override val viewModel: ThemeViewModel by viewModel()

    private val mAdapter: ThemeAdapter by lazy { ThemeAdapter() }

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }
    //endregion Fragment

    //region BaseFragment
    override fun initView() {
        toolbar?.Builder(appCompatActivity)
            ?.homeIcon(R.drawable.ic_back)
            ?.title(R.string.theme)
            ?.build()

        mAdapter.onItemClickListener = { _, position, theme ->
            mAdapter.setCheckedPosition(position)
            changeTheme(theme)
        }

        rvTheme?.let { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = mAdapter
        }
    }

    override fun initObservers() {
        viewModel.themeChanged.observe(this, Observer { theme ->
            setTheme(theme)
        })
        viewModel.themesFetched.observe(this, Observer { themes ->
            mAdapter.clearData()
            mAdapter.addData(themes)
        })
        viewModel.currentThemeFetched.observe(this, Observer { themePosition ->
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