package br.com.isgreen.archandroid.screen.pullrequest.option

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseBottomSheetDialogFragment
import br.com.isgreen.archandroid.base.BaseViewModel
import br.com.isgreen.archandroid.databinding.FragmentPullRequestOptionBinding
import org.koin.core.module.Module

class PullRequestOptionFragment : BaseBottomSheetDialogFragment<FragmentPullRequestOptionBinding>() {

    override val module: Module? = null
    override val screenLayout = R.layout.fragment_pull_request_option
    override val viewModel: BaseViewModel? = null
    override val bindingInflater: (LayoutInflater) -> FragmentPullRequestOptionBinding =
        FragmentPullRequestOptionBinding::inflate

    var onMergeClickListener: (() -> Unit)? = null
    var onApproveClickListener: (() -> Unit)? = null
    var onDeclineClickListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pull_request_option, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        binding.txtMerge.setOnClickListener {
            onMergeClickListener?.invoke()
            dismiss()
        }
        binding.txtApprove.setOnClickListener {
            onApproveClickListener?.invoke()
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