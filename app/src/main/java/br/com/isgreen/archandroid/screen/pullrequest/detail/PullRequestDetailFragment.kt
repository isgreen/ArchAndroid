package br.com.isgreen.archandroid.screen.pullrequest.detail

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.base.BasePagerAdapter
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.extension.appCompatActivity
import br.com.isgreen.archandroid.extension.showToast
import br.com.isgreen.archandroid.screen.pullrequest.commit.PullRequestCommitFragment
import br.com.isgreen.archandroid.screen.pullrequest.overview.PullRequestOverviewFragment
import com.google.android.material.transition.platform.MaterialContainerTransform
import kotlinx.android.synthetic.main.appbar_and_toolbar.*
import kotlinx.android.synthetic.main.fragment_pull_request_detail.*
import org.koin.core.module.Module

/**
 * Created by Éverdes Soares on 08/09/2020.
 */

class PullRequestDetailFragment : BaseFragment() {

    override val module: Module? = null
    override val viewModel: BaseViewModel? = null
    override val screenLayout = R.layout.fragment_pull_request_detail

    private val mArguments: PullRequestDetailFragmentArgs by navArgs()

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }
    //endregion Fragment

    //region BaseFragment
    override fun initObservers() {}

    override fun initView() {
        toolbar?.Builder(appCompatActivity)
            ?.homeIcon(R.drawable.ic_back)
            ?.title(R.string.pull_request)
            ?.build()

        lifecycleScope.launchWhenStarted {
            initViewPager()
        }
    }

    override fun fetchInitialData() {}

    override fun onLoadingChanged(isLoading: Boolean) {}

    override fun showError(message: String) {
        showToast(message)
    }
    //endregion BaseFragment

    //region Local
    private fun initViewPager() {
        val pullRequest = mArguments.argPullRequest
        val fragments = listOf(
            PullRequestOverviewFragment.newInstance(pullRequest),
            PullRequestCommitFragment.newInstance(pullRequest?.id, pullRequest?.destination?.repository?.fullName),
            PullRequestCommitFragment.newInstance(pullRequest?.id, pullRequest?.destination?.repository?.fullName)
        )
        pagerPullRequestDetail?.let {
            it.offscreenPageLimit = fragments.size
            it.adapter = BasePagerAdapter(
                context = context,
                fragments = fragments,
                fragmentManager = childFragmentManager,
                pageTitles = R.array.pull_request_tab_titles
            )

            tabPullRequestDetail?.setupWithViewPager(it)
        }
    }
    //endregion Local
}