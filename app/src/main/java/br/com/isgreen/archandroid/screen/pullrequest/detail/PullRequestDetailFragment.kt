package br.com.isgreen.archandroid.screen.pullrequest.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseFragment
import br.com.isgreen.archandroid.base.BasePagerAdapter
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.extension.appCompatActivity
import br.com.isgreen.archandroid.extension.navigate
import br.com.isgreen.archandroid.extension.navigateForResult
import br.com.isgreen.archandroid.extension.showToast
import br.com.isgreen.archandroid.screen.pullrequest.comment.PullRequestCommentFragment
import br.com.isgreen.archandroid.screen.pullrequest.commit.PullRequestCommitFragment
import br.com.isgreen.archandroid.screen.pullrequest.decline.PullRequestDeclineFragment
import br.com.isgreen.archandroid.screen.pullrequest.option.PullRequestOptionFragment
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
    private val mPagerFragments = mutableListOf<BaseFragment>()

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_pull_request_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_option -> {
                showPullRequestOption()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
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
        mPagerFragments.addAll(
            listOf(
                PullRequestOverviewFragment.newInstance(pullRequest),
                PullRequestCommitFragment.newInstance(
                    pullRequestId = pullRequest?.id,
                    repoFullName = pullRequest?.destination?.repository?.fullName
                ),
                PullRequestCommentFragment.newInstance(pullRequest)
            )
        )

        pagerPullRequestDetail?.let {
            it.offscreenPageLimit = mPagerFragments.size
            it.adapter = BasePagerAdapter(
                context = context,
                fragments = mPagerFragments,
                fragmentManager = childFragmentManager,
                pageTitles = R.array.pull_request_tab_titles
            )

            tabPullRequestDetail?.setupWithViewPager(it)
        }
    }

    private fun showPullRequestOption() {
        val fragment = PullRequestOptionFragment()
        fragment.onMergeClickListener = { showPullRequestMerge() }
        fragment.onApproveClickListener = {

        }
        fragment.onDeclineClickListener = { showPullRequestDecline() }
        fragment.show(childFragmentManager, null)
    }

    private fun showPullRequestMerge() {
        val direction = PullRequestDetailFragmentDirections
            .actionPullRequestDetailFragmentToPullRequestMergeFragment(mArguments.argPullRequest)
        navigate(direction)
    }

    private fun showPullRequestDecline() {
        val direction = PullRequestDetailFragmentDirections
            .actionPullRequestDetailFragmentToPullRequestDeclineFragment(mArguments.argPullRequest)
        navigateForResult<PullRequest>(
            directions = direction,
            key = PullRequestDeclineFragment.RESULT_KEY_PULL_REQUEST_DECLINED,
            onNavigationResult = { pullRequest ->
                (mPagerFragments[0] as? PullRequestOverviewFragment)?.updatePullRequest(pullRequest)
                (mPagerFragments[1] as? PullRequestCommitFragment)?.updateArguments(
                    pullRequestId = pullRequest.id,
                    repoFullName = pullRequest.destination?.repository?.fullName
                )
            }
        )
    }
    //endregion Local
}