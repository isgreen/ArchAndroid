package br.com.isgreen.archandroid.screen.pullrequest.merge

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseDialogFragment
import br.com.isgreen.archandroid.data.model.merge.MergeStrategy
import br.com.isgreen.archandroid.databinding.FragmentPullRequestMergeBinding
import br.com.isgreen.archandroid.extension.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Éverdes Soares on 08/30/2020.
 */

class PullRequestMergeFragment : BaseDialogFragment<FragmentPullRequestMergeBinding>() {

    companion object {
        const val RESULT_KEY_PULL_REQUEST_MERGED = "pullRequestMerged"
    }

    override val module = pullRequestMergeModule
    override val screenLayout = R.layout.fragment_pull_request_merge
    override val viewModel: PullRequestMergeViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentPullRequestMergeBinding
        get() = FragmentPullRequestMergeBinding::inflate

    private val mArguments: PullRequestMergeFragmentArgs by navArgs()

    //region BaseFragment
    override fun initObservers() {
        viewModel.pullRequestMerged.observe(this, {
            // TODO: 13/01/21 fazer com q retorne um PullRequest ao invés de um Boolean
            setNavigationResult(
                key = RESULT_KEY_PULL_REQUEST_MERGED,
                result = true,
                destinationId = R.id.pullRequestFragment
            )
            popUpTo(R.id.pullRequestFragment)
        })
        viewModel.mergeStrategiesFetched.observe(this, { mergeStrategies ->
            setDataInSpinner(mergeStrategies)
        })
    }

    override fun initView() {
        context?.let {
            binding.spnMergeStrategy.let { spinner ->
                spinner.adapter = ArrayAdapter<String>(it, android.R.layout.simple_spinner_dropdown_item)
                spinner.setOnItemSelectedListener { position ->

                }
            }
        }

        binding.edtMergeMessage.requestFocus()
        binding.btnMerge.setOnClickListener { doMerge() }
        binding.btnMergeCancel.setOnClickListener { dismiss() }

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
        binding.btnMerge.isEnabled = !isLoading
        binding.btnMergeCancel.isEnabled = !isLoading
        binding.edtMergeMessage.isEnabled = !isLoading
        binding.pbPullRequestMerge.isVisible = isLoading
        binding.cbMergeCloseSourceBranch.isEnabled = !isLoading
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInView() {
        val pullRequest = mArguments.argPullRequest

        binding.txtMergeBranches.text =
            "${pullRequest?.source?.branch?.name} > ${pullRequest?.destination?.branch?.name}"
    }

    private fun setDataInSpinner(mergeStrategies: List<MergeStrategy>) {
        val names = mergeStrategies.map { it.name }.toMutableList()

        context?.let {
            binding.spnMergeStrategy.let { spinner ->
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
            message = binding.edtMergeMessage.text?.toString(),
            repoFullName = pullRequest?.destination?.repository?.fullName,
            isCloseSourceBranch = binding.cbMergeCloseSourceBranch.isChecked,
            mergeStrategyPosition = binding.spnMergeStrategy.selectedItemPosition
        )

        hideKeyboard()
    }
    //endregion Local
}