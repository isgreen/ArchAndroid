package br.com.isgreen.archandroid.screen.pullrequest.merge

import android.annotation.SuppressLint
import android.widget.ArrayAdapter
import androidx.navigation.fragment.navArgs
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseDialogFragment
import br.com.isgreen.archandroid.data.model.merge.MergeStrategy
import br.com.isgreen.archandroid.extension.*
import kotlinx.android.synthetic.main.fragment_pull_request_merge.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/30/2020.
 */

class PullRequestMergeFragment : BaseDialogFragment() {

    companion object {
        const val RESULT_KEY_PULL_REQUEST_MERGED = "pullRequestMerged"
    }

    override val module = pullRequestMergeModule
    override val screenLayout = R.layout.fragment_pull_request_merge
    override val viewModel: PullRequestMergeViewModel by viewModel()

    private val mArguments: PullRequestMergeFragmentArgs by navArgs()

    //region BaseFragment
    override fun initObservers() {
        viewModel.pullRequestMerged.observe(this, {
            setNavigationResult(key = RESULT_KEY_PULL_REQUEST_MERGED, result = true, destinationId = R.id.pullRequestFragment)
            popUpTo(R.id.pullRequestFragment)
        })
        viewModel.mergeStrategiesFetched.observe(this, { mergeStrategies ->
            setDataInSpinner(mergeStrategies)
        })
    }

    override fun initView() {
        context?.let {
            spnMergeStrategy?.let { spinner ->
                spinner.adapter = ArrayAdapter<String>(it, android.R.layout.simple_spinner_dropdown_item)
                spinner.setOnItemSelectedListener { position ->

                }
            }
        }

        edtMergeMessage?.requestFocus()

        btnMerge?.setOnClickListener {
            doMerge()
        }

        btnMergeCancel?.setOnClickListener {
            dismiss()
        }

        setDataInView()
    }

    override fun fetchInitialData() {
        viewModel.fetchMergeStrategies()
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        changeLoading(isLoading)
    }

    override fun showError(message: String) {
        showToast(message)
    }
    //endregion BaseFragment

    //region Local
    private fun changeLoading(isLoading: Boolean) {
        btnMerge?.isEnabled = !isLoading
        btnMergeCancel?.isEnabled = !isLoading
        edtMergeMessage?.isEnabled = !isLoading
        cbMergeCloseSourceBranch?.isEnabled = !isLoading
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInView() {
        val pullRequest = mArguments.argPullRequest

        txtMergeBranches?.text = "${pullRequest?.source?.branch?.name} > ${pullRequest?.destination?.branch?.name}"
    }

    private fun setDataInSpinner(mergeStrategies: List<MergeStrategy>) {
        val names = mergeStrategies.map { it.name }.toMutableList()

        context?.let {
            spnMergeStrategy?.let { spinner ->
                spinner.adapter = ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    names
                )
            }
        }
    }

    private fun doMerge() {
        val pullRequest = mArguments.argPullRequest

        viewModel.doMerge(
            pullRequestId = pullRequest?.id,
            repoFullName = pullRequest?.destination?.repository?.fullName,
            isCloseSourceBranch = cbMergeCloseSourceBranch?.isChecked,
            mergeStrategyPosition = spnMergeStrategy?.selectedItemPosition
        )

        hideKeyboard()
    }
    //endregion Local
}