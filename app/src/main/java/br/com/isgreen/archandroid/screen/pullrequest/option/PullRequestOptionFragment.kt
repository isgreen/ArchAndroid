package br.com.isgreen.archandroid.screen.pullrequest.option

import android.view.LayoutInflater
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseBottomSheetDialogFragment
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.databinding.FragmentPullRequestOptionBinding
import org.koin.core.module.Module

class PullRequestOptionFragment : BaseBottomSheetDialogFragment<FragmentPullRequestOptionBinding>() {

    override val module: Module? = null
    override val viewModel: BaseViewModel? = null
    override val screenLayout = R.layout.fragment_pull_request_option
    override val bindingInflater: (LayoutInflater) -> FragmentPullRequestOptionBinding =
        FragmentPullRequestOptionBinding::inflate

    var onMergeClickListener: (() -> Unit)? = null
    var onDeclineClickListener: (() -> Unit)? = null

    override fun initView() {
        binding.txtMerge.setOnClickListener {
            onMergeClickListener?.invoke()
            dismiss()
        }
        binding.txtDecline.setOnClickListener {
            onDeclineClickListener?.invoke()
            dismiss()
        }
    }

    override fun initObservers() {}

    override fun fetchInitialData() {}

    override fun showError(message: String) {}

    override fun onLoadingChanged(isLoading: Boolean) {}
}